package com.example.noteapp.fragments

import android.os.Bundle
import androidx.lifecycle.Observer

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx .core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.MainActivity
import com.example.noteapp.R
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.databinding.FragmentHomeBinding
import com.example.noteapp.model.Note
import com.example.noteapp.viewmodel.Noteviewmodel


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener,MenuProvider {


    private var homeBinding:FragmentHomeBinding?=null
    private val binding get()=homeBinding!!


    private lateinit var noteviewmodel: Noteviewmodel
    private lateinit var noteAdapter: NoteAdapter




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       homeBinding=FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost:MenuHost=requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner,Lifecycle.State.RESUMED)

        noteviewmodel=(activity as MainActivity).noteviewmodel
        setupHomeRecyclerView()

        binding.addNoteFab.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }


    }

    private fun updateUI(note:List<Note>?){
        if (note!=null) {
            if (note.isNotEmpty()) {
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            } else {
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }

    }
    private fun setupHomeRecyclerView(){
        noteAdapter= NoteAdapter()
        binding.homeRecyclerView.apply {
            layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter=noteAdapter

        }
        activity?.let {
            noteviewmodel.getAllNotes().observe(viewLifecycleOwner){note->
                noteAdapter.differ.submitList(note)
                updateUI(note)

            }
        }


    }
   /* private fun searchNotes(query: String?) {
        val searchQuery = "%$query%"

        // Ensure noteviewmodel is initialized
        if (::noteviewmodel.isInitialized) {
            // Use viewLifecycleOwner to observe changes in the lifecycle of the Fragment
            noteviewmodel.searchNote(searchQuery).observe(viewLifecycleOwner, Observer { list: List<Note> ->
                // Submit the new list to the adapter
                noteAdapter.differ.submitList(list)
            })
        }
    }*/



    override fun onQueryTextSubmit(query: String?): Boolean {
       return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText!=null){
           //searchNotes(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding=null
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
       menu.clear()
        menuInflater.inflate(R.menu.home_menu,menu)

        val menuSearch=menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled=false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }


}

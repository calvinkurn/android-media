package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.viewholder.SearchSuggestionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.SearchSuggestionsAdapter
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseEtalaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import javax.inject.Inject

class PlaySearchSuggestionsFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
): PlayBaseEtalaseSetupFragment() {

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var rvSuggestions: RecyclerView

    private val searchSuggestionsAdapter = SearchSuggestionsAdapter(object : SearchSuggestionViewHolder.Listener {
        override fun onSuggestionClicked(suggestionText: String) {
//            psbSearch.text = suggestionText
//            psbSearch.forceSearch()
        }
    })

    override fun getScreenName(): String = "Search Suggestions Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_search_suggestions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeSearchSuggestions()
    }

    private fun initView(view: View) {
        with (view) {
            rvSuggestions = findViewById(R.id.rv_suggestions)
        }
    }

    private fun setupView(view: View) {
        rvSuggestions.adapter = searchSuggestionsAdapter
    }

    /**
     * Observe
     */
    private fun observeSearchSuggestions() {
        viewModel.observableSuggestionList.observe(viewLifecycleOwner, Observer {
            searchSuggestionsAdapter.setItemsAndAnimateChanges(it)
        })
    }
}
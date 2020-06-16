package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.viewholder.SearchSuggestionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.SearchSuggestionsAdapter
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseEtalaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlaySearchSuggestionsViewModel
import javax.inject.Inject

class PlaySearchSuggestionsFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
): PlayBaseEtalaseSetupFragment() {

    private lateinit var viewModel: PlaySearchSuggestionsViewModel

    private lateinit var rvSuggestions: RecyclerView

    private val keyword: String
        get() = arguments?.getString(EXTRA_KEYWORD) ?: ""

    private val searchSuggestionsAdapter = SearchSuggestionsAdapter(object : SearchSuggestionViewHolder.Listener {
        override fun onSuggestionClicked(suggestionText: String) {
            etalaseSetupCoordinator.openProductSearchPage(suggestionText)
        }
    })

    override fun getScreenName(): String = "Search Suggestions Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaySearchSuggestionsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        etalaseSetupCoordinator.showBottomAction(false)
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

    override fun refresh() {}

    fun searchKeyword(keyword: String) {
        if (::viewModel.isInitialized) viewModel.loadSuggestionsFromKeyword(keyword)
    }

    private fun initView(view: View) {
        with (view) {
            rvSuggestions = findViewById(R.id.rv_suggestions)
        }
    }

    private fun setupView(view: View) {
        rvSuggestions.adapter = searchSuggestionsAdapter

        searchKeyword(keyword)
    }

    /**
     * Observe
     */
    private fun observeSearchSuggestions() {
        viewModel.observableSuggestionList.observe(viewLifecycleOwner, Observer {
            searchSuggestionsAdapter.setItemsAndAnimateChanges(it)
        })
    }

    /**
     * Transition
     */
    private fun setupTransition() {
        setupExitTransition()
    }

    private fun setupExitTransition() {
        exitTransition = Fade(Fade.OUT)
                .setDuration(300)
    }

    companion object {

        const val EXTRA_KEYWORD = "keyword"
    }
}
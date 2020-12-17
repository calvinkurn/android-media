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
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.ui.viewholder.SearchSuggestionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.SearchSuggestionsAdapter
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseEtalaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlaySearchSuggestionsViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class PlaySearchSuggestionsFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val analytic: PlayBroadcastAnalytic
): PlayBaseEtalaseSetupFragment() {

    private lateinit var viewModel: PlaySearchSuggestionsViewModel

    private lateinit var rvSuggestions: RecyclerView

    private val keyword: String
        get() = arguments?.getString(EXTRA_KEYWORD) ?: ""

    private val searchSuggestionsAdapter = SearchSuggestionsAdapter(object : SearchSuggestionViewHolder.Listener {
        override fun onSuggestionClicked(suggestionText: String, suggestionId: String) {
            etalaseSetupCoordinator.openProductSearchPage(suggestionText)
            analytic.clickProductNameSuggestion(suggestionText, suggestionId)
        }
    })

    override fun getScreenName(): String = "Search Suggestions Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaySearchSuggestionsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        analytic.viewSearchProductResult()
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

    private fun dismissToaster() {
        try {
            Toaster.snackBar.dismiss()
        } catch (e: Throwable) {}
    }

    /**
     * Observe
     */
    private fun observeSearchSuggestions() {
        viewModel.observableSuggestionList.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is NetworkResult.Success -> {
                    dismissToaster()
                    searchSuggestionsAdapter.setItemsAndAnimateChanges(result.data)
                }
                is NetworkResult.Fail -> {
                    etalaseSetupCoordinator.showToaster(
                            message = result.error.localizedMessage,
                            type = Toaster.TYPE_ERROR,
                            duration = Toaster.LENGTH_LONG,
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = View.OnClickListener {
                                result.onRetry()
                            }
                    )
                }
            }
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
package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.model.ProductLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.ResultState
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.ui.viewholder.SearchSuggestionViewHolder
import com.tokopedia.play.broadcaster.util.doOnPreDraw
import com.tokopedia.play.broadcaster.util.scroll.EndlessRecyclerViewScrollListener
import com.tokopedia.play.broadcaster.view.adapter.PlayEtalaseAdapter
import com.tokopedia.play.broadcaster.view.adapter.ProductSelectableAdapter
import com.tokopedia.play.broadcaster.view.adapter.SearchSuggestionsAdapter
import com.tokopedia.play.broadcaster.view.custom.PlaySearchBar
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalasePickerFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment() {

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var container: ViewGroup
    private lateinit var tvInfo: TextView
    private lateinit var psbSearch: PlaySearchBar
    private lateinit var rvEtalase: RecyclerView
    private lateinit var rvSearchedProducts: RecyclerView
    private lateinit var rvSuggestions: RecyclerView

    private val etalaseAdapter = PlayEtalaseAdapter(object : PlayEtalaseViewHolder.Listener {
        override fun onEtalaseClicked(etalaseId: String, sharedElements: List<View>) {
            bottomSheetCoordinator.navigateToFragment(
                    PlayEtalaseDetailFragment::class.java,
                    Bundle().apply {
                        putString(PlayEtalaseDetailFragment.EXTRA_ETALASE_ID, etalaseId)
                    },
                    sharedElements = sharedElements
            )
        }

        override fun onEtalaseBound(etalaseId: String) {
            viewModel.loadEtalaseProductPreview(etalaseId)
        }
    })

    private val searchProductsAdapter = ProductSelectableAdapter(object : ProductSelectableViewHolder.Listener {
        override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
            viewModel.selectProduct(productId, isSelected)
        }

        override fun onProductSelectError(reason: Throwable) {
            Toaster.make(
                    view = requireView(),
                    text = reason.localizedMessage,
                    duration = Toaster.LENGTH_SHORT,
                    actionText = getString(R.string.play_ok)
            )
        }
    })

    private val searchSuggestionsAdapter = SearchSuggestionsAdapter(object : SearchSuggestionViewHolder.Listener {
        override fun onSuggestionClicked(suggestionText: String) {
            psbSearch.text = suggestionText
            psbSearch.forceSearch()
        }
    })

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun refresh() {
        etalaseAdapter.notifyDataSetChanged()
    }

    override fun getScreenName(): String = "Play Etalase Picker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomSheetCoordinator.showBottomAction(false)
        postponeEnterTransition()
        return inflater.inflate(R.layout.fragment_play_etalase_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        bottomSheetCoordinator.setupTitle("Select Products or Collection")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeEtalase()
        observeSearchProducts()
        observeSearchSuggestions()
    }

    override fun onInterceptBackPressed(): Boolean {
        return false
    }

    private fun initView(view: View) {
        with(view) {
            container = this as ViewGroup
            tvInfo = findViewById(R.id.tv_info)
            psbSearch = findViewById(R.id.psb_search)
            rvEtalase = findViewById(R.id.rv_etalase)
            rvSearchedProducts = findViewById(R.id.rv_searched_products)
            rvSuggestions = findViewById(R.id.rv_suggestions)
        }
    }

    private fun setupView(view: View) {
        psbSearch.setListener(object : PlaySearchBar.Listener {

            override fun onEditStateChanged(view: PlaySearchBar, isEditing: Boolean) {
                if (isEditing) enterSearchMode()
                else exitSearchMode()
            }

            override fun onCanceled(view: PlaySearchBar) {
                exitSearchMode()
            }

            override fun onNewKeyword(view: PlaySearchBar, keyword: String) {
                viewModel.loadSuggestionsFromKeyword(keyword)
            }

            override fun onSearchButtonClicked(view: PlaySearchBar, keyword: String) {
                shouldSearchProductWithKeyword(keyword)
            }
        })

        setupEtalaseList()
        setupSearchList()
        setupSuggestionList()
    }

    private fun setupEtalaseList() {
        rvEtalase.adapter = etalaseAdapter
        rvEtalase.addItemDecoration(PlayGridTwoItemDecoration(requireContext()))
    }

    private fun setupSearchList() {
        rvSearchedProducts.layoutManager = GridLayoutManager(rvSearchedProducts.context, SPAN_COUNT, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                override fun getSpanSize(position: Int): Int {
                    return if (searchProductsAdapter.getItem(position) == ProductLoadingUiModel) SPAN_COUNT
                    else 1
                }
            }
        }
        rvSearchedProducts.adapter = searchProductsAdapter
        rvSearchedProducts.addItemDecoration(PlayGridTwoItemDecoration(requireContext()))
        scrollListener = object : EndlessRecyclerViewScrollListener(rvSearchedProducts.layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.searchProductsByKeyword(psbSearch.text, page)
            }
        }
        rvSearchedProducts.addOnScrollListener(scrollListener)
    }

    private fun setupSuggestionList() {
        rvSuggestions.adapter = searchSuggestionsAdapter
    }

    private fun enterSearchMode() {
        onSearchModeTransition()

        rvEtalase.gone()
        rvSearchedProducts.gone()

        rvSuggestions.visible()

        viewModel.loadSuggestionsFromKeyword(psbSearch.text)

        bottomSheetCoordinator.showBottomAction(false)
    }

    private fun exitSearchMode() {
        onSearchModeTransition()

        if (psbSearch.text.isNotEmpty()) {
            rvSearchedProducts.visible()
            rvEtalase.gone()
        } else {
            rvSearchedProducts.gone()
            rvEtalase.visible()
        }

        rvSuggestions.invisible()

        bottomSheetCoordinator.showBottomAction(psbSearch.text.isNotEmpty())
    }

    private fun shouldSearchProductWithKeyword(keyword: String) {
        scrollListener.resetState()
        scrollListener.loadMoreNextPage()

        exitSearchMode()
    }

    //region observe
    /**
     * Observe
     */
    private fun observeEtalase() {
        viewModel.observableEtalase.observe(viewLifecycleOwner, Observer {
            etalaseAdapter.setItemsAndAnimateChanges(it)
            startPostponedTransition()
        })
    }

    private fun observeSearchProducts() {
        viewModel.observableSearchedProducts.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                is ResultState.Success -> {
                    searchProductsAdapter.setItemsAndAnimateChanges(it.currentValue)

                    scrollListener.setHasNextPage(it.state.hasNextPage)
                    scrollListener.updateState(true)
                }
                ResultState.Loading -> {
                    searchProductsAdapter.setItemsAndAnimateChanges(it.currentValue + ProductLoadingUiModel)
                }
                is ResultState.Fail -> {
                    searchProductsAdapter.setItemsAndAnimateChanges(it.currentValue)
                    scrollListener.setHasNextPage(true)
                    scrollListener.updateState(false)
                }
            }
        })
    }

    private fun observeSearchSuggestions() {
        viewModel.observableSuggestionList.observe(viewLifecycleOwner, Observer {
            searchSuggestionsAdapter.setItemsAndAnimateChanges(it)
        })
    }
    //endregion

    /**
     * Transition
     */
    private fun onSearchModeTransition() {
        TransitionManager.beginDelayedTransition(
                container,
                Slide(Gravity.BOTTOM)
                        .addTarget(rvEtalase)
                        .setDuration(300)
                        .setStartDelay(200)
                        .excludeChildren(psbSearch, true)
        )
    }

    private fun startPostponedTransition() {
        requireView().doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun setupTransition() {
        setupExitTransition()
        setupReenterTransition()
    }

    private fun setupExitTransition() {
        exitTransition = TransitionSet()
                .addTransition(Slide(Gravity.START))
                .addTransition(Fade(Fade.OUT))
                .setDuration(300)
    }

    private fun setupReenterTransition() {
        reenterTransition = TransitionSet()
                .addTransition(Slide(Gravity.START))
                .addTransition(Fade(Fade.IN))
                .setStartDelay(200)
                .setDuration(300)
    }

    companion object {

        private const val SPAN_COUNT = 2
    }
}
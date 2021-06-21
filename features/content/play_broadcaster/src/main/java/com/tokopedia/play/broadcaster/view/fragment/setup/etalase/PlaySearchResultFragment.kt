package com.tokopedia.play.broadcaster.view.fragment.setup.etalase

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.model.ProductLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.util.extension.productNotFoundState
import com.tokopedia.play.broadcaster.util.scroll.EndlessRecyclerViewScrollListener
import com.tokopedia.play.broadcaster.view.adapter.ProductSelectableAdapter
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseEtalaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.detachableview.detachableView
import com.tokopedia.play_common.util.scroll.StopFlingScrollListener
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class PlaySearchResultFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
): PlayBaseEtalaseSetupFragment(), FragmentWithDetachableView {

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private val rvSearchedProducts: RecyclerView by detachableView(R.id.rv_searched_products)
    private val errorProductNotFound: GlobalError by detachableView(R.id.error_product_not_found)

    private val fragmentViewContainer = FragmentViewContainer()

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private var shouldLoadFirst = true

    private val keyword: String
        get() = arguments?.getString(EXTRA_KEYWORD) ?: ""

    private val searchProductsAdapter = ProductSelectableAdapter(object : ProductSelectableViewHolder.Listener {
        override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
            viewModel.selectProduct(productId, isSelected)
        }

        override fun onProductSelectError(reason: Throwable) {
            etalaseSetupCoordinator.showToaster(
                    message = reason.localizedMessage,
                    duration = Toaster.LENGTH_SHORT,
                    actionLabel = getString(R.string.play_ok)
            )
        }
    })

    override fun getScreenName(): String = "Search Suggestions Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProvider(requireParentFragment().requireParentFragment(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        etalaseSetupCoordinator.showBottomAction(true)
        return inflater.inflate(R.layout.fragment_play_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPostponedTransition()
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeSearchProducts()
    }

    override fun refresh() {
        searchProductsAdapter.notifyDataSetChanged()
    }

    override fun getViewContainer(): FragmentViewContainer {
        return fragmentViewContainer
    }

    private fun setupView(view: View) {
        errorProductNotFound.productNotFoundState()

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
                viewModel.searchProductsByKeyword(keyword, page)
            }
        }
        rvSearchedProducts.addOnScrollListener(scrollListener)
        rvSearchedProducts.addOnScrollListener(StopFlingScrollListener())

        if (shouldLoadFirst) {
            scrollListener.loadMoreNextPage()
            shouldLoadFirst = false
        }
    }

    //region observe
    /**
     * Observe
     */
    private fun observeSearchProducts() {
        viewModel.observableSearchedProducts.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                is PageResultState.Success -> {
                    searchProductsAdapter.setItemsAndAnimateChanges(it.currentValue)

                    if (it.currentValue.isEmpty()) {
                        errorProductNotFound.visible()
                        rvSearchedProducts.gone()
                        scrollListener.setHasNextPage(false)
                    } else {
                        errorProductNotFound.gone()
                        rvSearchedProducts.visible()
                        scrollListener.setHasNextPage(it.state.hasNextPage)
                    }
                    scrollListener.updateState(true)
                }
                PageResultState.Loading -> {
                    if (it.currentValue.isEmpty()) {
                        searchProductsAdapter.setItems(listOf(ProductLoadingUiModel))
                        searchProductsAdapter.notifyDataSetChanged()
                    } else searchProductsAdapter.setItemsAndAnimateChanges(it.currentValue + ProductLoadingUiModel)
                }
                is PageResultState.Fail -> {
                    etalaseSetupCoordinator.showToaster(
                            message = it.state.error.localizedMessage,
                            type = Toaster.TYPE_ERROR,
                            duration = Toaster.LENGTH_LONG
                    )
                    searchProductsAdapter.setItemsAndAnimateChanges(it.currentValue)
                    scrollListener.setHasNextPage(true)
                    scrollListener.updateState(false)
                }
            }
        })
    }
    //endregion

    /**
     * Transition
     */
    private fun setupTransition() {
        setupEnterTransition()
        setupExitTransition()
    }

    private fun setupEnterTransition() {
        enterTransition = Slide(Gravity.BOTTOM)
                .setStartDelay(150)
                .setDuration(300)
    }

    private fun setupExitTransition() {
        exitTransition = Slide(Gravity.BOTTOM)
                .setStartDelay(150)
                .setDuration(300)
    }

    private fun startPostponedTransition() {
        etalaseSetupCoordinator.startPostponedEnterTransition()
    }

    companion object {

        const val EXTRA_KEYWORD = "keyword"
        private const val SPAN_COUNT = 2
    }
}
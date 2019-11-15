package com.tokopedia.similarsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct
import kotlinx.android.synthetic.main.similar_search_fragment_layout.*

internal class SimilarSearchFragment: TkpdBaseV4Fragment() {

    companion object {
        fun getInstance(): SimilarSearchFragment {
            return SimilarSearchFragment()
        }
    }

    private var similarSearchViewModel: SimilarSearchViewModel? = null
    private var similarSearchSelectedProductView: SimilarSearchSelectedProductView? = null
    private var similarSearchAdapter: SimilarSearchAdapter? = null
    private var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    override fun getScreenName(): String {
        return "/searchproduct - product"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.similar_search_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews()
        observeViewModelData()

        similarSearchViewModel?.onViewCreated()
    }

    private fun initViewModel() {
        activity?.let { activity ->
            similarSearchViewModel = ViewModelProviders.of(activity).get(SimilarSearchViewModel::class.java)
        }
    }

    private fun initViews() {
        initSelectedProductView()
        initRecyclerView()
        disableSwipeRefreshLayout()
    }

    private fun initSelectedProductView() {
        view?.let { view ->
            val selectedProductViewListener = createSelectedProductViewListener(view)

            similarSearchSelectedProductView = SimilarSearchSelectedProductView(selectedProductViewListener)
            similarSearchSelectedProductView?.bindSelectedProductView()
        }
    }

    private fun createSelectedProductViewListener(view: View): SimilarSearchSelectedProductViewListener {
        return object : SimilarSearchSelectedProductViewListener {
            override fun getSelectedProduct(): SimilarSearchSelectedProduct? {
                return similarSearchViewModel?.similarSearchSelectedProduct
            }

            override fun getFragmentView(): View {
                return view
            }

            override fun onButtonWishlistClicked() {
                similarSearchViewModel?.onViewToggleWishlistSelectedProduct()
            }
        }
    }

    private fun initRecyclerView() {
        similarSearchAdapter = SimilarSearchAdapter()
        recyclerViewLayoutManager = createRecyclerViewSimilarSearchLayoutManager()
        endlessRecyclerViewScrollListener = createEndlessRecyclerViewScrollListener()

        recyclerViewSimilarSearch?.adapter = similarSearchAdapter
        recyclerViewSimilarSearch?.layoutManager = recyclerViewLayoutManager
        endlessRecyclerViewScrollListener?.let {
            recyclerViewSimilarSearch?.addOnScrollListener(it)
        }
    }

    private fun createRecyclerViewSimilarSearchLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
            it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }

    private fun createEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener {
        return object: EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                similarSearchViewModel?.onViewLoadMore()
            }
        }
    }

    private fun disableSwipeRefreshLayout() {
        swipeToRefreshSimilarSearch?.isEnabled = false
    }

    private fun observeViewModelData() {
        similarSearchViewModel?.getSimilarSearchLiveData()?.observe(viewLifecycleOwner, Observer {
            updateAdapter(it)
        })

//        similarSearchViewModel?.getAddWishlistSelectedProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
//            handleAddWishlistSelectedProductEvent(it)
//        })

//        similarSearchViewModel?.getRemoveWishlistSelectedProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
//            handleRemoveWishlistSelectedProductEvent(it)
//        })

        similarSearchViewModel?.getRouteToLoginPageEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            handleRouteToLoginPageEvent()
        })
    }

    private fun updateAdapter(similarSearchLiveData: State<List<Any>>) {
        when (similarSearchLiveData) {
            is State.Loading -> {
                swipeToRefreshSimilarSearch?.isRefreshing = true
                updateAdapterList(similarSearchLiveData)
                updateScrollListener()
            }
            is State.Success -> {
                swipeToRefreshSimilarSearch?.isRefreshing = false
                updateAdapterList(similarSearchLiveData)
                updateScrollListener()
            }
            is State.Error -> {

            }
        }
    }

    private fun updateAdapterList(similarSearchLiveData: State<List<Any>>) {
        similarSearchAdapter?.updateList(similarSearchLiveData.data ?: listOf())
    }

    private fun updateScrollListener() {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(similarSearchViewModel?.getHasNextPage() ?: false)
    }

    private fun handleAddWishlistSelectedProductEvent(isSuccess: Boolean) {
        if (isSuccess) {
            showSnackbar(R.string.similar_search_add_wishlist_success)
        }
        else {
            showSnackbar(R.string.similar_search_add_wishlist_failed)
        }
    }

    private fun updateSelectedProductWishlistStatus(isWishlisted: Boolean) {
        similarSearchSelectedProductView?.updateWishlistStatus(isWishlisted)
    }

    private fun handleRemoveWishlistSelectedProductEvent(isSuccess: Boolean) {
        if (isSuccess) {
            showSnackbar(R.string.similar_search_remove_wishlist_success)
        }
        else {
            showSnackbar(R.string.similar_search_remove_wishlist_failed)
        }
    }

    private fun showSnackbar(@StringRes messageStringResource: Int) {
        SnackbarManager.make(
                activity,
                getString(messageStringResource),
                Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun handleRouteToLoginPageEvent() {
        RouteManager.route(activity, ApplinkConst.LOGIN)
    }
}
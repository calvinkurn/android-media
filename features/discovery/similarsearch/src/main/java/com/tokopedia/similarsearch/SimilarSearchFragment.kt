package com.tokopedia.similarsearch

import android.content.Intent
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
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_PRODUCT_ID
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_STATUS_IS_WISHLIST
import kotlinx.android.synthetic.main.similar_search_fragment_layout.*

internal class SimilarSearchFragment: TkpdBaseV4Fragment(), SimilarProductItemListener {

    companion object {
        fun getInstance(): SimilarSearchFragment {
            return SimilarSearchFragment()
        }

        const val REQUEST_CODE_GO_TO_PRODUCT_DETAIL = 123
        const val REQUEST_CODE_GO_TO_NORMAL_CHECKOUT = 124
    }

    private var similarSearchViewModel: SimilarSearchViewModel? = null
    private var similarSearchOriginalProductView: SimilarSearchOriginalProductView? = null
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
        initRecyclerView()
        disableSwipeRefreshLayout()
    }

    private fun initRecyclerView() {
        similarSearchAdapter = SimilarSearchAdapter(this)
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
        similarSearchViewModel?.getOriginalProductLiveData()?.observe(viewLifecycleOwner, Observer {
            initOriginalProductView(it)
        })

        similarSearchViewModel?.getSimilarSearchLiveData()?.observe(viewLifecycleOwner, Observer {
            updateAdapter(it)
        })

        similarSearchViewModel?.getRouteToLoginPageEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            handleRouteToLoginPageEvent()
        })

        similarSearchViewModel?.getUpdateWishlistOriginalProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            updateSelectedProductWishlistStatus(it)
        })

        similarSearchViewModel?.getUpdateWishlistSimilarProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            updateSimilarProductWishlistStatus(it)
        })

        similarSearchViewModel?.getAddWishlistEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            handleAddWishlistEvent(it)
        })

        similarSearchViewModel?.getRemoveWishlistEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            handleRemoveWishlistEvent(it)
        })
    }

    private fun initOriginalProductView(originalProduct: Product) {
        view?.let { view ->
            val selectedProductViewListener = createSelectedProductViewListener(view, originalProduct)

            similarSearchOriginalProductView = SimilarSearchOriginalProductView(selectedProductViewListener)
            similarSearchOriginalProductView?.bindOriginalProductView(originalProduct)
        }
    }

    private fun createSelectedProductViewListener(view: View, originalProduct: Product): SimilarSearchOriginalProductViewListener {
        return object : SimilarSearchOriginalProductViewListener {
            override fun getFragmentView(): View {
                return view
            }

            override fun onItemClicked() {
                routeToProductDetail(originalProduct.id)
            }

            override fun onButtonWishlistClicked() {
                similarSearchViewModel?.onViewToggleWishlistOriginalProduct()
            }

            override fun onButtonBuyClicked() {
                selectedProductOnButtonBuyClicked()
            }

            override fun onButtonAddToCartClicked() {
                selectedProductOnButtonAddToCartClicked(originalProduct)
            }
        }
    }

    private fun routeToProductDetail(productId: String) {
        activity?.let { activity ->
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)

            startActivityForResult(intent, REQUEST_CODE_GO_TO_PRODUCT_DETAIL)
        }
    }

    private fun selectedProductOnButtonBuyClicked() {

    }

    private fun selectedProductOnButtonAddToCartClicked(originalProduct: Product) {
        activity?.let { activity ->
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).also {
                it.putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, originalProduct.shop.id)
                it.putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, originalProduct.id)
            }

            startActivityForResult(intent, REQUEST_CODE_GO_TO_NORMAL_CHECKOUT)
        }
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

    private fun handleRouteToLoginPageEvent() {
        RouteManager.route(activity, ApplinkConst.LOGIN)
    }

    private fun updateSelectedProductWishlistStatus(isWishlisted: Boolean) {
        similarSearchOriginalProductView?.updateWishlistStatus(isWishlisted)
    }

    private fun updateSimilarProductWishlistStatus(similarProductItem: Product) {
        similarSearchAdapter?.updateSimilarProductItemWishlistStatus(similarProductItem)
    }

    private fun handleAddWishlistEvent(isSuccess: Boolean) {
        if (isSuccess) {
            showSnackbar(R.string.similar_search_add_wishlist_success)
        }
        else {
            showSnackbar(R.string.similar_search_add_wishlist_failed)
        }
    }

    private fun showSnackbar(@StringRes messageStringResource: Int) {
        SnackbarManager.make(
                activity,
                getString(messageStringResource),
                Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun handleRemoveWishlistEvent(isSuccess: Boolean) {
        if (isSuccess) {
            showSnackbar(R.string.similar_search_remove_wishlist_success)
        }
        else {
            showSnackbar(R.string.similar_search_remove_wishlist_failed)
        }
    }

    override fun onItemClicked(similarProductItem: Product, adapterPosition: Int) {
        routeToProductDetail(similarProductItem.id)
    }

    override fun onItemWishlistClicked(productId: String, isWishlisted: Boolean) {
        similarSearchViewModel?.onViewToggleWishlistSimilarProduct(productId, isWishlisted)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_GO_TO_PRODUCT_DETAIL && data?.extras != null) {
            val productId = data.extras?.getString(WISHLIST_PRODUCT_ID, "")
            val isWishlisted = data.extras?.getBoolean(WISHLIST_STATUS_IS_WISHLIST, false) ?: false

            similarSearchViewModel?.onViewUpdateProductWishlistStatus(productId, isWishlisted)
        }
    }
}
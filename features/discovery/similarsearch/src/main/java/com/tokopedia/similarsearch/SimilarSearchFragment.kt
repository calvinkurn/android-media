package com.tokopedia.similarsearch

import android.app.Activity
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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_PRODUCT_ID
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_STATUS_IS_WISHLIST
import com.tokopedia.purchase_platform.common.constant.ATC_AND_BUY
import com.tokopedia.purchase_platform.common.constant.ProductAction
import com.tokopedia.similarsearch.emptyresult.EmptyResultListener
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.originalproduct.OriginalProductView
import com.tokopedia.similarsearch.originalproduct.OriginalProductViewListener
import com.tokopedia.similarsearch.productitem.SimilarProductItemListener
import com.tokopedia.similarsearch.recyclerview.SimilarSearchAdapter
import com.tokopedia.similarsearch.recyclerview.SimilarSearchItemDecoration
import com.tokopedia.similarsearch.tracking.SimilarSearchTracking
import com.tokopedia.similarsearch.utils.asObjectDataLayerImpressionAndClick
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.similar_search_fragment_layout.*

internal class SimilarSearchFragment: TkpdBaseV4Fragment(), SimilarProductItemListener, EmptyResultListener {

    companion object {
        fun getInstance(): SimilarSearchFragment {
            return SimilarSearchFragment()
        }

        const val REQUEST_CODE_GO_TO_PRODUCT_DETAIL = 123
        const val REQUEST_CODE_GO_TO_CHECKOUT = 124
    }

    private var similarSearchViewModel: SimilarSearchViewModel? = null
    private var originalProductView: OriginalProductView? = null
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
    }

    private fun initRecyclerView() {
        initRecyclerViewAdapter()
        initRecyclerViewLayoutManager()
        initRecyclerViewEndlessScrollListener()
        initRecyclerViewItemDecoration()



//        recyclerViewSimilarSearch?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                Log.v("RecyclerView Scroll", "OnScroll dx: $dx, dy: $dy, vertical scroll offset: ${recyclerView.computeVerticalScrollOffset()}")
//                if (dy > 0) {
//                    originalProductView?.collapse(recyclerViewSimilarSearch?.computeVerticalScrollOffset() ?: 0)
//                }
//                else if (dy <= 0) {
//                    originalProductView?.expand(recyclerViewSimilarSearch?.computeVerticalScrollOffset() ?: 0)
//                }
//            }
//        })
    }

    private fun initRecyclerViewAdapter() {
        similarSearchAdapter = SimilarSearchAdapter(this, this)
        recyclerViewSimilarSearch?.adapter = similarSearchAdapter
    }

    private fun initRecyclerViewLayoutManager() {
        recyclerViewLayoutManager = createRecyclerViewSimilarSearchLayoutManager()
        recyclerViewSimilarSearch?.layoutManager = recyclerViewLayoutManager
    }

    private fun createRecyclerViewSimilarSearchLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
            it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }

    private fun initRecyclerViewEndlessScrollListener() {
        endlessRecyclerViewScrollListener = createEndlessRecyclerViewScrollListener()
        endlessRecyclerViewScrollListener?.let {
            recyclerViewSimilarSearch?.addOnScrollListener(it)
        }
    }

    private fun createEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener {
        return object: EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                similarSearchViewModel?.onViewLoadMore()
            }
        }
    }

    private fun initRecyclerViewItemDecoration() {
        activity?.let { activity ->
            recyclerViewSimilarSearch?.addItemDecoration(createSimilarSearchItemDecoration(activity))
        }
    }

    private fun createSimilarSearchItemDecoration(activity: Activity): RecyclerView.ItemDecoration {
        return SimilarSearchItemDecoration(activity.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16))
    }

    private fun observeViewModelData() {
        observeOriginalProductLiveData()
        observeSimilarSearchLiveData()
        observeRouteToLoginEventLiveData()
        observeUpdateWishlistOriginalProductEventLiveData()
        observeWishlistSimilarProductEventLiveData()
        observeAddWishlistEventLiveData()
        observeRemoveWishlistEventLiveData()
        observeAddToCartEventLiveData()
        observeTrackingImpressionSimilarProductEventLiveData()
        observeTrackingEmptyResultEventLiveData()
        observeTrackingWishlistEventLiveData()
        observeTrackingAddToCartEventLiveData()
    }

    private fun observeOriginalProductLiveData() {
        similarSearchViewModel?.getOriginalProductLiveData()?.observe(viewLifecycleOwner, Observer {
            initOriginalProductView(it)
        })
    }

    private fun initOriginalProductView(originalProduct: Product) {
        view?.let { view ->
            val selectedProductViewListener = createSelectedProductViewListener(view, originalProduct)

            originalProductView = OriginalProductView(selectedProductViewListener)
            originalProductView?.bindOriginalProductView(originalProduct)
        }
    }

    private fun createSelectedProductViewListener(view: View, originalProduct: Product): OriginalProductViewListener {
        return object : OriginalProductViewListener {
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
                selectedProductOnButtonBuyClicked(originalProduct)
            }

            override fun onButtonAddToCartClicked() {
                selectedProductOnButtonAddToCartClicked()
            }
        }
    }

    private fun routeToProductDetail(productId: String) {
        activity?.let { activity ->
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)

            startActivityForResult(intent, REQUEST_CODE_GO_TO_PRODUCT_DETAIL)
        }
    }

    private fun selectedProductOnButtonBuyClicked(originalProduct: Product) {
        SimilarSearchTracking.trackEventClickBuy()
        routeToCheckout(originalProduct, ATC_AND_BUY)
    }

    private fun selectedProductOnButtonAddToCartClicked() {
        similarSearchViewModel?.onViewClickAddToCart()
    }

    private fun routeToCheckout(originalProduct: Product, @ProductAction action: Int) {
        activity?.let { activity ->
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).also {
                it.putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, originalProduct.shop.id.toString())
                it.putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, originalProduct.id)
                it.putExtra(ApplinkConst.Transaction.EXTRA_ACTION, action)
            }

            startActivityForResult(intent, REQUEST_CODE_GO_TO_CHECKOUT)
        }
    }

    private fun observeSimilarSearchLiveData() {
        similarSearchViewModel?.getSimilarSearchLiveData()?.observe(viewLifecycleOwner, Observer {
            updateViewContent(it)
        })
    }

    private fun updateViewContent(similarSearchLiveData: State<List<Any>>) {
        if (similarSearchLiveData is State.Loading) {
            updateProgressBarVisiblity(View.VISIBLE)
        }
        else {
            updateProgressBarVisiblity(View.GONE)
        }

        updateAdapterList(similarSearchLiveData)
        updateScrollListener()
    }

    private fun updateProgressBarVisiblity(visibility: Int) {
        progressBarSimilarSearch?.visibility = visibility
    }

    private fun updateAdapterList(similarSearchLiveData: State<List<Any>>) {
        similarSearchAdapter?.updateList(similarSearchLiveData.data ?: listOf())
    }

    private fun updateScrollListener() {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(similarSearchViewModel?.getHasNextPage() ?: false)
    }

    private fun observeRouteToLoginEventLiveData() {
        similarSearchViewModel?.getRouteToLoginPageEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            RouteManager.route(activity, ApplinkConst.LOGIN)
        })
    }

    private fun observeUpdateWishlistOriginalProductEventLiveData() {
        similarSearchViewModel?.getUpdateWishlistOriginalProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            originalProductView?.updateWishlistStatus(it)
        })
    }

    private fun observeWishlistSimilarProductEventLiveData() {
        similarSearchViewModel?.getUpdateWishlistSimilarProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            similarSearchAdapter?.updateSimilarProductItemWishlistStatus(it)
        })
    }

    private fun observeAddWishlistEventLiveData() {
        similarSearchViewModel?.getAddWishlistEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            handleAddWishlistEvent(it)
        })
    }

    private fun handleAddWishlistEvent(isSuccess: Boolean) {
        if (isSuccess) {
            showSnackbar(R.string.similar_search_add_wishlist_success)
        }
        else {
            showSnackbar(R.string.similar_search_add_wishlist_failed, Toaster.TYPE_ERROR)
        }
    }

    private fun showSnackbar(@StringRes messageStringResource: Int, toasterType: Int = Toaster.TYPE_NORMAL) {
        view?.let { view ->
            Toaster.make(view, getString(messageStringResource), Snackbar.LENGTH_SHORT, toasterType)
        }
    }

    private fun observeRemoveWishlistEventLiveData() {
        similarSearchViewModel?.getRemoveWishlistEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            handleRemoveWishlistEvent(it)
        })
    }

    private fun handleRemoveWishlistEvent(isSuccess: Boolean) {
        if (isSuccess) {
            showSnackbar(R.string.similar_search_remove_wishlist_success)
        }
        else {
            showSnackbar(R.string.similar_search_remove_wishlist_failed, Toaster.TYPE_ERROR)
        }
    }

    private fun observeAddToCartEventLiveData() {
        similarSearchViewModel?.getAddToCartEventLiveData()?.observe(viewLifecycleOwner, EventObserver { isSuccess ->
            handleAddToCartEvent(isSuccess)
        })
    }

    private fun handleAddToCartEvent(isSuccess: Boolean) {
        if (isSuccess) {
            showSnackbar(R.string.similar_search_add_to_cart_success)
        }
        else {
            showSnackbar(R.string.similar_search_add_to_cart_failed, Toaster.TYPE_ERROR)
        }
    }

    private fun observeTrackingImpressionSimilarProductEventLiveData() {
        similarSearchViewModel?.getTrackingImpressionSimilarProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            SimilarSearchTracking.trackEventImpressionSimilarProduct(getOriginalProductId(), it)
        })
    }

    private fun getOriginalProductId(): String {
        return similarSearchViewModel?.getOriginalProductId() ?: ""
    }

    private fun observeTrackingEmptyResultEventLiveData() {
        similarSearchViewModel?.getTrackingEmptyResultEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            handleEventTrackEmptyResultSimilarSearch(it)
        })
    }

    private fun handleEventTrackEmptyResultSimilarSearch(isTrackEmptySearch: Boolean) {
        if (isTrackEmptySearch) {
            SimilarSearchTracking.trackEventEmptyResultSimilarSearch(getOriginalProductId(), screenName)
        }
    }

    private fun observeTrackingWishlistEventLiveData() {
        similarSearchViewModel?.getTrackingWishlistEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            SimilarSearchTracking.trackEventSuccessWishlistSimilarProduct(it)
        })
    }

    private fun observeTrackingAddToCartEventLiveData() {
        similarSearchViewModel?.getTrackingAddToCartEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            SimilarSearchTracking.trackEventSuccessAddToCart(it)
        })
    }

    override fun onItemClicked(similarProductItem: Product, adapterPosition: Int) {
        trackEventClickSimilarProduct(similarProductItem)
        routeToProductDetail(similarProductItem.id)
    }

    private fun trackEventClickSimilarProduct(similarProductItem: Product) {
        val screenName = "$screenName ${similarProductItem.id}"
        val similarProductItemAsObjectDataLayer = similarProductItem.asObjectDataLayerImpressionAndClick()

        SimilarSearchTracking.trackEventClickSimilarProduct(getOriginalProductId(), screenName, similarProductItemAsObjectDataLayer)
    }

    override fun onItemWishlistClicked(productId: String, isWishlisted: Boolean) {
        similarSearchViewModel?.onViewToggleWishlistSimilarProduct(productId, isWishlisted)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_CODE_GO_TO_PRODUCT_DETAIL -> handleResultFromProductDetail(data)
        }
    }

    private fun handleResultFromProductDetail(data: Intent?) {
        if (data?.extras != null) {
            val productId = data.extras?.getString(WISHLIST_PRODUCT_ID, "")
            val isWishlisted = data.extras?.getBoolean(WISHLIST_STATUS_IS_WISHLIST, false) ?: false

            similarSearchViewModel?.onViewUpdateProductWishlistStatus(productId, isWishlisted)
        }
    }

    override fun onEmptyResultButtonClicked() {
        activity?.finish()
    }
}
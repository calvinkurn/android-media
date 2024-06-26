package com.tokopedia.similarsearch

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_PRODUCT_ID
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_STATUS_IS_WISHLIST
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.similarsearch.databinding.SimilarSearchFragmentLayoutBinding
import com.tokopedia.similarsearch.databinding.SimilarSearchToolbarLayoutBinding
import com.tokopedia.similarsearch.emptyresult.EmptyResultListener
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.originalproduct.OriginalProductViewListener
import com.tokopedia.similarsearch.productitem.SimilarProductItemListener
import com.tokopedia.similarsearch.recyclerview.SimilarSearchAdapter
import com.tokopedia.similarsearch.recyclerview.SimilarSearchItemDecoration
import com.tokopedia.similarsearch.tracking.SimilarSearchTracking
import com.tokopedia.similarsearch.utils.asObjectDataLayerImpressionAndClick
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler

internal class SimilarSearchFragment: TkpdBaseV4Fragment(), SimilarProductItemListener, EmptyResultListener {

    private var binding: SimilarSearchFragmentLayoutBinding? by viewBinding()
    private val toolbarBinding: SimilarSearchToolbarLayoutBinding?
        get() = binding?.toolbarContent

    private var similarSearchViewModel: SimilarSearchViewModel? = null
    private var similarSearchAdapter: SimilarSearchAdapter? = null
    private var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var recyclerViewVerticalScrollDistance = 0

    override fun getScreenName(): String {
        return "/searchproduct - product"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.similar_search_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureToolbar()
        initViewModel()
        initViews()
        observeViewModelData()

        similarSearchViewModel?.onViewCreated()
    }

    private fun configureToolbar() {
        activity?.run {
            if (this !is AppCompatActivity) return

            setSupportActionBar(binding?.toolbar)
            configureSupportActionBar(supportActionBar)
            configureToolbarOnClick()
        }
    }

    private fun configureSupportActionBar(supportActionBar: ActionBar?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)
    }

    private fun configureToolbarOnClick() {
        toolbarBinding?.imageViewBack?.setOnClickListener {
            activity?.onBackPressed()
        }
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
        initRecyclerViewAnimateOriginalProductViewListener()
    }

    private fun initRecyclerViewAdapter() {
        similarSearchAdapter = SimilarSearchAdapter(this, this)
        binding?.recyclerViewSimilarSearch?.adapter = similarSearchAdapter
    }

    private fun initRecyclerViewLayoutManager() {
        recyclerViewLayoutManager = createRecyclerViewSimilarSearchLayoutManager()
        binding?.recyclerViewSimilarSearch?.layoutManager = recyclerViewLayoutManager
    }

    private fun createRecyclerViewSimilarSearchLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
            it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }

    private fun initRecyclerViewEndlessScrollListener() {
        endlessRecyclerViewScrollListener = createEndlessRecyclerViewScrollListener()
        endlessRecyclerViewScrollListener?.let {
            binding?.recyclerViewSimilarSearch?.addOnScrollListener(it)
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
            binding?.recyclerViewSimilarSearch?.addItemDecoration(
                createSimilarSearchItemDecoration(activity)
            )
        }
    }

    private fun initRecyclerViewAnimateOriginalProductViewListener() {
        val recyclerViewOnScrollListener = createRecyclerViewOnScrollListener()
        binding?.recyclerViewSimilarSearch?.addOnScrollListener(recyclerViewOnScrollListener)
    }

    private fun createRecyclerViewOnScrollListener(): RecyclerView.OnScrollListener {
        return object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                recyclerViewVerticalScrollDistance += dy

                applyToolbarElevation()
                toolbarBinding?.originalProductView?.animateBasedOnScroll(dy)
            }
        }
    }

    private fun applyToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (recyclerViewVerticalScrollDistance >= 10) {
                binding?.toolbar?.applyElevation()
            }
            else {
                binding?.toolbar?.removeElevation()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun Toolbar.applyElevation() {
        if (this.elevation == 0f) {
            this.elevation = 6f.toPx()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun Toolbar.removeElevation() {
        if (this.elevation > 0f) {
            binding?.toolbar?.elevation = 0f
        }
    }

    private fun Float.toPx(): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)
    }

    private fun createSimilarSearchItemDecoration(activity: Activity): RecyclerView.ItemDecoration {
        return SimilarSearchItemDecoration(activity.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16))
    }

    private fun observeViewModelData() {
        observeOriginalProductLiveData()
        observeSimilarSearchLiveData()
        observeRouteToLoginEventLiveData()
        observeUpdateWishlistOriginalProductEventLiveData()
        observeAddWishlistV2EventLiveData()
        observeRemoveWishlistV2EventLiveData()
        observeAddToCartEventLiveData()
        observeRouteToCartEventLiveData()
        observeTrackingImpressionSimilarProductEventLiveData()
        observeTrackingEmptyResultEventLiveData()
        observeTrackingWishlistEventLiveData()
        observeTrackingAddToCartEventLiveData()
        observeTrackingBuyEventLiveData()
    }

    private fun observeOriginalProductLiveData() {
        similarSearchViewModel?.getOriginalProductLiveData()?.observe(viewLifecycleOwner, Observer {
            initOriginalProductView(it)
        })
    }

    private fun initOriginalProductView(originalProduct: Product) {
        toolbarBinding?.originalProductView?.visible()

        val originalProductViewListener = createOriginalProductViewListener(originalProduct)
        toolbarBinding?.originalProductView?.bindOriginalProductView(originalProduct, originalProductViewListener)
    }

    private fun createOriginalProductViewListener(originalProduct: Product): OriginalProductViewListener {
        return object : OriginalProductViewListener {

            override fun onItemClicked() {
                routeToProductDetail(originalProduct.applink)
            }

            override fun onButtonWishlistClicked() {
                context?.let {
                    similarSearchViewModel?.onViewToggleWishlistV2OriginalProduct()
                }
            }

            override fun onButtonBuyClicked() {
                similarSearchViewModel?.onViewClickBuy()
            }

            override fun onButtonAddToCartClicked() {
                similarSearchViewModel?.onViewClickAddToCart()
            }
        }
    }

    private fun routeToProductDetail(applink: String) {
        activity?.let { activity ->
            val intent = RouteManager.getIntent(activity, applink)

            startActivityForResult(intent, REQUEST_CODE_GO_TO_PRODUCT_DETAIL)
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
        binding?.progressBarSimilarSearch?.visibility = visibility
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
            toolbarBinding?.originalProductView?.updateWishlistStatus(it)
        })
    }

    private fun observeAddWishlistV2EventLiveData() {
        similarSearchViewModel?.getAddWishlistV2EventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            handleAddWishlistV2Event(it)
        })
    }

    private fun observeRemoveWishlistV2EventLiveData() {
        similarSearchViewModel?.getRemoveWishlistV2EventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            handleRemoveWishlistV2Event(it)
        })
    }

    private fun handleAddWishlistV2Event(result: AddToWishlistV2Response.Data.WishlistAddV2) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(result, context, v)
            }
        }
    }

    private fun handleRemoveWishlistV2Event(result: DeleteWishlistV2Response.Data.WishlistRemoveV2) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(result, context, v)
            }
        }
    }

    private fun showSnackbar(@StringRes messageStringResource: Int, toasterType: Int = Toaster.TYPE_NORMAL) {
        view?.let { view ->
            Toaster.make(view, getString(messageStringResource), Snackbar.LENGTH_SHORT, toasterType)
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
            showAddToCartErrorMessage()
        }
    }

    private fun showAddToCartErrorMessage() {
        view?.let {
            val errorMessage = getAddToCartErrorMessage()
            Toaster.make(it, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR)
        }
    }

    private fun getAddToCartErrorMessage(): String {
        return similarSearchViewModel?.getAddToCartFailedMessage() ?: getString(R.string.similar_search_add_to_cart_failed)
    }

    private fun observeRouteToCartEventLiveData() {
        similarSearchViewModel?.getRouteToCartPageEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            activity?.let { activity ->
                RouteManager.route(activity, ApplinkConst.CART)
            }
        })
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
            SimilarSearchTracking.trackEventClickAddToCart(it)
        })
    }

    private fun observeTrackingBuyEventLiveData() {
        similarSearchViewModel?.getTrackingBuyEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            SimilarSearchTracking.trackEventClickBuy(it)
        })
    }

    override fun onItemClicked(similarProductItem: Product, adapterPosition: Int) {
        trackEventClickSimilarProduct(similarProductItem)
        routeToProductDetail(similarProductItem.applink)
    }

    private fun trackEventClickSimilarProduct(similarProductItem: Product) {
        val screenName = "$screenName ${similarProductItem.id}"
        val similarProductItemAsObjectDataLayer = similarProductItem.asObjectDataLayerImpressionAndClick()

        SimilarSearchTracking.trackEventClickSimilarProduct(getOriginalProductId(), screenName, similarProductItemAsObjectDataLayer)
    }

    override fun onThreeDotsClicked(similarProductItem: Product, adapterPosition: Int) {
        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = similarProductItem.isWishlisted,
                        productId = similarProductItem.id
                )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_CODE_GO_TO_PRODUCT_DETAIL -> handleResultFromProductDetail(data)
        }

        handleProductCardOptionsActivityResult(requestCode, resultCode, data, object: ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                similarSearchViewModel?.onReceiveProductCardOptionsWishlistResult(productCardOptionsModel)
            }
        })
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

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }

    companion object {
        const val REQUEST_CODE_GO_TO_PRODUCT_DETAIL = 123
    }
}

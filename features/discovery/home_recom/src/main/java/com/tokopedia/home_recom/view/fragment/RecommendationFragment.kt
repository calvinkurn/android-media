package com.tokopedia.home_recom.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.*
import com.tokopedia.home_recom.util.*
import com.tokopedia.home_recom.view.adapter.HomeRecommendationAdapter
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.view.fragment.RecommendationFragment.Companion.PDP_EXTRA_PRODUCT_ID
import com.tokopedia.home_recom.view.fragment.RecommendationFragment.Companion.PDP_EXTRA_UPDATED_POSITION
import com.tokopedia.home_recom.view.fragment.RecommendationFragment.Companion.REQUEST_FROM_PDP
import com.tokopedia.home_recom.view.fragment.RecommendationFragment.Companion.SAVED_PRODUCT_ID
import com.tokopedia.home_recom.view.fragment.RecommendationFragment.Companion.SHARE_PRODUCT_TITLE
import com.tokopedia.home_recom.view.fragment.RecommendationFragment.Companion.SPAN_COUNT
import com.tokopedia.home_recom.view.fragment.RecommendationFragment.Companion.WIHSLIST_STATUS_IS_WISHLIST
import com.tokopedia.home_recom.view.viewholder.ProductInfoViewHolder
import com.tokopedia.home_recom.view.viewholder.RecommendationErrorViewHolder
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.fragment_product_info.*
import javax.inject.Inject

/**
 * A Class of Recommendation Fragment.
 *
 * This class for handling Recommendation Page, it will shown List of ProductDetail, Recommendation Items, Recommendation Carousel
 *
 * @property viewModelFactory the factory for ViewModel provide by Dagger.
 * @property trackingQueue the queue util for handle tracking.
 * @property productId the productId of ProductDetail.
 * @property queryParam the ref code for know source page.
 * @property lastClickLayoutType for handling last click product layout type.
 * @property lastParentPosition for handling last click product and get know parent position for nested recyclerView.
 * @property viewModelProvider the viewModelProvider by Dagger
 * @property adapterFactory the factory for handling type factory Visitor Pattern
 * @property adapter the adapter for recyclerView
 * @property recommendationWidgetViewModel the viewModel for recommendation
 * @property menu the menu of this activity.
 * @property SPAN_COUNT the span count for list.
 * @property SHARE_PRODUCT_TITLE the const value for sharing title.
 * @property SAVED_PRODUCT_ID the const value for handling save productId at SaveInstance.
 * @property WIHSLIST_STATUS_IS_WISHLIST the const value for get extras `isWhislist` from ActivityFromResult ProductDetailActivity.
 * @property PDP_EXTRA_PRODUCT_ID the const value for get extras `product_id` from ActivityFromResult ProductDetailActivity.
 * @property PDP_EXTRA_UPDATED_POSITION the const value for get extras index item from ActivityFromResult ProductDetailActivity.
 * @property REQUEST_FROM_PDP the const value for set request calling startActivityForResult ProductDetailActivity.
 * @constructor Creates an empty recommendation.
 */
@SuppressLint("SyntheticAccessor")
open class RecommendationFragment: BaseListFragment<HomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl>(), RecommendationListener, TitleListener, RecommendationErrorListener, ProductInfoViewHolder.ProductInfoListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var trackingQueue: TrackingQueue? = null
    private var productId: String = ""
    private var queryParam: String = ""
    private var ref: String = ""
    private var internalRef: String = ""

    private var menu: Menu? = null
    private var lastClickLayoutType: String? = null
    private var lastParentPosition: Int? = null
    private lateinit var viewModelProvider: ViewModelProvider
    private val adapterFactory by lazy { HomeRecommendationTypeFactoryImpl(this, this, this, this) }
    private val adapter by lazy { HomeRecommendationAdapter(adapterTypeFactory) }
    private lateinit var recommendationWidgetViewModel: RecommendationPageViewModel

    companion object{
        private const val className = "com.tokopedia.home_recom.view.fragment.RecommendationFragment"
        private const val SPAN_COUNT = 2
        private const val SAVED_PRODUCT_ID = "saved_product_id"
        private const val SAVED_REF = "saved_ref"
        private const val SAVED_QUERY_PARAM = "saved_query_param"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val RECOMMENDATION_APP_LINK = "https://tokopedia.com/rekomendasi/%s"
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
        private const val REQUEST_FROM_PDP = 394
        private const val REQUEST_CODE_LOGIN = 283

        fun newInstance(productId: String = "", queryParam: String = "", ref: String = "null", internalRef: String = "",@FragmentInflater fragmentInflater: String = FragmentInflater.DEFAULT) = RecommendationFragment().apply {
            this.productId = productId
            this.queryParam = queryParam
            this.ref = ref
            this.internalRef = internalRef
            this.fragmentInflater = fragmentInflater
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommendation, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity ?.let {
            viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            recommendationWidgetViewModel = viewModelProvider.get(RecommendationPageViewModel::class.java)
            trackingQueue = TrackingQueue(it)
        }
        savedInstanceState?.let{
            productId = it.getString(SAVED_PRODUCT_ID) ?: ""
            queryParam = it.getString(SAVED_QUERY_PARAM) ?: ""
            ref = it.getString(SAVED_REF) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run{
            (this as AppCompatActivity).supportActionBar?.title = getString(R.string.recom_home_recommendation)
        }
        if(productId.isEmpty()) {
            RecommendationPageTracking.sendScreenRecommendationPage(
                    screenName,
                    null,
                    ref)
        }
        else {
            RecommendationPageTracking.sendScreenRecommendationPage(
                    screenName,
                    productId,
                    ref)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recommendation_page_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = com.tokopedia.home_recom.R.id.swipe_refresh_layout

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_PRODUCT_ID, productId)
        outState.putString(SAVED_REF, ref)
        outState.putString(SAVED_QUERY_PARAM, queryParam)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        disableLoadMore()
        getRecyclerView(view)?.layoutManager = recyclerViewLayoutManager
        getRecyclerView(view)?.addItemDecoration(StaggerGridSpacingItemDecoration(30))
        observeLiveData()
        observeAtcLiveData()
        observeBuyNowLiveData()
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun getRecyclerViewResourceId(): Int {
        return com.tokopedia.home_recom.R.id.recycler_view
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getAdapterTypeFactory(): HomeRecommendationTypeFactoryImpl {
        return adapterFactory
    }

    override fun createAdapterInstance(): BaseListAdapter<HomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl> {
        return adapter
    }

    override fun onItemClicked(dataModel: HomeRecommendationDataModel) {
    }

    override fun getScreenName(): String = getString(R.string.home_recom_screen_name)

    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        loadData()
    }

    override fun onPause() {
        super.onPause()
        trackingQueue?.sendAll()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                if(position >= 0 && adapter.data.size > position && adapter.data[position] is RecommendationItemDataModel) {
                    (adapter.data[position] as RecommendationItemDataModel).productItem.isWishlist = wishlistStatusFromPdp
                    adapter.notifyItemChanged(position, wishlistStatusFromPdp)
                }
            }
        }
        handleProductCardOptionsActivityResult(requestCode, resultCode, data,
                object : ProductCardOptionsWishlistCallback {
                    override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                        handleWishlistAction(productCardOptionsModel)
                    }
                })
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (productCardOptionsModel == null) return
        val wishlistResult = productCardOptionsModel.wishlistResult
        if (wishlistResult.isUserLoggedIn) {
            if (wishlistResult.isSuccess) {
                if (wishlistResult.isAddWishlist) {
                    if(productId.isNotBlank() || productId.isNotEmpty()){
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLoginWithProductId(true, ref)
                    }else {
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLogin(true, productCardOptionsModel.screenName, ref)
                    }
                    showMessageSuccessAddWishlist()
                } else {
                    if(productId.isNotBlank() || productId.isNotEmpty()){
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLoginWithProductId(false, ref)
                    }else {
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLogin(false, productCardOptionsModel.screenName, ref)
                    }
                    showMessageSuccessRemoveWishlist()
                }
                updateWishlist(wishlistResult.isAddWishlist, productCardOptionsModel.productPosition)
            } else {
                showMessageFailedWishlistAction()
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    private fun updateWishlist(isWishlist: Boolean, position: Int) {
        if(position > -1 && adapter.itemCount > 0 &&
                adapter.itemCount > position) {
            (adapter.data[position] as RecommendationItemDataModel).productItem.isWishlist = isWishlist
            adapter.notifyItemChanged(position, isWishlist)
        }
    }

    private fun showMessageSuccessAddWishlist() {
        showToastSuccessWithAction(
                getString(R.string.recom_msg_success_add_wishlist),
                getString(R.string.home_recom_go_to_wishlist)
        ){
            View.OnClickListener { goToWishlist() }
        }
    }

    private fun goToWishlist() {
        if (activity == null) return
        RouteManager.route(activity, ApplinkConst.NEW_WISHLIST)
    }

    private fun showMessageSuccessRemoveWishlist() {
        showToastSuccess(getString(R.string.recom_msg_success_remove_wishlist))
    }

    private fun showMessageFailedWishlistAction() {
        showToastError()
    }

    /**
     * Void [observeAtcLiveData]
     * This void handle observe changes list data model from viewmodel
     */
    private fun observeLiveData(){
        recommendationWidgetViewModel.recommendationListLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { response ->
                clearAllData()
                renderList(response)
                hideLoading()
                getRecyclerView(view)?.smoothScrollToPosition(0)
                (response.firstOrNull() as? ProductInfoDataModel)?.productDetailData?.let { productDetailData ->
                    menu?.findItem(R.id.action_share)?.isVisible = true
                    menu?.findItem(R.id.action_share)?.setOnMenuItemClickListener {
                        shareProduct(productDetailData.id.toString(), productDetailData.name, productDetailData.name, productDetailData.imageUrl)
                        true
                    }
                }
            }
        })
    }

    /**
     * Void [observeAtcLiveData]
     * This void handle observe changes status atc from view model
     */
    private fun observeAtcLiveData(){
        recommendationWidgetViewModel.addToCartLiveData.observe(viewLifecycleOwner, Observer { response ->
            when(response.status){
                Status.SUCCESS -> {
                    response.data?.let {
                        RecommendationPageTracking.eventUserClickAddToCartWithProductId(it.mapToRecommendationTracking(), ref, internalRef)
                        activity?.run {
                            showToastSuccessWithAction(getString(R.string.recom_msg_success_add_to_cart), getString(R.string.recom_see_cart)){
                                RecommendationPageTracking.eventUserClickSeeToCartWithProductId()
                                RouteManager.route(context, ApplinkConst.CART)
                            }
                        }

                    }
                }
                else -> {
                    add_to_cart?.isEnabled = true
                    activity?.run {
                        showToastError(response.exception)
                    }
                }
            }
        })
    }

    /**
     * Void [observeBuyNowLiveData]
     * This void handle observe changes status buy now from view model
     */
    private fun observeBuyNowLiveData(){
        recommendationWidgetViewModel.buyNowLiveData.observe(viewLifecycleOwner, Observer { response ->
            when(response.status){
                Status.SUCCESS -> {
                    response.data?.let {
                        RecommendationPageTracking.eventUserClickBuyWithProductId(it.mapToRecommendationTracking(), ref, internalRef)
                        RouteManager.route(context, ApplinkConst.CART)
                    }
                }
                else -> {
                    buy_now?.isEnabled = true
                    activity?.run {
                        showToastError(response.exception)
                    }
                }
            }
        })
    }

    /**
     * This void from Callback [RecommendationErrorListener]
     * it handle global error click from [RecommendationErrorViewHolder]
     */
    override fun onRefreshRecommendation() {
        loadInitialData()
    }

    /**
     * This void from Callback [RecommendationErrorListener]
     * it handle global error click from [RecommendationErrorViewHolder]
     */
    override fun onCloseRecommendation() {
        this.activity?.finish()
    }

    /**
     * This void from Callback [RecommendationListener]
     * It handling wishlist click from item
     * @param item the item clicked
     * @param isAddWishlist the wishlist is selected or not
     * @param callback the callback for notify when success or not, there are have 2 params [Boolean] and [Throwable]
     */
    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        if(recommendationWidgetViewModel.isLoggedIn()){
            if (isAddWishlist) {
                recommendationWidgetViewModel.addWishlist(item.productId.toString(), item.wishlistUrl, item.isTopAds, callback)
                if(productId.isNotBlank() || productId.isNotEmpty()){
                    RecommendationPageTracking.eventUserClickRecommendationWishlistForLoginWithProductId(true, ref)
                }else {
                    RecommendationPageTracking.eventUserClickRecommendationWishlistForLogin(true, getHeaderName(item), ref)
                }
            } else {
                recommendationWidgetViewModel.removeWishlist(item.productId.toString(), callback)
                if(productId.isNotBlank() || productId.isNotEmpty()){
                    RecommendationPageTracking.eventUserClickRecommendationWishlistForLoginWithProductId(false, ref)
                }else {
                    RecommendationPageTracking.eventUserClickRecommendationWishlistForLogin(false, getHeaderName(item), ref)
                }
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
            if (productId.isNotBlank() || productId.isNotEmpty()) {
               RecommendationPageTracking.eventUserClickRecommendationWishlistForNonLoginWithProductId(ref)
            } else {
               RecommendationPageTracking.eventUserClickRecommendationWishlistForNonLogin(getHeaderName(item), ref)
            }
        }
    }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        showProductCardOptions(
                this,
                createProductCardOptionsModel(item, position[0]))
    }

    override fun onProductAnchorImpression(productInfoDataModel: ProductInfoDataModel) {
        productInfoDataModel.productDetailData?.let { productDetailData ->
            if (productDetailData.isTopads) {
                TopAdsUrlHitter(context).hitImpressionUrl(
                        className,
                        productDetailData.trackerImageUrl,
                        productDetailData.id.toString(),
                        productDetailData.name,
                        productDetailData.imageUrl)
            }
        }
        RecommendationPageTracking.eventImpressionPrimaryProductWithProductId(productInfoDataModel.mapToRecommendationTracking(), "0", ref, internalRef)
    }

    override fun onProductAnchorClick(productInfoDataModel: ProductInfoDataModel) {
        productInfoDataModel.productDetailData?.let { productDetailData ->
            if (productDetailData.isTopads) {
                TopAdsUrlHitter(context).hitClickUrl(
                        className,
                        productDetailData.clickUrl,
                        productDetailData.id.toString(),
                        productDetailData.name,
                        productDetailData.imageUrl)
            }
        }
        RecommendationPageTracking.eventClickPrimaryProductWithProductId(productInfoDataModel.mapToRecommendationTracking(), "0", ref, internalRef)
        goToPDP(productId, 0)
    }

    override fun onProductAnchorAddToCart(productInfoDataModel: ProductInfoDataModel) {
        productInfoDataModel.productDetailData?.let {productDetailData ->
            if (recommendationWidgetViewModel.isLoggedIn()) {
                if (productDetailData.isTopads) {
                    TopAdsUrlHitter(context).hitClickUrl(
                            className,
                            productDetailData.clickUrl,
                            productDetailData.id.toString(),
                            productDetailData.name,
                            productDetailData.imageUrl)
                }
                recommendationWidgetViewModel.onAddToCart(productInfoDataModel)
            } else {
                RecommendationPageTracking.eventUserAddToCartNonLoginWithProductId(ref, productDetailData.shop.id.toString())
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
            }
        }
    }

    override fun onProductAnchorBuyNow(productInfoDataModel: ProductInfoDataModel) {
        productInfoDataModel.productDetailData?.let { productDetailData ->
            if (recommendationWidgetViewModel.isLoggedIn()){
                if (productDetailData.isTopads) {
                    TopAdsUrlHitter(context).hitClickUrl(
                            className,
                            productDetailData.clickUrl,
                            productDetailData.id.toString(),
                            productDetailData.name,
                            productDetailData.imageUrl)
                }
                recommendationWidgetViewModel.onBuyNow(productInfoDataModel)
            } else {
                RecommendationPageTracking.eventUserClickBuyNonLoginWithProductId(ref, productDetailData.shop.id.toString())
                context?.let {
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
                }
            }
        }
    }

    override fun onProductAnchorClickWishlist(productInfoDataModel: ProductInfoDataModel, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        productInfoDataModel.productDetailData?.let {productDetailData ->
            if (recommendationWidgetViewModel.isLoggedIn()) {
                if (productDetailData.isTopads) {
                    TopAdsUrlHitter(context).hitClickUrl(
                            className,
                            productDetailData.clickUrl,
                            productDetailData.id.toString(),
                            productDetailData.name,
                            productDetailData.imageUrl)
                }
                RecommendationPageTracking.eventUserClickProductToWishlistForUserLoginWithProductId(isAddWishlist, ref, productDetailData.shop.id.toString())
                if (!isAddWishlist) {
                    recommendationWidgetViewModel.removeWishlist(productDetailData.id.toString()){ state, throwable ->
                        callback(state, throwable)
                        if(state){
                            showToastSuccess(getString(com.tokopedia.wishlist.common.R.string.msg_success_remove_wishlist))
                        } else {
                            showToastError(MessageErrorException())
                        }
                    }
                } else {
                    recommendationWidgetViewModel.addWishlist(productDetailData.id.toString(), productDetailData.wishlistUrl, productDetailData.isTopads){ state, throwable ->
                        callback(state, throwable)
                        if(state){
                            showToastSuccess(getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist))
                        } else {
                            showToastError(MessageErrorException())
                        }
                    }
                }
            } else {
                RecommendationPageTracking.eventUserClickProductToWishlistForNonLoginWithProductId(ref, productDetailData.shop.id.toString())
                RouteManager.route(activity, ApplinkConst.LOGIN)
            }
        }
    }

    /**
     * This void from Callback [RecommendationListener]
     * It handling product impression item
     * @param item the item clicked
     */
    override fun onProductImpression(item: RecommendationItem) {
        trackingQueue?.let { trackingQueue->
            if(recommendationWidgetViewModel.isLoggedIn()){
                if(productId.isNotBlank() || productId.isNotEmpty()){
                    RecommendationPageTracking.eventImpressionProductRecommendationOnHeaderNameLoginWithProductId(trackingQueue, getHeaderName(item), item, item.position.toString(), ref, internalRef)
                }else {
                    RecommendationPageTracking.eventImpressionProductRecommendationOnHeaderNameLogin(trackingQueue, getHeaderName(item), item, item.position.toString(), ref, internalRef)
                }
            } else {
                if(productId.isNotBlank() || productId.isNotEmpty()){
                    RecommendationPageTracking.eventImpressionProductRecommendationOnHeaderNameWithProductId(trackingQueue, getHeaderName(item), item, item.position.toString(), ref, internalRef)
                }else {
                    RecommendationPageTracking.eventImpressionProductRecommendationOnHeaderName(trackingQueue, getHeaderName(item), item, item.position.toString(), ref, internalRef)
                }
            }
        }
    }

    /**
     * This void from Callback [RecommendationListener]
     * It handling item click
     * @param item the item clicked
     * @param layoutType the layoutType is type layout where item placed
     * @param position list of position of the item at Adapter, can be [1] or [1,2] for dynamic nested item
     */
    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        eventTrackerClickListener(item)
        lastClickLayoutType = layoutType
        if(position.size > 1){
            lastParentPosition = position[0]
            goToPDP(item.productId.toString(), position[1])
        }else {
            goToPDP(item.productId.toString(), position[0])
        }
    }

    /**
     * This void from Callback [TitleListener]
     * It handling item see more click
     * @param pageName the page name clicked item
     * @param seeMoreAppLink the applink item clicked
     */
    override fun onClickSeeMore(pageName: String, seeMoreAppLink: String) {
        RecommendationPageTracking.eventUserClickSeeMore(pageName, productId)
        RouteManager.route(this.context, seeMoreAppLink)
    }

    /**
     * Void [loadData]
     * It handling trigger load primaryProduct and recommendationList from viewModel
     */
    private fun loadData(){
        activity?.let{
            recommendationWidgetViewModel.getRecommendationList(productId,
                    queryParam)
        }
    }

    /**
     * Void [eventTrackerClickListener]
     * It handling tracker event from click Product
     * @param item the recommendation item product
     */
    private fun eventTrackerClickListener(item: RecommendationItem){
        if(recommendationWidgetViewModel.isLoggedIn()){
            if(productId.isNotBlank() || productId.isNotEmpty()){
                RecommendationPageTracking.eventUserClickOnHeaderNameProductWithProductId(getHeaderName(item), item, item.position.toString(), ref, internalRef)
            }else {
                RecommendationPageTracking.eventUserClickOnHeaderNameProduct(getHeaderName(item), item, item.position.toString(), ref, internalRef)
            }
        }else{
            if(productId.isNotBlank() || productId.isNotEmpty()){
                RecommendationPageTracking.eventUserClickOnHeaderNameProductNonLoginWithProductId(getHeaderName(item), item, item.position.toString(), ref, internalRef)
            }else {
                RecommendationPageTracking.eventUserClickOnHeaderNameProductNonLogin(getHeaderName(item), item, item.position.toString(), ref, internalRef)
            }
        }
    }

    /**
     * Void [goToPDP]
     * It handling routing to PDP
     * @param productId the recommendation item
     * @param position the position of the item at adapter
     */
    private fun goToPDP(productId: String, position: Int){
        try {
            RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId).run {
                putExtra(PDP_EXTRA_UPDATED_POSITION, position)
                startActivityForResult(this, REQUEST_FROM_PDP)
            }
        }catch (e: Exception){

        }
    }

    /**
     * Function [getHeaderName]
     * It handling routing to PDP
     * @param item the recommendation item
     * @return header name
     */
    private fun getHeaderName(item: RecommendationItem): String{
        if(item.header.isNotBlank()) return item.header
        return ""
    }


    /**
     * Void [shareProduct]
     * It handling show share intent
     * @param id product id
     * @param name product name
     * @param description product description
     * @param imageUrl product image url
     */
    private fun shareProduct(id: String, name: String, description: String, imageUrl: String){
        context?.let{ context ->
            if(productId.isNotBlank() || productId.isNotEmpty()){
                RecommendationPageTracking.eventClickIconShareWithProductId()
            }else {
                RecommendationPageTracking.eventClickIconShare()
            }
            LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                    productDataToLinkerDataMapper(id, name, description, imageUrl), object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult) {
                    if(linkerShareData.url != null) {
                        openIntentShare(name, context.getString(R.string.recom_home_recommendation), linkerShareData.url)
                    }
                }

                override fun onError(linkerError: LinkerError) {
                    openIntentShare(name, context.getString(R.string.recom_home_recommendation), String.format(RECOMMENDATION_APP_LINK, "$id?${queryParam}"))
                }
            }))
        }
    }

    /**
     * Function [productDataToLinkerDataMapper]
     * It handling routing to PDP
     * @param id product id
     * @param name product name
     * @param description product description
     * @param imageUrl product image url
     * @return LinkerShareData for the requirement share intent
     */
    private fun productDataToLinkerDataMapper(
            id: String, name: String, description: String, imageUrl: String
    ): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = id
        linkerData.name = name
        linkerData.description = description
        linkerData.imgUri = imageUrl
        linkerData.ogUrl = null
        linkerData.type = "Recommendation"
        linkerData.uri =  "https://m.tokopedia.com/rekomendasi/$id?$queryParam"
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    /**
     * Void [openIntentShare]
     * It handling show intent share
     * @param title the title of intent share
     * @param shareContent the content of intent share
     * @param shareUri the uri of intent share
     */
    fun openIntentShare(title: String, shareContent: String, shareUri: String){
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_REFERRER, shareUri)
            putExtra(Intent.EXTRA_HTML_TEXT, "$shareContent $shareUri")
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, "$shareContent $shareUri")
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        activity?.startActivity(Intent.createChooser(shareIntent, SHARE_PRODUCT_TITLE))
    }

    private fun createProductCardOptionsModel(recommendationItem: RecommendationItem, position: Int): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = recommendationItem.isWishlist
        productCardOptionsModel.productId = recommendationItem.productId.toString()
        productCardOptionsModel.isTopAds = recommendationItem.isTopAds
        productCardOptionsModel.topAdsWishlistUrl = recommendationItem.wishlistUrl
        productCardOptionsModel.productPosition = position
        productCardOptionsModel.screenName = recommendationItem.header
        return productCardOptionsModel
    }

}
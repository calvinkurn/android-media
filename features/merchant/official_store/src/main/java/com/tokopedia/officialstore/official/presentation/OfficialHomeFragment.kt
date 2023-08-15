package com.tokopedia.officialstore.official.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.convertToLocationParams
import com.tokopedia.navigation_common.listener.OfficialStorePerformanceMonitoringListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.officialstore.FirebasePerformanceMonitoringConstant
import com.tokopedia.officialstore.OSPerformanceConstant
import com.tokopedia.officialstore.OfficialStoreInstance
import com.tokopedia.officialstore.analytics.OSFeaturedShopTracking
import com.tokopedia.officialstore.ApplinkConstant.CLICK_TYPE_WISHLIST
import com.tokopedia.officialstore.OSPerformanceConstant.KEY_PERFORMANCE_PREPARING_OS_HOME
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.category.presentation.data.OSChooseAddressData
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.common.listener.RecyclerViewScrollListener
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Cta
import com.tokopedia.officialstore.official.di.DaggerOfficialStoreHomeComponent
import com.tokopedia.officialstore.official.di.OfficialStoreHomeComponent
import com.tokopedia.officialstore.official.di.OfficialStoreHomeModule
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBannerDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialFeaturedShopDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBenefitDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingMoreDataModel
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler
import com.tokopedia.officialstore.official.presentation.listener.RecommendationWidgetCallback
import com.tokopedia.officialstore.official.presentation.listener.OfficialStoreHomeComponentCallback
import com.tokopedia.officialstore.official.presentation.listener.OfficialStoreLegoBannerComponentCallback
import com.tokopedia.officialstore.official.presentation.listener.OSMixLeftComponentCallback
import com.tokopedia.officialstore.official.presentation.listener.OSMixTopComponentCallback
import com.tokopedia.officialstore.official.presentation.listener.OSFeaturedBrandCallback
import com.tokopedia.officialstore.official.presentation.listener.OSFeaturedShopDCCallback
import com.tokopedia.officialstore.official.presentation.listener.OSMerchantVoucherCallback
import com.tokopedia.officialstore.official.presentation.listener.OSSpecialReleaseComponentCallback
import com.tokopedia.officialstore.official.presentation.viewmodel.OfficialStoreHomeViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TOASTER_RED
import javax.inject.Inject
import com.tokopedia.wishlist_common.R as Rwishlist

class OfficialHomeFragment :
        BaseDaggerFragment(),
        HasComponent<OfficialStoreHomeComponent>,
        RecommendationListener,
        FeaturedShopListener,
        DynamicChannelEventHandler{

    companion object {
        const val PRODUCT_RECOMM_GRID_SPAN_COUNT = 2
        const val BUNDLE_CATEGORY = "category_os"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 898
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val SLUG_CONST = "{slug}"
        private const val POS_1 = 1
        private const val POS_10 = 10
        private const val DELAY_200L = 200L

        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeFragment().apply { arguments = bundle }
    }

    private var officialStorePerformanceMonitoringListener: OfficialStorePerformanceMonitoringListener? = null
    private val sentDynamicChannelTrackers = mutableSetOf<String>()

    @Inject
    lateinit var viewModel: OfficialStoreHomeViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var tracking: OfficialStoreTracking? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: StaggeredGridLayoutManager? = null
    private var category: Category? = null
    private var adapter: OfficialHomeAdapter? = null
    private var lastClickLayoutType: String? = null
    private var lastParentPosition: Int? = null
    private var isLoadedOnce: Boolean = false
    private var isScrolling = false
    private var localChooseAddress: OSChooseAddressData? = null
    private var recommendationWishlistItem: RecommendationItem? = null
    private var totalScrollRecyclerView = 0

    private lateinit var bannerPerformanceMonitoring: PerformanceMonitoring
    private lateinit var shopPerformanceMonitoring: PerformanceMonitoring
    private lateinit var dynamicChannelPerformanceMonitoring: PerformanceMonitoring
    private lateinit var productRecommendationPerformanceMonitoring: PerformanceMonitoring

    private val endlessScrollListener: EndlessRecyclerViewScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (swipeRefreshLayout?.isRefreshing == false) {
                    val categoryConst: String = category?.slug.orEmpty()
                    val recomConstant = (FirebasePerformanceMonitoringConstant.PRODUCT_RECOM).replace(SLUG_CONST, categoryConst)
                    val categories = category?.categories.toString()
                    val categoriesWithoutOpeningSquare = categories.replace("[", "") // Remove Square bracket from the string
                    val categoriesWithoutClosingSquare = categoriesWithoutOpeningSquare.replace("]", "") // Remove Square bracket from the string
                    viewModel.counterTitleShouldBeRendered += 1
                    productRecommendationPerformanceMonitoring = PerformanceMonitoring.start(recomConstant)
                    viewModel.loadMoreProducts(categoriesWithoutClosingSquare, page)

                    viewModel.addLoadingMore()

                }
            }
        }
    }

    fun forceLoadData() {
        reloadDataForDifferentAddressSaved()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        officialStorePerformanceMonitoringListener = context?.let { castContextToOfficialStorePerformanceMonitoring(it) }
        if (savedInstanceState == null) {
            officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.startCustomMetric(
                KEY_PERFORMANCE_PREPARING_OS_HOME)
        }
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(BUNDLE_CATEGORY)
        }
        context?.let { tracking = OfficialStoreTracking(it) }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (isChooseAddressUpdated() && isLoadedOnce) {
                reloadDataForDifferentAddressSaved()
            } else {
                loadData()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_official_home_child, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.os_child_recycler_view)
        layoutManager = StaggeredGridLayoutManager(PRODUCT_RECOMM_GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        recyclerView?.layoutManager = layoutManager

        val adapterTypeFactory = OfficialHomeAdapterTypeFactory(
                this,
                this,
                RecommendationWidgetCallback(this, this.context, userSession.userId),
                OfficialStoreHomeComponentCallback(),
                OfficialStoreLegoBannerComponentCallback(this),
                OSMixLeftComponentCallback(this),
                OSMixTopComponentCallback(this),
                OSFeaturedShopDCCallback(this),
                recyclerView?.recycledViewPool,
                OSMerchantVoucherCallback(this),
                OSSpecialReleaseComponentCallback(this, userSession.userId),
                onTopAdsHeadlineClicked,
                this
        )
        adapter = OfficialHomeAdapter(adapterTypeFactory)
        recyclerView?.itemAnimator = null
        recyclerView?.adapter = adapter
        viewModel.resetState()
        return view
    }

    private val onTopAdsHeadlineClicked: (applink: String) -> Unit = {
        RouteManager.route(context, it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeOfficialStoreList()
        observeError()
        observeRecomUpdated()
        initLocalChooseAddressData()
        resetData()
        loadData()
        setListener()
        getOfficialStorePageLoadTimeCallback()?.stopPreparePagePerformanceMonitoring()
        if (savedInstanceState == null) officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.stopCustomMetric(
            KEY_PERFORMANCE_PREPARING_OS_HOME)

    }

    override fun onPause() {
        super.onPause()
        tracking?.sendAll()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): OfficialStoreHomeComponent? {
        return activity?.run {
            DaggerOfficialStoreHomeComponent
                    .builder()
                    .officialStoreHomeModule(OfficialStoreHomeModule())
                    .officialStoreComponent(OfficialStoreInstance.getComponent(application))
                    .build()
        }
    }

    override fun onDestroy() {
        viewModel.officialStoreLiveData.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST, false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                updateWishlist(wishlistStatusFromPdp, position)
            }
            lastClickLayoutType = null
            lastParentPosition = null
        }

        handleProductCardOptionsActivityResult(
                requestCode, resultCode, data, object : ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                handleWishlistAction(productCardOptionsModel)
            }
        }
        )
    }

    private fun observeOfficialStoreList(){
        viewModel.officialStoreLiveData.observe(viewLifecycleOwner){ dataModel ->
            removeLoading(dataModel.isCache)
            swipeRefreshLayout?.isRefreshing = false
            adapter?.submitList(dataModel.dataList.toList())
            if(dataModel.dataList.any { it is OfficialBannerDataModel }){
                bannerPerformanceMonitoring.stopTrace()
            }
            if(dataModel.dataList.any { it is OfficialFeaturedShopDataModel }){
                shopPerformanceMonitoring.stopTrace()
            }
            if(dataModel.dataList.any {
                it !is OfficialBannerDataModel &&
                it !is OfficialFeaturedShopDataModel &&
                it !is OfficialBenefitDataModel &&
                it !is OfficialLoadingDataModel &&
                it !is OfficialLoadingMoreDataModel
            }){
                dynamicChannelPerformanceMonitoring.stopTrace()
            }
        }
    }

    private fun observeError() {
        viewModel.officialStoreError.observe(viewLifecycleOwner){
            swipeRefreshLayout?.isRefreshing = false
            showErrorNetwork(it)
            bannerPerformanceMonitoring.stopTrace()
            dynamicChannelPerformanceMonitoring.stopTrace()
            shopPerformanceMonitoring.stopTrace()
        }
    }

    private fun observeRecomUpdated(){
        viewModel.recomUpdated.observe(viewLifecycleOwner){
            val data = it.getContentIfNotHandled()
            data?.let {
                endlessScrollListener.updateStateAfterGetData()
                productRecommendationPerformanceMonitoring.stopTrace()
            }
        }
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        lastClickLayoutType = layoutType
        if (position.size > 1) {
            lastParentPosition = position[0]
            goToPDP(item, position[1])
        } else {
            goToPDP(item, position[0])
        }
    }

    private fun updateWishListRecomWidget(isWishlist: Boolean) {
        recommendationWishlistItem?.isWishlist = isWishlist
    }

    private fun updateWishlist(isWishlist: Boolean, position: Int) {
        updateWishListRecomWidget(isWishlist)
        if (position > -1 && adapter != null) {
            viewModel.updateWishlist(isWishlist, position)
        }
    }

    private fun copyCoupon(view: View, cta: Cta) {
        val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(getString(R.string.os_coupon_code_label), cta.couponCode)
        clipboard.setPrimaryClip(clipData)
        Toaster.build(view.parent as ViewGroup,
                getString(R.string.os_toaster_coupon_copied),
                Snackbar.LENGTH_LONG).show()
    }

    private fun getLocation(): String {
        return ChooseAddressUtils.getLocalizingAddressData(requireContext())?.convertToLocationParams() ?: ""
    }

    override fun onProductImpression(item: RecommendationItem) {
        tracking?.eventImpressionProductRecommendation(
                item,
                isLogin(),
                category?.title.toString(),
                viewModel.productRecommendationTitleSection,
                item.position.toString()
        )
    }

    override fun onWishlistV2Click(item: RecommendationItem, isAddWishlist: Boolean) {
        if (isLogin()) {
            if (isAddWishlist) {
                viewModel.addWishlistV2(item, object: WishlistV2ActionListener{
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
                        view?.let {
                            AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMessage, it)
                        }
                    }

                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {
                        context?.let { context ->
                            view?.let { v ->
                                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(result, context, v)
                            }
                        }
                        if (item.isTopAds) {
                            hitWishlistTopadsClickUrl(item)
                        }
                    }

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) { }
                    override fun onSuccessRemoveWishlist(result: DeleteWishlistV2Response.Data.WishlistRemoveV2, productId: String) { }

                })
            } else {
                viewModel.removeWishlistV2(item, object: WishlistV2ActionListener{
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) { }
                    override fun onSuccessAddWishlist(result: AddToWishlistV2Response.Data.WishlistAddV2, productId: String) { }

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
                        view?.let {
                            AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMessage, it)
                        }
                    }

                    override fun onSuccessRemoveWishlist(
                        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                        productId: String
                    ) {
                        context?.let { context ->
                            view?.let { v ->
                                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(result, context, v)
                            }
                        }
                    }

                })
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }

        tracking?.eventClickWishlist(
            category?.title.toEmptyStringIfNull(),
            isAddWishlist,
            isLogin(),
            item.productId,
            item.isTopAds
        )
    }

    private fun hitWishlistTopadsClickUrl(item: RecommendationItem) {
        context?.let {
            TopAdsUrlHitter(it).hitClickUrl(
                this::class.java.simpleName,
                item.clickUrl+CLICK_TYPE_WISHLIST,
                item.productId.toString(),
                item.name,
                item.imageUrl
            )
        }
    }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        recommendationWishlistItem = item
        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        productId = item.productId.toString(),
                        isWishlisted = item.isWishlist,
                        isTopAds = item.isTopAds,
                        topAdsWishlistUrl = item.wishlistUrl,
                        productPosition = position.getOrElse(0) { 0 }
                )
        )
    }

    override fun onCountDownFinished() {
        viewModel.removeFlashSale()
        loadData(true)
    }

    override fun onClickLegoHeaderActionText(applink: String) {
        RouteManager.route(context, applink)
    }

    override fun onClickLegoImage(channelModel: ChannelModel, position: Int) {
        val gridData = channelModel.channelGrids[position]
        val applink = gridData.applink

        gridData.let {
            tracking?.clickLego36Image(
                    viewModel.currentSlugDC,
                    channelModel.channelHeader.name,
                    (position + POS_1).toString(POS_10),
                    it,
                    channelModel,
                    getUserId()
            )
        }

        RouteManager.route(context, applink)
    }

    override fun legoImpression(channelModel: ChannelModel) {
        if (!sentDynamicChannelTrackers.contains(channelModel.id)) {
            tracking?.impressionLego36Image(viewModel.currentSlugDC, channelModel, getUserId())
            sentDynamicChannelTrackers.add(channelModel.id)
        }
    }

    override fun onClickFlashSaleActionText(applink: String, channelId: String, headerName: String): View.OnClickListener {
        return View.OnClickListener {
            tracking?.flashSaleClickViewAll(viewModel.currentSlugDC, channelId, headerName)
            RouteManager.route(context, applink)
        }
    }

    override fun onClickFlashSaleImage(channelData: Channel, position: Int): View.OnClickListener {
        return View.OnClickListener {
            channelData.grids.getOrNull(position)?.let { gridData ->
                val applink = gridData.applink
                tracking?.flashSalePDPClick(
                        viewModel.currentSlugDC,
                        channelData.header?.name ?: "",
                        (position + POS_1).toString(POS_10),
                        gridData,
                        channelData.id,
                        getUserId()
                )
                RouteManager.route(context, applink)
            }

        }
    }

    override fun flashSaleImpression(channelData: Channel) {
        if (!sentDynamicChannelTrackers.contains(channelData.id)) {
            tracking?.flashSaleImpression(viewModel.currentSlugDC, channelData, getUserId())
            sentDynamicChannelTrackers.add(channelData.id)
        }
    }

    override fun onClickMixTopBannerItem(applink: String) {
        RouteManager.route(context, applink)
    }

    override fun onClickMixTopBannerCtaButton(cta: Cta, channelId: String, applink: String, headerName: String, channelBannerAttribution: String) {
        tracking?.mixTopBannerCtaButtonClicked(
                viewModel.currentSlugDC,
                cta.text,
                channelId,
                headerName,
                channelBannerAttribution
        )
        if (cta.couponCode.isEmpty()) {
            RouteManager.route(context, applink)
        } else {
            view?.let {
                copyCoupon(it, cta)
            }
        }
    }

    override fun onClickMixLeftBannerImage(channel: ChannelModel, position: Int) {
        tracking?.eventClickMixLeftImageBanner(channel, category?.title.orEmpty(), position, getUserId())
        RouteManager.route(context, channel.channelBanner.applink)
    }

    override fun onMixLeftBannerImpressed(channel: ChannelModel, position: Int) {
        tracking?.eventImpressionMixLeftImageBanner(channel, category?.title.orEmpty(), position, getUserId())
    }

    override fun onProductCardImpressed(position: Int, grid: ChannelGrid, channel: ChannelModel) {
        tracking?.carouselProductCardImpression(
                viewModel.currentSlugDC,
                channel,
                grid,
                position.toString(),
                getUserId()
        )
    }

    override fun onCarouselSeeAllCardClicked(channel: ChannelModel, applink: String) {
        tracking?.carouselViewAllCardClicked(
                viewModel.currentSlugDC,
                channel
        )
        if (!TextUtils.isEmpty(applink)) {
            RouteManager.route(context, applink)
        }
    }

    override fun getOSCategory(): Category? {
        return category
    }

    override fun isLogin(): Boolean {
        return userSession.isLoggedIn
    }

    override fun getUserId(): String {
        return userSession.userId
    }

    override fun getTrackingObject(): OfficialStoreTracking? {
        return tracking
    }

    override fun onCarouselSeeAllHeaderClicked(channel: ChannelModel, applink: String) {
        tracking?.carouselHeaderSeeAllClick(
                viewModel.currentSlugDC,
                channel
        )
        if (!TextUtils.isEmpty(applink)) {
            RouteManager.route(context, applink)
        }
    }

    override fun onProductCardClicked(position: Int, channel: ChannelModel, grid: ChannelGrid, applink: String) {
        tracking?.carouselProductCardClicked(
                viewModel.currentSlugDC,
                channel,
                grid,
                position.toString(),
                getUserId()
        )
        RouteManager.route(context, applink)
    }

    override fun onShopImpression(categoryName: String, position: Int, shopData: Shop) {
        tracking?.eventImpressionShop(
                categoryName = categoryName,
                shopPosition = position,
                shopName = shopData.name.orEmpty(),
                url = shopData.imageUrl.orEmpty(),
                additionalInformation = shopData.additionalInformation.orEmpty(),
                featuredBrandId = shopData.featuredBrandId.orEmpty(),
                isLogin = isLogin(),
                shopId = shopData.shopId.orEmpty()
        )
    }

    override fun onShopClick(categoryName: String, position: Int, shopData: Shop) {
        tracking?.eventClickShop(
                categoryName = categoryName,
                shopPosition = position,
                shopName = shopData.name.orEmpty(),
                url = shopData.url.orEmpty(),
                additionalInformation = shopData.additionalInformation.orEmpty(),
                featuredBrandId = shopData.featuredBrandId.orEmpty(),
                isLogin = isLogin(),
                shopId = shopData.shopId.orEmpty(),
                campaignCode = shopData.campaignCode.orEmpty()
        )
        RouteManager.route(context, shopData.url)
    }

    override fun onFeaturedShopDCClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        OSFeaturedShopTracking.getEventClickShopWidget(
            channel = channelModel,
            grid = channelGrid,
            categoryName = category?.title?:"",
            bannerPosition = position,
            userId = userSession.userId
        )
        goToApplink(channelGrid.applink)
    }

    override fun onFeaturedShopDCImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        tracking?.trackingQueueObj?.putEETracking(
                OSFeaturedShopTracking.getEventImpressionShopWidget(
                        channel = channelModel,
                        grid = channelGrid,
                        categoryName = category?.title?:"",
                        bannerPosition = position,
                        userId = userSession.userId
                )
        )

        viewModel.recordShopWidgetImpression(channelModel.id, channelGrid.id)
    }

    override fun onSeeAllFeaturedShopDCClicked(channel: ChannelModel, position: Int, applink: String) {
        goToApplink(applink)
    }

    override fun goToApplink(applink: String) {
        RouteManager.route(requireContext(), applink)
    }

    private fun initFirebasePerformanceMonitoring() {
        val CATEGORY_CONST: String = category?.slug.orEmpty()

        val bannerConstant = (FirebasePerformanceMonitoringConstant.BANNER).replace(SLUG_CONST, CATEGORY_CONST)
        bannerPerformanceMonitoring = PerformanceMonitoring.start(bannerConstant)

        val brandConstant = (FirebasePerformanceMonitoringConstant.BRAND).replace(SLUG_CONST, CATEGORY_CONST)
        shopPerformanceMonitoring = PerformanceMonitoring.start(brandConstant)

        val dynamicChannelConstant = (FirebasePerformanceMonitoringConstant.DYNAMIC_CHANNEL).replace(SLUG_CONST, CATEGORY_CONST)
        dynamicChannelPerformanceMonitoring = PerformanceMonitoring.start(dynamicChannelConstant)
    }

    private fun removeLoading(isCache: Boolean?) {
        isCache?.let {
            val osPltCallback = getOfficialStorePageLoadTimeCallback()
            if (osPltCallback != null) {
                osPltCallback.stopNetworkRequestPerformanceMonitoring()
                osPltCallback.startRenderPerformanceMonitoring()
            }
            setPerformanceListenerForRecyclerView(isCache)
        }
    }

    private fun castContextToOfficialStorePerformanceMonitoring(context: Context): OfficialStorePerformanceMonitoringListener? {
        return if (context is OfficialStorePerformanceMonitoringListener) {
            context
        } else null
    }

    private fun getOfficialStorePageLoadTimeCallback(): PageLoadTimePerformanceInterface? {
        return officialStorePerformanceMonitoringListener?.officialStorePageLoadTimePerformanceInterface
    }

    private fun setPerformanceListenerForRecyclerView(isCache: Boolean) {
        recyclerView?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                officialStorePerformanceMonitoringListener?.stopOfficialStorePerformanceMonitoring(isCache)
                recyclerView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun resetData() {
        endlessScrollListener.resetState()
    }

    private fun loadData(isRefresh: Boolean = false) {
        initFirebasePerformanceMonitoring()

        if (userVisibleHint && isAdded && ::viewModel.isInitialized) {
            if (!isLoadedOnce || isRefresh) {
                viewModel.loadFirstData(category, getLocation(),
                        onBannerCacheStartLoad = {
                            officialStorePerformanceMonitoringListener?.officialStorePageLoadTimePerformanceInterface?.startCustomMetric(OSPerformanceConstant.KEY_PERFORMANCE_OS_HOME_BANNER_CACHE)
                        },
                        onBannerCacheStopLoad = {
                            officialStorePerformanceMonitoringListener?.officialStorePageLoadTimePerformanceInterface?.stopCustomMetric(OSPerformanceConstant.KEY_PERFORMANCE_OS_HOME_BANNER_CACHE)
                        },
                        onBannerCloudStartLoad = {
                            officialStorePerformanceMonitoringListener?.officialStorePageLoadTimePerformanceInterface?.startCustomMetric(OSPerformanceConstant.KEY_PERFORMANCE_OS_HOME_BANNER_CLOUD)
                        },
                        onBannerCloudStopLoad = {
                            officialStorePerformanceMonitoringListener?.officialStorePageLoadTimePerformanceInterface?.stopCustomMetric(OSPerformanceConstant.KEY_PERFORMANCE_OS_HOME_BANNER_CLOUD)
                        })
                isLoadedOnce = true

                getOfficialStorePageLoadTimeCallback()?.startNetworkRequestPerformanceMonitoring()

                if (!isRefresh) {
                    tracking?.sendScreen(category?.title.toEmptyStringIfNull())
                }
            }
        }
    }

    private fun reloadDataForDifferentAddressSaved() {
        localChooseAddress?.setLocalCacheModel(ChooseAddressUtils.getLocalizingAddressData(requireContext())?.copy())
        viewModel.resetState()
        viewModel.loadFirstData(category, getLocation())
    }

    private fun showErrorNetwork(t: Throwable) {
        view?.let {
            Toaster.build(it,
                    ErrorHandler.getErrorMessage(context, t),
                    Snackbar.LENGTH_LONG, TYPE_ERROR
            ).show()
        }
    }

    private fun setListener() {
        setLoadMoreListener()
        swipeRefreshLayout?.setOnRefreshListener {
            viewModel.counterTitleShouldBeRendered = 0
            loadData(true)
            viewModel.resetShopWidgetImpressionCount()
            viewModel.resetIsFeatureShopAllowed()
            resetData()
        }

        if (parentFragment is RecyclerViewScrollListener) {
            val scrollListener = parentFragment as RecyclerViewScrollListener
            layoutManager?.let {
                recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        totalScrollRecyclerView += dy
                        if (!isScrolling) {
                            isScrolling = true
                            scrollListener.onContentScrolled(dy, totalScrollRecyclerView)
                            Handler().postDelayed({
                                isScrolling = false
                            }, DELAY_200L)
                        }

                    }

                })
            }
        }

    }

    private fun setLoadMoreListener() {
        recyclerView?.addOnScrollListener(endlessScrollListener)
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        val wishlistResult = productCardOptionsModel.wishlistResult

        if (wishlistResult.isUserLoggedIn)
            handleWishlistActionForLoggedInUser(productCardOptionsModel)
        else
            RouteManager.route(context, ApplinkConst.LOGIN)

        tracking?.eventClickWishlist(
                category?.title.toEmptyStringIfNull(),
                !productCardOptionsModel.isWishlisted,
                isLogin(),
                productCardOptionsModel.productId.toLongOrZero(),
                productCardOptionsModel.isTopAds
        )
    }

    private fun handleWishlistActionForLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess)
            handleWishlistV2ActionSuccess(productCardOptionsModel)
        else
            showErrorWishlistV2(productCardOptionsModel.wishlistResult)
    }

    private fun handleWishlistV2ActionSuccess(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isAddWishlist)
            showSuccessAddWishlistV2(productCardOptionsModel.wishlistResult)
        else
            showSuccessRemoveWishlistV2()

        updateWishlist(productCardOptionsModel.wishlistResult.isAddWishlist, productCardOptionsModel.productPosition)
    }

    private fun showSuccessAddWishlistV2(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        val msg = wishlistResult.messageV2.ifEmpty {
            if (wishlistResult.isSuccess) getString(Rwishlist.string.on_success_add_to_wishlist_msg)
            else getString(Rwishlist.string.on_failed_add_to_wishlist_msg)
        }

        var typeToaster = TYPE_NORMAL
        if (wishlistResult.toasterColorV2 == TOASTER_RED || !wishlistResult.isSuccess) typeToaster = TYPE_ERROR

        var ctaText = getString(com.tokopedia.wishlist_common.R.string.cta_success_add_to_wishlist)
        if (wishlistResult.ctaTextV2.isNotEmpty()) ctaText = wishlistResult.ctaTextV2

        view?.let { v ->
            Toaster.build(v, msg, Toaster.LENGTH_SHORT, typeToaster, ctaText) {
                RouteManager.route(context, ApplinkConst.WISHLIST)
            }.show()
        }
    }

    private fun showSuccessRemoveWishlistV2() {
        activity?.let {
            val view = it.findViewById<View>(android.R.id.content) ?: return
            val message = getString(Rwishlist.string.on_success_remove_from_wishlist_msg)

            Toaster.build(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun showErrorWishlistV2(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        val errorMsg = wishlistResult.messageV2.ifEmpty {
            if (wishlistResult.isAddWishlist) getString(Rwishlist.string.on_failed_add_to_wishlist_msg)
            else getString(Rwishlist.string.on_failed_remove_from_wishlist_msg)
        }

        activity?.let {
            val view = it.findViewById<View>(android.R.id.content) ?: return
            Toaster.build(view, errorMsg, Toaster.LENGTH_SHORT, TYPE_ERROR).show()
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun goToPDP(item: RecommendationItem, position: Int) {
        eventTrackerClickListener(item, position)
        try {
            RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString()).run {
                putExtra(PDP_EXTRA_UPDATED_POSITION, position)
                startActivityForResult(this, REQUEST_FROM_PDP)
            }
        }catch (e: Exception){
        }
    }

    private fun eventTrackerClickListener(item: RecommendationItem, position: Int) {
        tracking?.eventClickProductRecommendation(
                item,
                position.toString(),
                viewModel.productRecommendationTitleSection,
                isLogin(),
                category?.title.toString()
        )
    }

    private fun initLocalChooseAddressData() {
        val addressData = ChooseAddressUtils.getLocalizingAddressData(requireContext())
        addressData?.let {
            localChooseAddress = OSChooseAddressData()
            localChooseAddress?.setLocalCacheModel(it.copy())
        }
    }

    override fun onBestSellerClick(appLink: String) {
        RouteManager.route(context, appLink)
    }

    override fun onBestSellerThreeDotsClick(recommendationItem: RecommendationItem,
        widgetPosition: Int) {
        recommendationWishlistItem = recommendationItem
        showProductCardOptions(
            this,
            recommendationItem.createProductCardOptionsModel(widgetPosition))
    }

    override fun onBestSellerSeeMoreTextClick(appLink: String) {
        RouteManager.route(context, appLink)
    }

    override fun onBestSellerSeeAllCardClick(appLink: String) {
        RouteManager.route(context, appLink)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun isChooseAddressUpdated(): Boolean {
        try {
            localChooseAddress?.let {
                return ChooseAddressUtils.isLocalizingAddressHasUpdated(requireContext(), it.toLocalCacheModel())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return false

    }

    private fun RecommendationItem.createProductCardOptionsModel(position: Int): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = isWishlist
        productCardOptionsModel.productId = productId.toString()
        productCardOptionsModel.isTopAds = isTopAds
        productCardOptionsModel.topAdsWishlistUrl = wishlistUrl
        productCardOptionsModel.productPosition = position
        productCardOptionsModel.screenName = header
        return productCardOptionsModel
    }
}

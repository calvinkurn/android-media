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
import com.tokopedia.officialstore.*
import com.tokopedia.officialstore.ApplinkConstant.CLICK_TYPE_WISHLIST
import com.tokopedia.officialstore.OSPerformanceConstant.KEY_PERFORMANCE_PREPARING_OS_HOME
import com.tokopedia.officialstore.analytics.OSMixLeftTracking
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.category.presentation.data.OSChooseAddressData
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.common.listener.RecyclerViewScrollListener
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Cta
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.officialstore.official.di.DaggerOfficialStoreHomeComponent
import com.tokopedia.officialstore.official.di.OfficialStoreHomeComponent
import com.tokopedia.officialstore.official.di.OfficialStoreHomeModule
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler
import com.tokopedia.officialstore.official.presentation.listener.*
import com.tokopedia.officialstore.official.presentation.viewmodel.OfficialStoreHomeViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TOASTER_RED
import com.tokopedia.wishlistcommon.util.WishlistV2RemoteConfigRollenceUtil
import com.tokopedia.wishlist_common.R as Rwishlist
import java.util.*
import javax.inject.Inject

class OfficialHomeFragment :
        BaseDaggerFragment(),
        HasComponent<OfficialStoreHomeComponent>,
        RecommendationListener,
        FeaturedShopListener,
        DynamicChannelEventHandler{

    companion object {
        const val PRODUCT_RECOMM_GRID_SPAN_COUNT = 2
        const val BUNDLE_CATEGORY = "category_os"
        var PRODUCT_RECOMMENDATION_TITLE_SECTION = ""
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 898
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val SLUG_CONST = "{slug}"
        private const val PERFORMANCE_OS_PAGE_NAME = "OS"
        private const val POS_1 = 1
        private const val POS_10 = 10
        private const val DELAY_200L = 200L

        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeFragment().apply { arguments = bundle }
    }

    private var currentBannerData: OfficialStoreBanners? = null
    private var officialStorePerformanceMonitoringListener: OfficialStorePerformanceMonitoringListener? = null
    private val sentDynamicChannelTrackers = mutableSetOf<String>()

    @Inject
    lateinit var viewModel: OfficialStoreHomeViewModel
    @Inject
    lateinit var officialHomeMapper: OfficialHomeMapper

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
    private var counterTitleShouldBeRendered = 0
    private var isLoadedOnce: Boolean = false
    private var isScrolling = false
    private var remoteConfig: RemoteConfig? = null
    private var localChooseAddress: OSChooseAddressData? = null
    private var recommendationWishlistItem: RecommendationItem? = null

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
                    counterTitleShouldBeRendered += 1
                    productRecommendationPerformanceMonitoring = PerformanceMonitoring.start(recomConstant)
                    viewModel.loadMoreProducts(categoriesWithoutClosingSquare, page)

                    officialHomeMapper.showLoadingMore(adapter)

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
        remoteConfig = FirebaseRemoteConfigImpl(activity)
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
                OSFeaturedBrandCallback(this, tracking),
                OSFeaturedShopDCCallback(this),
                recyclerView?.recycledViewPool,
                OSMerchantVoucherCallback(this),
                OSSpecialReleaseComponentCallback(this, userSession.userId),
                onTopAdsHeadlineClicked)
        adapter = OfficialHomeAdapter(adapterTypeFactory)
        recyclerView?.adapter = adapter
        officialHomeMapper.resetState(adapter)
        return view
    }

    private val onTopAdsHeadlineClicked: (applink: String) -> Unit = {
        RouteManager.route(context, it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeBannerData()
        observeBenefit()
        observeFeaturedShop()
        observeDynamicChannel()
        observeProductRecommendation()
        observeFeaturedShopSuccessDC()
        observeFeaturedShopRemoveDC()
        observeRecomWidget()
        initLocalChooseAddressData()
        resetData()
        loadData()
        setListener()
        getOfficialStorePageLoadTimeCallback()?.stopPreparePagePerformanceMonitoring()
        if (savedInstanceState == null) officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.stopCustomMetric(
            KEY_PERFORMANCE_PREPARING_OS_HOME)

    }

    private fun observeRecomWidget() {
        viewModel.recomWidget.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (!isEligibleForDisableBestSellerWidget()) {
                        officialHomeMapper.mappingRecomWidget(it.data) { newDataList ->
                            adapter?.submitList(newDataList)
                        }
                    }
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorNetwork(it.throwable)
                }

            }
        }
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
        viewModel.officialStoreBannersResult.removeObservers(this)
        viewModel.officialStoreBenefitsResult.removeObservers(this)
        viewModel.officialStoreDynamicChannelResult.removeObservers(this)
        viewModel.productRecommendation.removeObservers(this)
        viewModel.featuredShopRemove.removeObservers(this)
        viewModel.featuredShopResult.removeObservers(this)
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
            officialHomeMapper.updateWishlist(isWishlist, position, adapter)
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
                viewModel.isLoggedIn(),
                category?.title.toString(),
                PRODUCT_RECOMMENDATION_TITLE_SECTION,
                item.position.toString()
        )
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        if (viewModel.isLoggedIn()) {
            if (isAddWishlist) {
                viewModel.addWishlist(item, callback)
            } else {
                viewModel.removeWishlist(item, callback)
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }

        tracking?.eventClickWishlist(
                category?.title.toEmptyStringIfNull(),
                isAddWishlist,
                viewModel.isLoggedIn(),
                item.productId,
                item.isTopAds
        )
    }

    override fun onWishlistV2Click(item: RecommendationItem, isAddWishlist: Boolean) {
        if (viewModel.isLoggedIn()) {
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
            viewModel.isLoggedIn(),
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
        officialHomeMapper.removeFlashSale(adapter)
        loadData(true)
    }

    override fun onClickLegoHeaderActionText(applink: String) {
        RouteManager.route(context, applink)
    }

    override fun onClickLegoImage(channelModel: ChannelModel, position: Int) {
        val gridData = channelModel.channelGrids[position]
        val applink = gridData.applink

        gridData.let {
            tracking?.dynamicChannelHomeComponentClick(
                    viewModel.currentSlug,
                    channelModel.channelHeader.name,
                    (position + POS_1).toString(POS_10),
                    it,
                    channelModel
            )
        }

        RouteManager.route(context, applink)
    }

    override fun legoImpression(channelModel: ChannelModel) {
        if (!sentDynamicChannelTrackers.contains(channelModel.id)) {
            tracking?.dynamicChannelHomeComponentImpression(viewModel.currentSlug, channelModel)
            sentDynamicChannelTrackers.add(channelModel.id)
        }
    }

    override fun onClickLegoHeaderActionTextListener(applink: String): View.OnClickListener {
        return View.OnClickListener {
            RouteManager.route(context, applink)
        }
    }

    override fun onClickLegoImage(channelData: Channel, position: Int): View.OnClickListener {
        return View.OnClickListener {
            channelData.grids.getOrNull(position)?.let { gridData ->
                val applink = gridData.applink

                tracking?.dynamicChannelImageClick(
                        viewModel.currentSlug,
                        channelData.header?.name ?: "",
                        (position + POS_1).toString(POS_10),
                        gridData,
                        channelData
                )

                RouteManager.route(context, applink)
            }
        }
    }

    override fun legoImpression(channelData: Channel) {
        if (!sentDynamicChannelTrackers.contains(channelData.id)) {
            tracking?.dynamicChannelImpression(viewModel.currentSlug, channelData)
            sentDynamicChannelTrackers.add(channelData.id)
        }
    }

    override fun onClickFlashSaleActionText(applink: String, headerId: Long): View.OnClickListener {
        return View.OnClickListener {
            tracking?.flashSaleActionTextClick(viewModel.currentSlug, headerId)
            RouteManager.route(context, applink)
        }
    }

    override fun onClickFlashSaleImage(channelData: Channel, position: Int): View.OnClickListener {
        return View.OnClickListener {
            channelData.grids.getOrNull(position)?.let { gridData ->
                val applink = gridData.applink
                val campaignId = channelData.campaignID
                val campaignCode = channelData.campaignCode
                tracking?.flashSalePDPClick(
                        viewModel.currentSlug,
                        channelData.header?.name ?: "",
                        (position + POS_1).toString(POS_10),
                        gridData,
                        campaignId,
                        campaignCode
                )
                RouteManager.route(context, applink)
            }

        }
    }

    override fun flashSaleImpression(channelData: Channel) {
        if (!sentDynamicChannelTrackers.contains(channelData.id)) {
            val campaignId = channelData.campaignID
            tracking?.flashSaleImpression(viewModel.currentSlug, channelData, campaignId)
            sentDynamicChannelTrackers.add(channelData.id)
        }
    }

    override fun onClickMixActionText(applink: String): View.OnClickListener {
        return View.OnClickListener {
            RouteManager.route(context, applink)
        }
    }

    override fun onClickMixImage(channelData: Channel, position: Int): View.OnClickListener {
        return View.OnClickListener {
            channelData.grids.getOrNull(position)?.let { gridData ->
                val applink = gridData.applink
                tracking?.dynamicChannelMixCardClick(
                        viewModel.currentSlug,
                        channelData.header?.name ?: "",
                        (position + POS_1).toString(POS_10),
                        gridData,
                        channelData.campaignCode,
                        channelData.campaignID.toString()
                )

                RouteManager.route(context, applink)
            }

        }
    }

    override fun onClickMixBanner(channelData: Channel): View.OnClickListener {
        return View.OnClickListener {
            val bannerData = channelData.banner
            val applink = bannerData?.applink ?: ""

            bannerData?.let {
                tracking?.dynamicChannelMixBannerClick(
                        viewModel.currentSlug,
                        channelData.header?.name ?: "",
                        it,
                        channelData
                )
            }

            RouteManager.route(context, applink)
        }
    }

    override fun mixImageImpression(channelData: Channel) {
        val impressionTag = "Images Impression"

        if (!sentDynamicChannelTrackers.contains(channelData.id + impressionTag)) {
            tracking?.dynamicChannelMixCardImpression(viewModel.currentSlug, channelData)
            sentDynamicChannelTrackers.add(channelData.id + impressionTag)
        }
    }

    override fun mixBannerImpression(channelData: Channel) {
        val impressionTag = "Banner Impression"

        if (!sentDynamicChannelTrackers.contains(channelData.id + impressionTag)) {
            tracking?.dynamicChannelMixBannerImpression(viewModel.currentSlug, channelData)
            sentDynamicChannelTrackers.add(channelData.id + impressionTag)
        }
    }

    override fun onFlashSaleCardImpressed(position: Int, grid: Grid, channel: Channel) {
        tracking?.flashSaleCardImpression(
                viewModel.currentSlug,
                channel,
                grid,
                position.toString(),
                viewModel.isLoggedIn()
        )
    }

    override fun onMixFlashSaleSeeAllClicked(channel: Channel, applink: String) {
        tracking?.seeAllMixFlashSaleClicked(
                viewModel.currentSlug,
                channel
        )
        if (!TextUtils.isEmpty(applink)) {
            RouteManager.route(context, applink)
        }
    }

    override fun onFlashSaleCardClicked(position: Int, channel: Channel, grid: Grid, applink: String) {
        tracking?.flashSaleCardClicked(
                viewModel.currentSlug,
                channel,
                grid,
                position.toString(),
                viewModel.isLoggedIn()
        )
        RouteManager.route(context, applink)
    }

    override fun onClickMixTopBannerItem(applink: String) {
        RouteManager.route(context, applink)
    }

    override fun onClickMixTopBannerCtaButton(cta: Cta, channelId: String, applink: String, channelBannerAttribution: String) {
        tracking?.mixTopBannerCtaButtonClicked(
                viewModel.currentSlug,
                cta.text,
                channelId
        )
        if (cta.couponCode.isEmpty()) {
            RouteManager.route(context, applink)
        } else {
            view?.let {
                copyCoupon(it, cta)
            }
        }
    }

    override fun onClickMixLeftBannerImage(channel: Channel, position: Int) {
        if (RouteManager.route(context, channel.banner?.applink.orEmpty())) {
            tracking?.eventClickMixLeftImageBanner(channel, category?.title.orEmpty(), position)
        }
    }



    override fun onMixLeftBannerImpressed(channel: Channel, position: Int) {
        tracking?.eventImpressionMixLeftImageBanner(channel, category?.title.orEmpty(), position)
    }

    override fun onClickMixLeftBannerImage(channel: ChannelModel, position: Int) {
        tracking?.trackerObj?.sendEnhanceEcommerceEvent(
                OSMixLeftTracking.eventClickMixLeftImageBanner(channel, category?.title.orEmpty(), position) as HashMap<String, Any>)
        RouteManager.route(context, channel.channelBanner?.applink.orEmpty())
    }

    override fun onMixLeftBannerImpressed(channel: ChannelModel, position: Int) {
        tracking?.trackingQueueObj?.putEETracking(
                OSMixLeftTracking.eventImpressionMixLeftImageBanner(channel, category?.title.orEmpty(), position, getUserId()) as HashMap<String, Any>)
    }

    override fun onFlashSaleCardImpressedComponent(position: Int, grid: ChannelGrid, channel: ChannelModel) {
        tracking?.flashSaleCardImpressionComponent(
                viewModel.currentSlugDC,
                channel,
                grid,
                position.toString(),
                viewModel.isLoggedIn(),
                viewModel.getUserId()
        )
    }

    override fun onMixFlashSaleSeeAllClickedComponent(channel: ChannelModel, applink: String) {
        tracking?.seeAllMixFlashSaleClickedComponent(
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

    override fun onSeeAllBannerClickedComponent(channel: ChannelModel, applink: String) {
        tracking?.seeAllBannerFlashSaleClickedComponent(
                viewModel.currentSlugDC,
                channel
        )
        if (!TextUtils.isEmpty(applink)) {
            RouteManager.route(context, applink)
        }
    }

    override fun onFlashSaleCardClickedComponent(position: Int, channel: ChannelModel, grid: ChannelGrid, applink: String) {
        tracking?.flashSaleCardClickedComponent(
                viewModel.currentSlugDC,
                channel,
                grid,
                position,
                viewModel.isLoggedIn(),
                viewModel.getUserId()
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
                isLogin = viewModel.isLoggedIn(),
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
                isLogin = viewModel.isLoggedIn(),
                shopId = shopData.shopId.orEmpty(),
                campaignCode = shopData.campaignCode.orEmpty()
        )
        RouteManager.route(context, shopData.url)
    }

    override fun onFeaturedShopDCClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        tracking?.trackingQueueObj?.putEETracking(
                OSFeaturedShopTracking.getEventClickShopWidget(
                        channel = channelModel,
                        grid = channelGrid,
                        categoryName = category?.title?:"",
                        bannerPosition = position,
                        userId = userSession.userId
                ) as HashMap<String, Any>
        )
        goToApplink(channelGrid.applink)
    }

    override fun onFeaturedShopDCImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        tracking?.trackerObj?.sendEnhanceEcommerceEvent(
                OSFeaturedShopTracking.getEventImpressionShopWidget(
                        channel = channelModel,
                        grid = channelGrid,
                        categoryName = category?.title?:"",
                        bannerPosition = position,
                        userId = userSession.userId
                ) as HashMap<String, Any>
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

    private fun removeLoading(isCache: Boolean) {
        val osPltCallback = getOfficialStorePageLoadTimeCallback()
        if (osPltCallback != null) {
            osPltCallback.stopNetworkRequestPerformanceMonitoring()
            osPltCallback.startRenderPerformanceMonitoring()
        }
        setPerformanceListenerForRecyclerView(isCache)
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
                            officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.startCustomMetric(OSPerformanceConstant.KEY_PERFORMANCE_OS_HOME_BANNER_CACHE)
                        },
                        onBannerCacheStopLoad = {
                            officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.stopCustomMetric(OSPerformanceConstant.KEY_PERFORMANCE_OS_HOME_BANNER_CACHE)
                        },
                        onBannerCloudStartLoad = {
                            officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.startCustomMetric(OSPerformanceConstant.KEY_PERFORMANCE_OS_HOME_BANNER_CLOUD)
                        },
                        onBannerCloudStopLoad = {
                            officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.stopCustomMetric(OSPerformanceConstant.KEY_PERFORMANCE_OS_HOME_BANNER_CLOUD)
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
        officialHomeMapper.resetState(adapter)
        viewModel.loadFirstData(category, getLocation())
    }

    private fun observeBannerData() {
        viewModel.officialStoreBannersResult.observe(viewLifecycleOwner) {
            val resultValue = it.second

            val shouldShowErrorMessage = it.first
            when (resultValue) {
                is Success -> {
                    if (resultValue.data.banners.isNotEmpty() && (this.currentBannerData == null || this.currentBannerData != resultValue.data)) {
                        this.currentBannerData = resultValue.data
                        removeLoading(resultValue.data.isCache)
                        swipeRefreshLayout?.isRefreshing = false
                        officialHomeMapper.mappingBanners(
                            resultValue.data,
                            adapter,
                            category?.title,
                            isEligibleForDisableMappingBanner()
                        )
                    }
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (shouldShowErrorMessage) {
                        showErrorNetwork(resultValue.throwable)
                    }
                }
            }
            bannerPerformanceMonitoring.stopTrace()
        }
    }

    private fun observeBenefit() {
        viewModel.officialStoreBenefitsResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if(!isEligibleForDisableMappingBenefit()) {
                        officialHomeMapper.mappingBenefit(it.data, adapter)
                    }
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorNetwork(it.throwable)
                }

            }
        }
    }

    private fun observeFeaturedShop() {
        viewModel.officialStoreFeaturedShopResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if(!isEligibleForDisableMappingOfficialFeaturedShop()) {
                        officialHomeMapper.mappingFeaturedShop(it.data, adapter, category?.title, this)
                    }
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorNetwork(it.throwable)
                }

            }
            shopPerformanceMonitoring.stopTrace()
        }
    }

    private fun observeDynamicChannel() {
        viewModel.officialStoreDynamicChannelResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    officialHomeMapper.mappingDynamicChannel(
                            result.data,
                            adapter,
                            remoteConfig
                    )
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorNetwork(result.throwable)
                }
            }
            dynamicChannelPerformanceMonitoring.stopTrace()
        })
    }

    private fun observeProductRecommendation() {
        viewModel.productRecommendation.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    PRODUCT_RECOMMENDATION_TITLE_SECTION = it.data.recommendationWidget.title
                    endlessScrollListener.updateStateAfterGetData()
                    swipeRefreshLayout?.isRefreshing = false
                    if (counterTitleShouldBeRendered == 1) {
                        officialHomeMapper.mappingProductRecommendationTitle(it.data.recommendationWidget.title, adapter)
                    }
                    officialHomeMapper.mappingProductRecommendation(it.data, adapter, this)
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorNetwork(it.throwable)
                }
            }
            productRecommendationPerformanceMonitoring.stopTrace()
        })
    }

    private fun observeFeaturedShopSuccessDC() {
        viewModel.featuredShopResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    //update UI
                    if (!isEligibleForDisableShopWidget()) {
                        officialHomeMapper.updateFeaturedShopDC(
                            it.data
                        ) { newDataList ->
                            adapter?.submitList(newDataList)
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun observeFeaturedShopRemoveDC() {
        viewModel.featuredShopRemove.observe(viewLifecycleOwner) {
            if (!isEligibleForDisableRemoveShopWidget()) {
                officialHomeMapper.removeFeaturedShopDC(it) { newDataList ->
                    adapter?.submitList(newDataList)
                }
            }
        }
    }

    private fun showErrorNetwork(t: Throwable) {
        view?.let {
            Toaster.build(it,
                    ErrorHandler.getErrorMessage(context, t),
                    Snackbar.LENGTH_LONG, TYPE_ERROR
            ).show()
        }
    }

    private fun removeRecomWidget() {
        if (!isEligibleForDisableRemoveBestSellerWidget()) {
            officialHomeMapper.removeRecomWidget {
                adapter?.submitList(it)
            }
        }
    }

    private fun setListener() {
        setLoadMoreListener()
        swipeRefreshLayout?.setOnRefreshListener {
            counterTitleShouldBeRendered = 0
            officialHomeMapper.removeRecommendation(adapter)
            removeRecomWidget()
            officialHomeMapper.removeTopAdsHeadlineWidget(adapter)
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

                        if (!isScrolling) {
                            isScrolling = true
                            scrollListener.onContentScrolled(dy)
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
                viewModel.isLoggedIn(),
                productCardOptionsModel.productId.toLongOrZero(),
                productCardOptionsModel.isTopAds
        )
    }

    private fun handleWishlistActionForLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        val isUsingWishlistV2 = productCardOptionsModel.wishlistResult.isUsingWishlistV2
        if (productCardOptionsModel.wishlistResult.isSuccess)
            if (isUsingWishlistV2) handleWishlistV2ActionSuccess(productCardOptionsModel)
            else handleWishlistActionSuccess(productCardOptionsModel)
        else
            if (isUsingWishlistV2) showErrorWishlistV2(productCardOptionsModel.wishlistResult)
            else showErrorWishlist()
    }

    private fun handleWishlistActionSuccess(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isAddWishlist)
            showSuccessAddWishlist()
        else
            showSuccessRemoveWishlist()

        updateWishlist(productCardOptionsModel.wishlistResult.isAddWishlist, productCardOptionsModel.productPosition)
    }

    private fun handleWishlistV2ActionSuccess(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isAddWishlist)
            showSuccessAddWishlistV2(productCardOptionsModel.wishlistResult)
        else
            showSuccessRemoveWishlistV2()

        updateWishlist(productCardOptionsModel.wishlistResult.isAddWishlist, productCardOptionsModel.productPosition)
    }

    private fun showSuccessAddWishlist() {
        activity?.let { activity ->
            val view = activity.findViewById<View>(android.R.id.content) ?: return

            val msg = getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg)
            val ctaText = getString(com.tokopedia.wishlist_common.R.string.cta_success_add_to_wishlist)
            Toaster.build(view, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, ctaText) { RouteManager.route(activity, ApplinkConst.WISHLIST) }.show()
        }
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

    private fun showSuccessRemoveWishlist() {
        activity?.let {
            val view = it.findViewById<View>(android.R.id.content) ?: return
            val message = getString(com.tokopedia.officialstore.R.string.msg_success_remove_wishlist)

            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showSuccessRemoveWishlistV2() {
        activity?.let {
            val view = it.findViewById<View>(android.R.id.content) ?: return
            val message = getString(Rwishlist.string.on_success_remove_from_wishlist_msg)

            Toaster.build(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun showErrorWishlist() {
        activity?.let {
            val view = it.findViewById<View>(android.R.id.content) ?: return
            Toaster.build(view, ErrorHandler.getErrorMessage(it, null), Snackbar.LENGTH_LONG, TYPE_ERROR).show()
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
                PRODUCT_RECOMMENDATION_TITLE_SECTION,
                viewModel.isLoggedIn(),
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

    private fun isEligibleForDisableShopWidget(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_SHOP_WIDGET)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun isEligibleForDisableBestSellerWidget(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_BEST_SELLER_WIDGET)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun isEligibleForDisableMappingBanner(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_MAPPING_BANNERS)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun isEligibleForDisableRemoveBestSellerWidget(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_REMOVE_BEST_SELLER_WIDGET)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun isEligibleForDisableRemoveShopWidget(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_REMOVE_SHOP_WIDGET)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun isEligibleForDisableMappingBenefit(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_MAPPING_BENEFIT)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun isEligibleForDisableMappingOfficialFeaturedShop(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_MAPPING_OFFICIAL_FEATURED_SHOP)
                ?: false
        } catch (e: Exception) {
            false
        }
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

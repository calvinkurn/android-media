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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
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
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.navigation_common.listener.OfficialStorePerformanceMonitoringListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.officialstore.FirebasePerformanceMonitoringConstant
import com.tokopedia.officialstore.OfficialStoreInstance
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.common.listener.RecyclerViewScrollListener
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Cta
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.officialstore.official.di.DaggerOfficialStoreHomeComponent
import com.tokopedia.officialstore.official.di.OfficialStoreHomeComponent
import com.tokopedia.officialstore.official.di.OfficialStoreHomeModule
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationTitleViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationViewModel
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.officialstore.official.presentation.listener.OfficialStoreHomeComponentCallback
import com.tokopedia.officialstore.official.presentation.listener.OfficialStoreLegoBannerComponentCallback
import com.tokopedia.officialstore.official.presentation.viewmodel.OfficialStoreHomeViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class OfficialHomeFragment :
        BaseDaggerFragment(),
        HasComponent<OfficialStoreHomeComponent>,
        RecommendationListener,
        FeaturedShopListener,
        DynamicChannelEventHandler {

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

        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeFragment().apply { arguments = bundle }
    }

    private var officialStorePerformanceMonitoringListener: OfficialStorePerformanceMonitoringListener? = null
    private val sentDynamicChannelTrackers = mutableSetOf<String>()

    @Inject
    lateinit var viewModel: OfficialStoreHomeViewModel
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

    private lateinit var bannerPerformanceMonitoring: PerformanceMonitoring
    private lateinit var shopPerformanceMonitoring: PerformanceMonitoring
    private lateinit var dynamicChannelPerformanceMonitoring: PerformanceMonitoring
    private lateinit var productRecommendationPerformanceMonitoring: PerformanceMonitoring

    private val endlessScrollListener: EndlessRecyclerViewScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (swipeRefreshLayout?.isRefreshing == false) {
                    val CATEGORY_CONST: String = category?.slug.orEmpty()
                    val recomConstant = (FirebasePerformanceMonitoringConstant.PRODUCT_RECOM).replace(SLUG_CONST, CATEGORY_CONST)
                    val categories = category?.categories.toString()
                    val categoriesWithoutOpeningSquare = categories.replace("[", "") // Remove Square bracket from the string
                    val categoriesWithoutClosingSquare = categoriesWithoutOpeningSquare.replace("]", "") // Remove Square bracket from the string
                    counterTitleShouldBeRendered += 1
                    productRecommendationPerformanceMonitoring = PerformanceMonitoring.start(recomConstant)
                    viewModel.loadMoreProducts(categoriesWithoutClosingSquare, page)

                    if (adapter?.getVisitables()?.lastOrNull() is ProductRecommendationViewModel) {
                        adapter?.showLoading()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(BUNDLE_CATEGORY)
        }
        context?.let { tracking = OfficialStoreTracking(it) }
        officialStorePerformanceMonitoringListener = context?.let { castContextToOfficialStorePerformanceMonitoring(it) }
        if (getOfficialStorePageLoadTimeCallback() != null) {
            getOfficialStorePageLoadTimeCallback()!!.stopPreparePagePerformanceMonitoring()
            getOfficialStorePageLoadTimeCallback()?.startRenderPerformanceMonitoring()
        }
        remoteConfig = FirebaseRemoteConfigImpl(activity)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            loadData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_official_home_child, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_view)
        layoutManager = StaggeredGridLayoutManager(PRODUCT_RECOMM_GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        recyclerView?.layoutManager = layoutManager

        val adapterTypeFactory = OfficialHomeAdapterTypeFactory(
                this,
                this,
                this,
                OfficialStoreHomeComponentCallback(),
                OfficialStoreLegoBannerComponentCallback(this))
        adapter = OfficialHomeAdapter(adapterTypeFactory)
        recyclerView?.adapter = adapter

        return view
    }

    private fun setPerformanceListenerForRecyclerView(){
        recyclerView?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if(officialStorePerformanceMonitoringListener != null){
                    officialStorePerformanceMonitoringListener!!.stopOfficialStorePerformanceMonitoring()
                    recyclerView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (getOfficialStorePageLoadTimeCallback() != null) {
            getOfficialStorePageLoadTimeCallback()!!.startNetworkRequestPerformanceMonitoring()
        }
        super.onViewCreated(view, savedInstanceState)
        observeBannerData()
        observeBenefit()
        observeFeaturedShop()
        observeDynamicChannel()
        observeProductRecommendation()
        resetData()
        loadData()
        setListener()
    }

    override fun onPause() {
        super.onPause()
        tracking?.sendAll()
    }

    private fun resetData() {
        adapter?.clearAllElements()
        adapter?.resetState(this)
        endlessScrollListener?.resetState()
    }

    private fun loadData(isRefresh: Boolean = false) {
        initFirebasePerformanceMonitoring()

        if (userVisibleHint && isAdded && ::viewModel.isInitialized) {
            if (!isLoadedOnce || isRefresh) {
                viewModel.loadFirstData(category)
                isLoadedOnce = true

                if (!isRefresh) {
                    tracking?.sendScreen(category?.title.toEmptyStringIfNull())
                }
            }
        }
    }

    private fun observeBannerData() {
        viewModel.officialStoreBannersResult.observe(this, Observer {
            if (getOfficialStorePageLoadTimeCallback() != null) {
                getOfficialStorePageLoadTimeCallback()?.stopNetworkRequestPerformanceMonitoring()
                getOfficialStorePageLoadTimeCallback()?.startRenderPerformanceMonitoring()
            }
            setPerformanceListenerForRecyclerView()
            when (it) {
                is Success -> {
                    removeLoading()
                    swipeRefreshLayout?.isRefreshing = false
                    OfficialHomeMapper.mappingBanners(it.data, adapter, category?.title)
                    setLoadMoreListener()
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorNetwork(it.throwable)
                }
            }
            bannerPerformanceMonitoring.stopTrace()
        })
    }

    private fun observeBenefit() {
        viewModel.officialStoreBenefitsResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    OfficialHomeMapper.mappingBenefit(it.data, adapter)
                    setLoadMoreListener()
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorNetwork(it.throwable)
                }

            }
        })
    }

    private fun observeFeaturedShop() {
        viewModel.officialStoreFeaturedShopResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    OfficialHomeMapper.mappingFeaturedShop(it.data, adapter, category?.title, this)
                    setLoadMoreListener()
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorNetwork(it.throwable)
                }

            }
            shopPerformanceMonitoring.stopTrace()
        })
    }

    private fun observeDynamicChannel() {
        viewModel.officialStoreDynamicChannelResult.observe(this, Observer { result ->
            when (result) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    OfficialHomeMapper.mappingDynamicChannel(
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
        viewModel.productRecommendation.observe(this, Observer {
            when (it) {
                is Success -> {
                    PRODUCT_RECOMMENDATION_TITLE_SECTION = it.data.title
                    adapter?.hideLoading()
                    endlessScrollListener.updateStateAfterGetData()
                    swipeRefreshLayout?.isRefreshing = false
                    if (counterTitleShouldBeRendered == 1) {
                        OfficialHomeMapper.mappingProductrecommendationTitle(it.data.title, adapter)
                    }
                    OfficialHomeMapper.mappingProductRecommendation(it.data, adapter, this)
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorNetwork(it.throwable)
                }
            }
            productRecommendationPerformanceMonitoring.stopTrace()
        })
    }

    private fun observeTopAdsWishlist() {
        viewModel.topAdsWishlistResult.observe(this, Observer {
            when (it) {
                is Success -> {
                }
                is Fail -> {
                    showErrorNetwork(it.throwable)
                }
            }
        })
    }

    fun showErrorNetwork(t: Throwable) {
        view?.let {
            Toaster.showError(it,
                    ErrorHandler.getErrorMessage(context, t),
                    Snackbar.LENGTH_LONG)
        }
    }

    private fun setListener() {
        setLoadMoreListener()

        swipeRefreshLayout?.setOnRefreshListener {
            adapter?.getVisitables()?.removeAll {
                it is DynamicChannelViewModel
                        || it is ProductRecommendationViewModel
                        || it is ProductRecommendationTitleViewModel
                        || it is LoadingMoreModel
                        || it is DynamicLegoBannerDataModel
            }
            counterTitleShouldBeRendered = 0
            adapter?.notifyDataSetChanged()
            recyclerView?.removeOnScrollListener(endlessScrollListener)
            loadData(true)
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
                            }, 200)
                        }

                    }

                })
            }
        }

    }

    private fun setLoadMoreListener() {
        recyclerView?.addOnScrollListener(endlessScrollListener)
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
        viewModel.officialStoreFeaturedShopResult.removeObservers(this)
        viewModel.officialStoreDynamicChannelResult.removeObservers(this)
        viewModel.productRecommendation.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID)
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
                productCardOptionsModel.productId.toIntOrZero(),
                productCardOptionsModel.isTopAds
        )
    }

    private fun handleWishlistActionForLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess)
            handleWishlistActionSuccess(productCardOptionsModel)
        else
            showErrorWishlist()
    }

    private fun handleWishlistActionSuccess(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isAddWishlist)
            showSuccessAddWishlist()
        else
            showSuccessRemoveWishlist()

        updateWishlist(productCardOptionsModel.wishlistResult.isAddWishlist, productCardOptionsModel.productPosition)
    }

    private fun showSuccessAddWishlist() {
        activity?.let { activity ->
            val view = activity.findViewById<View>(android.R.id.content) ?: return
            val message = getString(R.string.msg_success_add_wishlist)

            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("Lihat Wishlist") { RouteManager.route(activity, ApplinkConst.WISHLIST) }
                    .show()

        }
    }

    private fun showSuccessRemoveWishlist() {
        activity?.let {
            val view = it.findViewById<View>(android.R.id.content) ?: return
            val message = getString(R.string.msg_success_remove_wishlist)

            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showErrorWishlist() {
        activity?.let {
            val view = it.findViewById<View>(android.R.id.content) ?: return
            Toaster.showError(view, ErrorHandler.getErrorMessage(it, null), Snackbar.LENGTH_LONG)
        }
    }

    private fun goToPDP(item: RecommendationItem, position: Int) {
        eventTrackerClickListener(item, position)
        RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString()).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, position)
            startActivityForResult(this, REQUEST_FROM_PDP)
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

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        lastClickLayoutType = layoutType
        if (position.size > 1) {
            lastParentPosition = position[0]
            goToPDP(item, position[1])
        } else {
            goToPDP(item, position[0])
        }
    }

    private fun updateWishlist(isWishlist: Boolean, position: Int) {
        if (position > -1 && adapter != null) {
            (adapter?.list?.getOrNull(position) as?
                    ProductRecommendationViewModel)?.productItem?.isWishlist = isWishlist
            adapter?.notifyItemChanged(position, isWishlist)
        }
    }

    private fun copyCoupon(view: View, cta: Cta) {
        val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(getString(R.string.os_coupon_code_label), cta.couponCode)
        clipboard.setPrimaryClip(clipData)
        Toaster.make(view.parent as ViewGroup,
                getString(R.string.os_toaster_coupon_copied),
                Snackbar.LENGTH_LONG)
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
        recyclerView?.post {
            adapter?.getVisitables()?.removeAll {
                it is DynamicChannelViewModel || it is ProductRecommendationViewModel
            }
            adapter?.notifyDataSetChanged()
        }
        loadData(true)
    }

    override fun onClickLegoHeaderActionText(applink: String) {
        RouteManager.route(context, applink)
    }

    override fun onClickLegoImage(channelModel: ChannelModel, position: Int) {
        val gridData = channelModel.channelGrids[position]
        val applink = gridData.applink ?: ""

        gridData.let {
            tracking?.dynamicChannelHomeComponentClick(
                    viewModel.currentSlug,
                    channelModel.channelHeader.name ?: "",
                    (position + 1).toString(10),
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
            val gridData = channelData.grids?.get(position)
            val applink = gridData?.applink ?: ""

            gridData?.let {
                tracking?.dynamicChannelImageClick(
                        viewModel.currentSlug,
                        channelData.header?.name ?: "",
                        (position + 1).toString(10),
                        it,
                        channelData
                )
            }

            RouteManager.route(context, applink)
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
            val gridData = channelData.grids?.get(position)
            val applink = gridData?.applink ?: ""
            val campaignId = channelData.campaignID
            val campaignCode = channelData.campaignCode

            gridData?.let {
                tracking?.flashSalePDPClick(
                        viewModel.currentSlug,
                        channelData.header?.name ?: "",
                        (position + 1).toString(10),
                        it,
                        campaignId,
                        campaignCode
                )
            }

            RouteManager.route(context, applink)
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
            val gridData = channelData.grids?.get(position)
            val applink = gridData?.applink ?: ""

            gridData?.let {
                tracking?.dynamicChannelMixCardClick(
                        viewModel.currentSlug,
                        channelData.header?.name ?: "",
                        (position + 1).toString(10),
                        it,
                        channelData.campaignCode
                )
            }

            RouteManager.route(context, applink)
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

    override fun onClickMixTopBannerCtaButton(cta: Cta, channelId: String, applink: String) {
        tracking?.mixTopBannerCtaButtonClicked(
                viewModel.currentSlug,
                cta.text,
                channelId
        )
        if (cta.couponCode.isEmpty()) {
            RouteManager.route(context, applink)
        } else {
            view?.let{
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

    private fun removeLoading() {
        if (getOfficialStorePageLoadTimeCallback() != null) {
            getOfficialStorePageLoadTimeCallback()?.stopNetworkRequestPerformanceMonitoring()
        }
        setPerformanceListenerForRecyclerView()

        recyclerView?.post {
            adapter?.getVisitables()?.removeAll {
                it is LoadingModel
            }
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onShopImpression(categoryName: String, position: Int, shopData: Shop) {
        tracking?.eventImpressionFeatureBrand(
                categoryName,
                position,
                shopData.name.orEmpty(),
                shopData.imageUrl.orEmpty(),
                shopData.additionalInformation.orEmpty(),
                shopData.featuredBrandId.orEmpty(),
                viewModel.isLoggedIn(),
                shopData.shopId.orEmpty()
        )
    }

    override fun onShopClick(categoryName: String, position: Int, shopData: Shop) {
        tracking?.eventClickFeaturedBrand(
                categoryName,
                position,
                shopData.name.orEmpty(),
                shopData.url.orEmpty(),
                shopData.additionalInformation.orEmpty(),
                shopData.featuredBrandId.orEmpty(),
                viewModel.isLoggedIn(),
                shopData.shopId.orEmpty(),
                shopData.campaignCode.orEmpty()
        )
        RouteManager.route(context, shopData.url)
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

    private fun castContextToOfficialStorePerformanceMonitoring(context: Context): OfficialStorePerformanceMonitoringListener? {
        return if (context is OfficialStorePerformanceMonitoringListener) {
            context
        } else null
    }

    private fun getOfficialStorePageLoadTimeCallback(): PageLoadTimePerformanceInterface? {
        return if (officialStorePerformanceMonitoringListener != null && officialStorePerformanceMonitoringListener!!.getOfficialStorePageLoadTimePerformanceInterface() != null) {
            officialStorePerformanceMonitoringListener!!.getOfficialStorePageLoadTimePerformanceInterface()
        } else null
    }
}

package com.tokopedia.search.result.presentation.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.getSortFilterCount
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.filter.common.helper.isSortHasDefaultValue
import com.tokopedia.filter.common.helper.toMapParam
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.iris.Iris
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.ProductCardLifecycleObserver
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_MPC_LIFECYCLE_OBSERVER
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_PRODUCT_CARD_VIEWSTUB
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.search.R
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.analytics.ProductClickAnalyticsData
import com.tokopedia.search.analytics.RecommendationTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.di.module.SearchContextModule
import com.tokopedia.search.di.module.SearchNavigationListenerModule
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.result.presentation.view.listener.QuickFilterElevation
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactoryImpl
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.ScreenNameProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.addtocart.AddToCartVariantBottomSheetLauncher
import com.tokopedia.search.result.product.addtocart.analytics.AddToCartTracking
import com.tokopedia.search.result.product.banner.BannerListenerDelegate
import com.tokopedia.search.result.product.broadmatch.BroadMatchListenerDelegate
import com.tokopedia.search.result.product.changeview.ChangeView
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressListener
import com.tokopedia.search.result.product.cpm.BannerAdsListenerDelegate
import com.tokopedia.search.result.product.cpm.BannerAdsPresenter
import com.tokopedia.search.result.product.emptystate.EmptyStateListenerDelegate
import com.tokopedia.search.result.product.filter.analytics.SearchSortFilterTracking
import com.tokopedia.search.result.product.filter.bottomsheetfilter.BottomSheetFilterViewDelegate
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavListenerDelegate
import com.tokopedia.search.result.product.grid.ProductGridType
import com.tokopedia.search.result.product.inspirationbundle.InspirationBundleListenerDelegate
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselListenerDelegate
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcListenerDelegate
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetListenerDelegate
import com.tokopedia.search.result.product.lastfilter.LastFilterListenerDelegate
import com.tokopedia.search.result.product.onboarding.OnBoardingListenerDelegate
import com.tokopedia.search.result.product.performancemonitoring.PerformanceMonitoringModule
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationListener
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaListenerDelegate
import com.tokopedia.search.result.product.suggestion.SuggestionListenerDelegate
import com.tokopedia.search.result.product.tdn.TopAdsImageViewListenerDelegate
import com.tokopedia.search.result.product.ticker.TickerListenerDelegate
import com.tokopedia.search.result.product.video.SearchVideoPreference
import com.tokopedia.search.result.product.videowidget.VideoCarouselListenerDelegate
import com.tokopedia.search.result.product.violation.ViolationListenerDelegate
import com.tokopedia.search.result.product.wishlist.WishlistHelper
import com.tokopedia.search.utils.BackToTopView
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.SearchIdlingResource
import com.tokopedia.search.utils.SearchLogger
import com.tokopedia.search.utils.SmallGridSpanCount
import com.tokopedia.search.utils.applinkmodifier.ApplinkModifier
import com.tokopedia.search.utils.applyQuickFilterElevation
import com.tokopedia.search.utils.componentIdMap
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.search.utils.manualFilterToggleMap
import com.tokopedia.search.utils.originFilterMap
import com.tokopedia.search.utils.removeQuickFilterElevation
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.Category
import com.tokopedia.topads.sdk.domain.model.FreeOngkir
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.video_widget.VideoPlayerAutoplay
import com.tokopedia.video_widget.carousel.VideoCarouselWidgetCoordinator
import com.tokopedia.video_widget.util.networkmonitor.DefaultNetworkMonitor
import org.json.JSONArray
import javax.inject.Inject

class ProductListFragment: BaseDaggerFragment(),
    ProductListSectionContract.View,
    ProductListener,
    RecommendationListener,
    QuickFilterElevation,
    ChooseAddressListener,
    ProductListParameterListener,
    QueryKeyProvider,
    SearchParameterProvider,
    FragmentProvider,
    ClassNameProvider,
    ScreenNameProvider,
    BackToTopView {

    companion object {
        private const val SCREEN_SEARCH_PAGE_PRODUCT_TAB = "Search result - Product tab"
        private const val REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123
        private const val SEARCH_RESULT_ENHANCE_ANALYTIC = "SEARCH_RESULT_ENHANCE_ANALYTIC"
        private const val LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT"
        private const val EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER"
        private const val LABEL_POSITION_VIEW = "view"

        fun newInstance(searchParameter: SearchParameter?): ProductListFragment {
            val args = Bundle().apply {
                putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter)
            }

            return ProductListFragment().apply {
                arguments = args
            }
        }
    }

    var presenter: ProductListSectionContract.Presenter? = null
        @Inject set

    @Inject
    lateinit var iris: Iris

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var smallGridSpanCount: SmallGridSpanCount

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var recyclerViewUpdater: RecyclerViewUpdater

    @Inject
    lateinit var lastFilterListenerDelegate: LastFilterListenerDelegate

    @Inject
    lateinit var filterController: FilterController

    @Inject
    lateinit var onBoardingListenerDelegate: OnBoardingListenerDelegate

    @Inject
    lateinit var gridLayoutManager: GridLayoutManager

    @Inject
    lateinit var spanSizeLookup: SpanSizeLookup

    @Inject @Suppress("LateinitUsage")
    lateinit var sameSessionRecommendationListener: SameSessionRecommendationListener

    @Inject @Suppress("LateinitUsage")
    lateinit var changeView: ChangeView

    @Inject
    lateinit var recycledViewPool: RecyclerView.RecycledViewPool

    @Inject
    lateinit var inspirationListAtcListenerDelegate: InspirationListAtcListenerDelegate

    @Inject @Suppress("LateinitUsage")
    lateinit var applinkModifier: ApplinkModifier

    @Suppress("LateinitUsage")
    @Inject
    lateinit var productVideoAutoplay: VideoPlayerAutoplay

    @Inject
    lateinit var atcVariantBottomSheetLauncher: AddToCartVariantBottomSheetLauncher

    @Inject @Suppress("LateinitUsage")
    lateinit var wishlistHelper: WishlistHelper

    @Suppress("LateinitUsage")
    @Inject
    lateinit var bottomSheetFilterViewDelegate: BottomSheetFilterViewDelegate

    @Suppress("LateinitUsage")
    @Inject
    lateinit var searchVideoPreference: SearchVideoPreference

    private var refreshLayout: SwipeRefreshLayout? = null
    private var gridLayoutLoadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var searchNavigationListener: SearchNavigationListener? = null
    private var performanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var redirectionListener: RedirectionListener? = null
    private var searchParameter: SearchParameter? = null
    private var irisSession: IrisSession? = null
    private var searchSortFilter: SortFilter? = null
    private var shimmeringView: LinearLayout? = null

    override var productCardLifecycleObserver: ProductCardLifecycleObserver? = null
        private set

    private val isSneakPeekEnabled: Boolean by lazy {
        searchVideoPreference.isSneakPeekEnabled && getABTestVideoSneakPeek()
    }

    private fun getABTestVideoSneakPeek(): Boolean {
        return try {
            val abTestVideoSneakPeekAutoPlay = abTestRemoteConfig?.getString(
                RollenceKey.SEARCH_VIDEO_SNEAK_PEEK_AUTOPLAY,
                ""
            )
            RollenceKey.SEARCH_VIDEO_SNEAK_PEEK_AUTOPLAY_VARIANT == abTestVideoSneakPeekAutoPlay
                || RollenceKey.SEARCH_VIDEO_SNEAK_PEEK_AUTOPLAY_OTHER_VARIANT == abTestVideoSneakPeekAutoPlay
        } catch (e: Exception) {
            false
        }
    }

    private lateinit var videoCarouselWidgetCoordinator : VideoCarouselWidgetCoordinator
    private lateinit var networkMonitor : DefaultNetworkMonitor

    override fun getFragment(): Fragment = this

    //region onCreate Fragments
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadDataFromArguments()
        initProductCardLifecycleObserver()
        initNetworkMonitor()
    }

    private fun loadDataFromArguments() {
        arguments?.let {
            copySearchParameter(it.getParcelable(EXTRA_SEARCH_PARAMETER))
        }
    }

    private fun copySearchParameter(searchParameterToCopy: SearchParameter?) {
        if (searchParameterToCopy != null) {
            searchParameter = SearchParameter(searchParameterToCopy).apply {
                presenter?.modifySearchParameterIfShowAdultEnabled(this)
            }
        }
    }

    private fun initProductCardLifecycleObserver() {
        if (!remoteConfig.getBoolean(ENABLE_MPC_LIFECYCLE_OBSERVER)) return

        ProductCardLifecycleObserver().also {
            productCardLifecycleObserver = it
            lifecycle.addObserver(it)
        }
    }

    private fun initBottomSheetFilterLifecycleObserver() {
        viewLifecycleOwner.lifecycle.addObserver(bottomSheetFilterViewDelegate)
    }

    private fun initNetworkMonitor() {
        networkMonitor = DefaultNetworkMonitor(activity, this)
    }

    override fun initInjector() {
        val activity = activity ?: return

        DaggerProductListViewComponent.builder()
            .baseAppComponent(getComponent(BaseAppComponent::class.java))
            .searchContextModule(SearchContextModule(activity))
            .performanceMonitoringModule(PerformanceMonitoringModule(performanceMonitoring))
            .productListFragmentModule(ProductListFragmentModule(this))
            .searchNavigationListenerModule(SearchNavigationListenerModule(searchNavigationListener))
            .build()
            .inject(this)
    }
    //endregion

    //region onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        presenter?.attachView(this)

        if (irisSession == null && container != null)
            irisSession = IrisSession(container.context)

        return inflater.inflate(R.layout.search_result_product_fragment_layout, null)
    }
    //endregion

    //region onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreInstanceState(savedInstanceState)
        initVideoCarouselWidgetController()
        initViews(view)
        addDefaultSelectedSort()
        initProductVideoAutoplayLifecycleObserver()
        initBottomSheetFilterLifecycleObserver()

        presenter?.onViewCreated()
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) return

        copySearchParameter(savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER))
    }

    private fun addDefaultSelectedSort() {
        val searchParameter = searchParameter ?: return
        if (searchParameter.get(SearchApiConst.OB).isEmpty())
            searchParameter.set(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
    }

    private fun initProductVideoAutoplayLifecycleObserver() {
        productVideoAutoplay.registerLifecycleObserver(viewLifecycleOwner)
    }

    private fun initVideoCarouselWidgetController() {
        videoCarouselWidgetCoordinator = VideoCarouselWidgetCoordinator(
            lifecycleOwner = this,
            hasExternalAutoPlayController = true,
        )
    }
    //endregion

    //region View Related stuff (recycler view, layout managers, load more, shimmering, etc
    private fun initViews(view: View) {
        initSwipeToRefresh(view)

        initSearchQuickSortFilter(view)
        initShimmeringView(view)

        initLoadMoreListener()
        setupRecyclerView(view)
    }

    private fun initSwipeToRefresh(view: View) {
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        refreshLayout?.setOnRefreshListener(this::reloadData)
    }

    private fun initSearchQuickSortFilter(rootView: View) {
        searchSortFilter = rootView.findViewById(R.id.search_product_quick_sort_filter)
    }

    private fun initShimmeringView(view: View) {
        shimmeringView = view.findViewById(R.id.shimmeringView)
    }

    private fun initLoadMoreListener() {
        val layoutManager = changeView.activeLayoutManager ?: return
        gridLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
    }

    private fun getEndlessRecyclerViewListener(
        recyclerViewLayoutManager: RecyclerView.LayoutManager,
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                val searchParameterMap = searchParameter?.getSearchParameterMap() ?: return
                SearchIdlingResource.increment()
                presenter?.loadMoreData(searchParameterMap)
            }
        }
    }

    private fun setupRecyclerView(rootView: View) {
        gridLayoutManager.spanSizeLookup = spanSizeLookup
        recyclerViewUpdater.initialize(
            rootView.findViewById(R.id.recyclerview),
            changeView.activeLayoutManager,
            listOf(
                gridLayoutLoadMoreTriggerListener,
                onBoardingListenerDelegate.createScrollListener(),
            ),
            createProductListTypeFactory(),
            viewLifecycleOwner,
        )

        recyclerViewUpdater.recyclerView?.let {
            productVideoAutoplay.setUp(it)
        }
    }

    private fun createProductListTypeFactory(): ProductListTypeFactoryImpl {
        val inspirationWidgetListenerDelegate = InspirationWidgetListenerDelegate(
            activity,
            this,
            filterController,
            this,
        )

        val videoCarouselListenerDelegate = VideoCarouselListenerDelegate(
            activity,
            trackingQueue,
            this,
            this
        )

        return ProductListTypeFactoryImpl(
            fragmentProvider = this,
            productListener = this,
            tickerListener = TickerListenerDelegate(
                iris,
                filterController,
                recyclerViewUpdater,
                presenter,
                this,
                presenter,
            ),
            suggestionListener = SuggestionListenerDelegate(iris, applinkModifier, activity),
            globalNavListener = GlobalNavListenerDelegate(trackingQueue, activity, iris),
            bannerAdsListener = BannerAdsListenerDelegate(
                this@ProductListFragment,
                activity,
                redirectionListener,
                presenter as BannerAdsPresenter,
                getUserId(),
            ),
            emptyStateListener = EmptyStateListenerDelegate(
                activity,
                this,
                filterController,
                redirectionListener,
                this,
            ),
            recommendationListener = this,
            inspirationCarouselListener = InspirationCarouselListenerDelegate(
                this,
                activity,
                this,
                trackingQueue,
                getUserId(),
                presenter
            ),
            broadMatchListener = BroadMatchListenerDelegate(
                presenter,
                productCardLifecycleObserver,
                this,
                this,
            ),
            inspirationCardListener = inspirationWidgetListenerDelegate,
            searchInTokopediaListener = SearchInTokopediaListenerDelegate(activity),
            changeViewListener = changeView,
            topAdsImageViewListener = TopAdsImageViewListenerDelegate(activity),
            chooseAddressListener = this,
            bannerListener = BannerListenerDelegate(iris, activity),
            lastFilterListener = lastFilterListenerDelegate,
            inspirationFilterListener = inspirationWidgetListenerDelegate,
            violationListener = ViolationListenerDelegate(activity),
            videoCarouselListener = videoCarouselListenerDelegate,
            videoCarouselWidgetCoordinator = videoCarouselWidgetCoordinator,
            inspirationBundleListener = InspirationBundleListenerDelegate(
                activity,
                iris,
                trackingQueue,
                this,
            ),
            inspirationListAtcListener = inspirationListAtcListenerDelegate,
            networkMonitor = networkMonitor,
            isUsingViewStub = remoteConfig.getBoolean(ENABLE_PRODUCT_CARD_VIEWSTUB),
            sameSessionRecommendationListener = sameSessionRecommendationListener,
            recycledViewPool = recycledViewPool,
            isSneakPeekEnabled = isSneakPeekEnabled,
        )
    }

    override fun showRefreshLayout() {
        refreshLayout?.isRefreshing = true
    }

    override fun hideRefreshLayout() {
        refreshLayout?.isRefreshing = false
    }
    //endregion

    //region Search parameter related stuff
    override val isFirstActiveTab
        get() = getActiveTab() == SearchConstant.ActiveTab.PRODUCT

    private fun getActiveTab() = searchParameter?.get(SearchApiConst.ACTIVE_TAB) ?: ""

    override val queryKey: String
        get() = searchParameter?.getSearchQuery() ?: ""

    override val previousKeyword: String
        get() = searchParameter?.get(SearchApiConst.PREVIOUS_KEYWORD) ?: ""

    override val isLandingPage: Boolean
        get() = searchParameter?.getBoolean(SearchApiConst.LANDING_PAGE) ?: false

    override fun getSearchParameter(): SearchParameter? {
        return searchParameter
    }
    //endregion

    //region onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        searchNavigationListener = castContextToSearchNavigationListener(context)
        redirectionListener = castContextToRedirectionListener(context)
        performanceMonitoring = castContextToPerformanceMonitoring(context)
    }

    private fun castContextToSearchNavigationListener(context: Context): SearchNavigationListener? {
        return if (context is SearchNavigationListener) context else null
    }

    private fun castContextToRedirectionListener(context: Context): RedirectionListener? {
        return if (context is RedirectionListener) context else null
    }

    private fun castContextToPerformanceMonitoring(
        context: Context
    ): PageLoadTimePerformanceInterface? =
        if (context is PageLoadTimePerformanceInterface) context else null

    //endregion

    override fun onResume() {
        super.onResume()

        presenter?.onViewResumed()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        presenter?.onViewVisibilityChanged(isVisibleToUser, isAdded)
    }

    override fun trackScreenAuthenticated() {
        if (userVisibleHint
                && activity != null
                && activity?.applicationContext != null) {
            SearchTracking.screenTrackSearchSectionFragment(screenName)
        }
    }

    //region adding visitable list to recycler view adapter
    override fun addProductList(list: List<Visitable<*>>) {
        recyclerViewUpdater.appendItems(list)
        SearchIdlingResource.decrement()
    }

    override fun setProductList(list: List<Visitable<*>>) {
        recyclerViewUpdater.setItems(list)
        SearchIdlingResource.decrement()
    }

    override fun addLoading() {
        recyclerViewUpdater.addLoading()
    }

    override fun removeLoading() {
        recyclerViewUpdater.removeLoading()
    }

    override fun addLocalSearchRecommendation(visitableList: List<Visitable<*>>) {
        recyclerViewUpdater.appendItems(visitableList)
    }

    override fun refreshItemAtIndex(index: Int) {
        recyclerViewUpdater.refreshItemAtIndex(index)
    }
    //endregion

    //region network error handler
    override fun showNetworkError(throwable: Throwable?) {
        val productListAdapter = recyclerViewUpdater.productListAdapter ?: return

        SearchIdlingResource.decrement()
        if (productListAdapter.isListEmpty())
            showNetworkErrorOnEmptyList(throwable)
        else
            showNetworkErrorOnLoadMore(throwable)
    }

    private fun showNetworkErrorOnEmptyList(throwable: Throwable?) {
        hideViewOnError()
        if (throwable != null) {
            NetworkErrorHelper.showEmptyState(activity,view, ErrorHandler.getErrorMessage(requireContext(), throwable)) {
                refreshLayout?.visible()
                reloadData()
            }
        } else {
            NetworkErrorHelper.showEmptyState(activity, view) {
                refreshLayout?.visible()
                reloadData()
            }
        }
    }

    private fun hideViewOnError() {
        searchSortFilter?.gone()
        shimmeringView?.gone()
        refreshLayout?.gone()
    }

    private fun showNetworkErrorOnLoadMore(throwable: Throwable?) {
        val searchParameter = searchParameter ?: return
        if (throwable!= null) {
            NetworkErrorHelper.createSnackbarWithAction(activity, ErrorHandler.getErrorMessage(requireContext(), throwable)) {
                addLoading()
                SearchIdlingResource.increment()
                presenter?.loadMoreData(searchParameter.getSearchParameterMap())
            }
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity) {
                addLoading()
                SearchIdlingResource.increment()
                presenter?.loadMoreData(searchParameter.getSearchParameterMap())
            }.showRetrySnackbar()
        }
    }
    //endregion

    //region onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_GOTO_PRODUCT_DETAIL)
            wishlistHelper.handleActivityResult(requestCode, resultCode, data)

        activity?.let {
            AdultManager.handleActivityResult(it, requestCode, resultCode, data)
            handleProductCardOptionsActivityResult(
                    requestCode = requestCode,
                    resultCode = resultCode,
                    data = data,
                    wishlistCallback = object : ProductCardOptionsWishlistCallback {
                        override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                            handleWishlistAction(productCardOptionsModel)
                        }
                    }
            )

            atcVariantBottomSheetLauncher.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        presenter?.handleWishlistAction(productCardOptionsModel)
    }
    //endregion

    override fun onPause() {
        super.onPause()

        onBoardingListenerDelegate.dismissCoachmark()
        trackingQueue?.sendAll()
    }

    //region product item (organic and topads) impression, click, and 3 dots click
    override fun onProductImpressed(item: ProductItemDataView?, adapterPosition: Int) {
        presenter?.onProductImpressed(item, adapterPosition)
    }

    private val additionalPositionMap: Map<String, String>
        get() = mapOf(
            LABEL_POSITION_VIEW to changeView.viewType.trackingLabel,
        )

    override fun sendProductImpressionTrackingEvent(
        item: ProductItemDataView,
        suggestedRelatedKeyword: String,
    ) {
        val userId = getUserId()
        val eventLabel = getSearchProductTrackingEventLabel(item, suggestedRelatedKeyword)
        val dataLayerList = ArrayList<Any>()
        val productItemDataViews = mutableListOf<ProductItemDataView>()
        val irisSessionId = irisSession?.getSessionId() ?: ""
        val filterSortParams = searchParameter?.let {
            getSortFilterParamsString(it.getSearchParameterMap() as Map<String?, Any?>)
        } ?: ""
        val pageComponentId = presenter?.pageComponentId ?: ""

        dataLayerList.add(
            item.getProductAsObjectDataLayer(
                filterSortParams,
                pageComponentId,
                additionalPositionMap,
            )
        )
        productItemDataViews.add(item)

        SearchTracking.eventImpressionSearchResultProduct(
            trackingQueue,
            dataLayerList,
            eventLabel,
            irisSessionId,
            userId,
        )
    }

    private fun getSearchProductTrackingEventLabel(item: ProductItemDataView, suggestedRelatedKeyword: String): String {
        val keyword = if (suggestedRelatedKeyword.isEmpty()) queryKey else suggestedRelatedKeyword
        val pageTitle = item.pageTitle ?: ""

        return if (pageTitle.isEmpty()) keyword else pageTitle
    }

    override fun sendTopAdsGTMTrackingProductImpression(item: ProductItemDataView) {
        val trackingQueue = trackingQueue ?: return
        val product: Product = createTopAdsProductForTracking(item)
        val irisSessionId = irisSession?.getSessionId() ?: ""
        val pageComponentId = presenter?.pageComponentId ?: ""

        TopAdsGtmTracker.getInstance().eventImpressionSearchResultProduct(
            trackingQueue,
            product,
            item.position,
            item.dimension90,
            queryKey,
            getUserId(),
            irisSessionId,
            item.topadsTag,
            item.getDimension115(additionalPositionMap),
            item.dimension131,
            pageComponentId,
        )
    }

    private fun createTopAdsProductForTracking(item: ProductItemDataView): Product {
        val product = Product()

        product.id = item.productID
        product.name = item.productName
        product.priceFormat = item.price
        product.category = Category(item.categoryID)
        product.freeOngkir = createTopAdsProductFreeOngkirForTracking(item)
        product.categoryBreadcrumb = item.categoryBreadcrumb ?: ""

        return product
    }

    private fun createTopAdsProductFreeOngkirForTracking(item: ProductItemDataView?): FreeOngkir {
        return if (item?.freeOngkirDataView != null) {
            FreeOngkir(
                    item.freeOngkirDataView.isActive,
                    item.freeOngkirDataView.imageUrl
            )
        } else FreeOngkir()
    }

    override fun onItemClicked(item: ProductItemDataView?, adapterPosition: Int) {
        presenter?.onProductClick(item, adapterPosition)
    }

    override fun sendTopAdsGTMTrackingProductClick(item: ProductItemDataView) {
        val product = createTopAdsProductForTracking(item)
        val pageComponentId = presenter?.pageComponentId ?: ""

        TopAdsGtmTracker.eventSearchResultProductClick(
            context,
            queryKey,
            product,
            item.position,
            getUserId(),
            item.dimension90,
            item.topadsTag,
            item.getDimension115(additionalPositionMap),
            item.dimension131,
            pageComponentId,
        )
    }

    override fun sendGTMTrackingProductClick(
        item: ProductItemDataView,
        userId: String,
        suggestedRelatedKeyword: String,
    ) {
        val eventLabel = getSearchProductTrackingEventLabel(item, suggestedRelatedKeyword)
        val filterSortParams = searchParameter?.let {
            getSortFilterParamsString(it.getSearchParameterMap() as Map<String?, Any?>)
        } ?: ""
        val pageComponentId = presenter?.pageComponentId ?: ""

        val productAnalyticsData = ProductClickAnalyticsData(
            isOrganicAds = item.isOrganicAds,
            topadsTag = item.topadsTag,
            filterSortParams = filterSortParams,
            componentId = pageComponentId,
        )
        SearchTracking.trackEventClickSearchResultProduct(
            item.getProductAsObjectDataLayer(
                filterSortParams,
                pageComponentId,
                additionalPositionMap,
            ),
            eventLabel,
            userId,
            productAnalyticsData,
        )
    }

    override fun routeToProductDetail(item: ProductItemDataView?, adapterPosition: Int) {
        item ?: return
        val intent = getProductIntent(item.productID, item.warehouseID, item.applink) ?: return

        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL)
    }

    private fun getProductIntent(productId: String, warehouseId: String, applink: String = ""): Intent? {
        return context?.let {
            when {
                applink.isNotEmpty() && RouteManager.isSupportApplink(it, applink) -> RouteManager.getIntent(it, applink)
                warehouseId.isNotEmpty() -> RouteManager.getIntent(
                    it,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_WAREHOUSE_ID,
                    productId,
                    warehouseId
                )
                else -> RouteManager.getIntent(
                    it,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    productId
                )
            }
        }
    }

    override fun onThreeDotsClick(item: ProductItemDataView?, adapterPosition: Int) {
        if (getSearchParameter() == null || item == null) return

        presenter?.onThreeDotsClick(item, adapterPosition)
    }

    override fun trackEventLongPress(productID: String) {
        SearchTracking.trackEventProductLongPress(queryKey, productID)
    }

    override fun showProductCardOptions(productCardOptionsModel: ProductCardOptionsModel) {
        showProductCardOptions(this, productCardOptionsModel)
    }

    override fun onAddToCartClick(item: ProductItemDataView) {
        SearchIdlingResource.increment()

        presenter?.onProductAddToCart(item)
    }

    override fun sendGTMTrackingProductATC(productItemDataView: ProductItemDataView?, cartId: String?) {
        if (productItemDataView == null) return

        val filterSortParams =
            getSortFilterParamsString(getSearchParameter()?.getSearchParameterMap() as Map<String?, Any?>)

        val products = arrayListOf(
            productItemDataView.getAtcObjectDataLayer(
                filterSortParams = filterSortParams,
                componentId = presenter?.pageComponentId ?: "",
                cartId
            )
        )

        AddToCartTracking.trackEventClickAddToCart(
            queryKey,
            productItemDataView.isAds,
            products
        )
    }

    override fun openAddToCartToaster(message: String, isSuccess: Boolean) {
        view?.let {
            Toaster.build(
                it,
                message,
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                if (isSuccess) getFragment().getString(R.string.search_see_cart) else "",
            ) {
                if (isSuccess) RouteManager.route(context, ApplinkConst.CART)
            }.show()
        }

        SearchIdlingResource.decrement()
    }

    override fun openVariantBottomSheet(data: ProductItemDataView) {
        atcVariantBottomSheetLauncher.launch(
            productId = data.productID,
            shopId = data.shopID,
            trackerCDListName = SearchTracking.getActionFieldString(
                data.isOrganicAds,
                data.topadsTag,
                presenter?.pageComponentId ?: "",
            ),
        ) {
            presenter?.trackProductClick(data)
            sendGTMTrackingProductATC(data, it.cartId)
        }

        SearchIdlingResource.decrement()
    }
    //endregion

    override fun redirectionStartActivity(applink: String?, url: String?) {
        if (!applink.isNullOrEmpty())
            redirectionListener?.startActivityWithApplink(applink.decodeQueryParameter())
        else
            redirectionListener?.startActivityWithUrl(url)
    }

    //region RecommendationItem (during empty state) impression, click, and 3 dots wishlist
    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        val intent = getProductIntent(item.productId.toString(), "0") ?: return

        if (presenter?.isUserLoggedIn == true)
            RecommendationTracking.eventClickProductRecommendationLogin(item, item.position.toString())
        else
            RecommendationTracking.eventClickProductRecommendationNonLogin(item, item.position.toString())

        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL)
    }

    override fun onProductImpression(item: RecommendationItem) {
        val trackingQueue = trackingQueue ?: return

        if (presenter?.isUserLoggedIn == true)
            RecommendationTracking.eventImpressionProductRecommendationLogin(trackingQueue, item, item.position.toString())
        else
            RecommendationTracking.eventImpressionProductRecommendationNonLogin(trackingQueue, item, item.position.toString())
    }

    override fun onWishlistV2Click(item: RecommendationItem, isAddWishlist: Boolean) { }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        if (getSearchParameter() == null || activity == null) return
        showProductCardOptions(this, createProductCardOptionsModel(item))
    }

    private fun createProductCardOptionsModel(item: RecommendationItem): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()

        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = item.isWishlist
        productCardOptionsModel.keyword = queryKey
        productCardOptionsModel.productId = item.productId.toString()
        productCardOptionsModel.isTopAds = item.isTopAds
        productCardOptionsModel.topAdsWishlistUrl = item.wishlistUrl
        productCardOptionsModel.isRecommendation = true

        return productCardOptionsModel
    }
    //endregion

    //region Quick Filter
    override fun isFilterSelected(option: Option?): Boolean {
        option ?: return false

        return filterController.getFilterViewState(option.uniqueId)
    }

    override fun onQuickFilterSelected(filter: Filter, option: Option, pageSource: String) {
        val isQuickFilterSelectedReversed = !isFilterSelected(option)
        setFilterToQuickFilterController(option, isQuickFilterSelectedReversed)

        val queryParams = filterController.getParameter() +
            originFilterMap() +
            componentIdMap(SearchSortFilterTracking.QUICK_FILTER_COMPONENT_ID) +
            manualFilterToggleMap()

        refreshSearchParameter(queryParams)

        lastFilterListenerDelegate.updateLastFilter()

        reloadData()

        trackEventSearchResultQuickFilter(
            option.key,
            option.value,
            isQuickFilterSelectedReversed,
            pageSource,
        )
    }

    private fun setFilterToQuickFilterController(option: Option, isQuickFilterSelected: Boolean) {
        if (option.isCategoryOption)
            filterController.setFilter(option, isQuickFilterSelected, true)
        else
            filterController.setFilter(option, isQuickFilterSelected)
    }

    private fun trackEventSearchResultQuickFilter(
        filterName: String,
        filterValue: String,
        isSelected: Boolean,
        pageSource: String,
    ) {
        SearchSortFilterTracking.trackEventClickQuickFilter(
            filterName,
            filterValue,
            isSelected,
            keyword = queryKey,
            pageSource = pageSource,
        )
    }

    override fun initFilterController(quickFilterList: List<Filter>) {
        filterController.initFilterController(searchParameter?.getSearchParameterHashMap(), quickFilterList)
    }

    override fun hideQuickFilterShimmering() {
        shimmeringView?.gone()
    }

    override fun setQuickFilter(items: List<SortFilterItem>) {
        searchSortFilter?.let {
            it.sortFilterItems.removeAllViews()
            it.visibility = View.VISIBLE
            it.sortFilterHorizontalScrollView.scrollX = 0
            it.addItem(items as ArrayList<SortFilterItem>)
            it.textView?.text = getString(R.string.search_filter)
            it.parentListener = { this.openBottomSheetFilterRevamp() }

            setSortFilterNewNotification(items)
        }
    }

    private fun setSortFilterNewNotification(items: List<SortFilterItem>) {
        val quickFilterList = presenter?.quickFilterList ?: listOf()
        for (i in items.indices) {
            if (i >= quickFilterList.size) break
            val item = items[i]
            val quickFilter = quickFilterList[i]

            sortFilterItemShowNew(item, isFilterHasNewOption(quickFilter))
        }
    }

    private fun isFilterHasNewOption(filter: Filter): Boolean = filter.options.all { it.isNew }

    private fun sortFilterItemShowNew(item: SortFilterItem, isNew: Boolean) {
        item.refChipUnify.showNewNotification = isNew
    }

    override fun configure(shouldRemove: Boolean) {
        if (shouldRemove)
            removeQuickFilterElevation(searchSortFilter)
        else
            applyQuickFilterElevation(context, searchSortFilter)
    }

    private fun hideSearchSortFilter() {
        searchSortFilter?.gone()
        shimmeringView?.visible()
    }

    override fun setSortFilterIndicatorCounter() {
        val searchParameter = searchParameter ?: return
        searchSortFilter?.indicatorCounter = getSortFilterCount(searchParameter.getSearchParameterMap())
    }
    //endregion

    override fun reloadData() {
        val searchParameter = getSearchParameter() ?: return

        showRefreshLayout()

        onBoardingListenerDelegate.dismissCoachmark()
        presenter?.clearData()
        recyclerViewUpdater.productListAdapter?.clearData()
        productVideoAutoplay.stopVideoAutoplay()

        hideSearchSortFilter()

        SearchIdlingResource.increment()
        presenter?.loadData(searchParameter.getSearchParameterMap())
    }

    //region Change product card layout
    override fun setDefaultLayoutType(defaultView: Int) {
        changeView.change(defaultView)
    }

    override fun setProductGridType(productGridType: ProductGridType) {
        changeView.onProductGridTypeChanged(productGridType) {
            changeRecyclerViewLayoutManager(it)
        }
    }

    private fun changeRecyclerViewLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        val oldLoadMoreListener = gridLayoutLoadMoreTriggerListener
        initLoadMoreListener()
        recyclerViewUpdater.changeLayoutManager(
            layoutManager,
            listOf(oldLoadMoreListener),
            listOf(gridLayoutLoadMoreTriggerListener),
        )
    }
    //endregion

    override fun backToTop() {
        recyclerViewUpdater.backToTop()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    override fun getScreenName(): String {
        return getScreenNameId()
    }

    private fun getScreenNameId() = SCREEN_SEARCH_PAGE_PRODUCT_TAB

    override fun setAutocompleteApplink(autocompleteApplink: String?) {
        redirectionListener?.setAutocompleteApplink(autocompleteApplink)
    }

    override fun updateScrollListener() {
        gridLayoutLoadMoreTriggerListener?.updateStateAfterGetData()

        tryForceLoadNextPage()
    }

    private fun tryForceLoadNextPage() {
        val recyclerView = recyclerViewUpdater.recyclerView ?: return

        recyclerView.post {
            if (!recyclerView.canScrollVertically(1))
                gridLayoutLoadMoreTriggerListener?.loadMoreNextPage()
        }
    }

    override fun showAdultRestriction() {
        AdultManager.showAdultPopUp(this, AdultManager.ORIGIN_SEARCH_PAGE, queryKey)
    }

    override fun redirectSearchToAnotherPage(applink: String?) {
        redirectionListener?.startActivityWithApplink(applink)
        activity?.finish()
    }

    private fun getUserId() = presenter?.userId ?: "0"

    override val abTestRemoteConfig: RemoteConfig?
        get() = RemoteConfigInstance.getInstance().abTestPlatform

    override fun logWarning(message: String?, throwable: Throwable?) {
        SearchLogger().logWarning(message, throwable)
    }

    override val className: String
        get() = activity?.javaClass?.name ?: ""

    //region Tracking General Search (tracker after finished get product list from BE)
    override fun sendTrackingEventAppsFlyerViewListingSearch(
        afProdIds: JSONArray?,
        query: String?,
        prodIdArray: ArrayList<String?>?,
        allProdIdArray: ArrayList<String?>?
    ) {
        SearchTracking.eventAppsFlyerViewListingSearch(afProdIds!!, query!!, prodIdArray!!, allProdIdArray)
    }

    override fun sendTrackingEventMoEngageSearchAttempt(
            query: String?,
            hasProductList: Boolean,
            category: HashMap<String?, String?>?,
    ) {
        SearchTracking.trackMoEngageSearchAttempt(query, hasProductList, category)
    }

    override fun sendTrackingGTMEventSearchAttempt(generalSearchTrackingModel: GeneralSearchTrackingModel) {
        SearchTracking.trackGTMEventSearchAttempt(generalSearchTrackingModel)
    }
    //endregion

    //region Filter related
    override val isAnyFilterActive: Boolean
        get() = filterController.isFilterActive()

    override val isAnySortActive: Boolean
        get() {
            val mapParameter = searchParameter?.getSearchParameterMap() ?: mapOf()
            return !isSortHasDefaultValue(mapParameter)
        }

    override fun setAutoFilterToggle(autoFilterParameter: String) {
        val autoFilterParameterMap = autoFilterParameter.toMapParam()
        val searchParameter = searchParameter?.getSearchParameterHashMap() ?: mapOf()

        val autoFilterSearchParameterMap = searchParameter + autoFilterParameterMap

        refreshSearchParameter(autoFilterSearchParameterMap)
    }

    override fun refreshSearchParameter(queryParams: Map<String, String>) {
        searchParameter?.resetParams(queryParams)
        searchNavigationListener?.updateSearchParameter(searchParameter)
        filterController.refreshMapParameter(queryParams)
    }

    private fun SearchParameter.resetParams(queryParams: Map<String, String>) {
        getSearchParameterHashMap().clear()
        getSearchParameterHashMap().putAll(queryParams)
    }
    //endregion

    //region Product Item Position (probably not used anymore)
    override fun clearLastProductItemPositionFromCache() {
        activity?.applicationContext?.let {
            LocalCacheHandler.clearCache(it, SEARCH_RESULT_ENHANCE_ANALYTIC)
        }
    }

    override fun saveLastProductItemPositionToCache(lastProductItemPositionToCache: Int) {
        activity?.applicationContext?.let {
            val cache = LocalCacheHandler(it, SEARCH_RESULT_ENHANCE_ANALYTIC)
            cache.putInt(LAST_POSITION_ENHANCE_PRODUCT, lastProductItemPositionToCache)
            cache.applyEditor()
        }
    }

    override val lastProductItemPositionFromCache: Int
        get() = activity?.applicationContext?.let {
                val cache = LocalCacheHandler(it, SEARCH_RESULT_ENHANCE_ANALYTIC)
                return cache.getInt(LAST_POSITION_ENHANCE_PRODUCT, 0)
            } ?: 0
    //endregion

    //region on boarding / coachmark
    override fun showOnBoarding(firstProductPosition: Int) {
        onBoardingListenerDelegate.showProductWithBOEOnBoarding(firstProductPosition)
    }

    override fun enableProductViewTypeOnBoarding() {
        onBoardingListenerDelegate.enableProductViewTypeCoachmark()
    }

    //endregion

    //region Bottom Sheet Filter
    private fun openBottomSheetFilterRevamp() {
        presenter?.openFilterPage(getSearchParameter()?.getSearchParameterMap())
    }
    //endregion

    override fun onLocalizingAddressSelected() {
        presenter?.onLocalizingAddressSelected()
    }

    //region dropdown quick filter
    override fun openBottomsheetMultipleOptionsQuickFilter(filter: Filter) {
        val filterDetailCallback = object: FilterGeneralDetailBottomSheet.Callback {
            override fun onApplyButtonClicked(optionList: List<Option>?) {
                presenter?.onApplyDropdownQuickFilter(optionList)
            }
        }

        setupActiveOptionsQuickFilter(filter)

        FilterGeneralDetailBottomSheet().show(
            parentFragmentManager,
            filter,
            filterDetailCallback,
            getString(R.string.search_quick_filter_dropdown_apply_button_text)
        )
    }

    private fun setupActiveOptionsQuickFilter(filter: Filter) {
        filter.options.forEach {
            it.inputState = isFilterSelected(it).toString()
        }
    }

    override fun applyDropdownQuickFilter(optionList: List<Option>?) {
        filterController.setFilter(optionList)

        val queryParams = filterController.getParameter() +
            originFilterMap() +
            componentIdMap(SearchSortFilterTracking.DROPDOWN_QUICK_FILTER_COMPONENT_ID) +
            manualFilterToggleMap()

        refreshSearchParameter(queryParams)

        lastFilterListenerDelegate.updateLastFilter()

        reloadData()
    }

    override fun trackEventApplyDropdownQuickFilter(
        optionList: List<Option>?,
        pageSource: String,
    ) {
        SearchSortFilterTracking.trackEventApplyDropdownQuickFilter(
            optionList,
            keyword = queryKey,
            pageSource = pageSource,
        )
    }

    //endregion

    override fun updateSearchBarNotification() {
        searchNavigationListener?.updateSearchBarNotification()
    }
}

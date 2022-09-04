package com.tokopedia.search.result.presentation.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.getSortFilterCount
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.filter.common.helper.isSortHasDefaultValue
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.iris.Iris
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.productcard.ProductCardLifecycleObserver
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_MPC_LIFECYCLE_OBSERVER
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_PRODUCT_CARD_VIEWSTUB
import com.tokopedia.search.R
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.analytics.ProductClickAnalyticsData
import com.tokopedia.search.analytics.RecommendationTracking
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.di.module.SearchContextModule
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.presentation.view.listener.BroadMatchListener
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.result.presentation.view.listener.QuickFilterElevation
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationClickListener
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener
import com.tokopedia.search.result.presentation.view.listener.TickerListener
import com.tokopedia.search.result.presentation.view.listener.TopAdsImageViewListener
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactoryImpl
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.banner.BannerListenerDelegate
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressListener
import com.tokopedia.search.result.product.cpm.BannerAdsListenerDelegate
import com.tokopedia.search.result.product.cpm.BannerAdsPresenter
import com.tokopedia.search.result.product.emptystate.EmptyStateListenerDelegate
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavListenerDelegate
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetListenerDelegate
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView
import com.tokopedia.search.result.product.lastfilter.LastFilterListenerDelegate
import com.tokopedia.search.result.product.onboarding.OnBoardingListenerDelegate
import com.tokopedia.search.result.product.performancemonitoring.PerformanceMonitoringModule
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaListenerDelegate
import com.tokopedia.search.result.product.videowidget.VideoCarouselListenerDelegate
import com.tokopedia.search.result.product.violation.ViolationListenerDelegate
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.SearchLogger
import com.tokopedia.search.utils.SmallGridSpanCount
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.search.utils.addFilterOrigin
import com.tokopedia.search.utils.applyQuickFilterElevation
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.search.utils.removeQuickFilterElevation
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.Category
import com.tokopedia.topads.sdk.domain.model.FreeOngkir
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.video_widget.VideoPlayerAutoplay
import com.tokopedia.video_widget.carousel.VideoCarouselWidgetCoordinator
import com.tokopedia.video_widget.util.networkmonitor.DefaultNetworkMonitor
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import org.json.JSONArray
import javax.inject.Inject
import com.tokopedia.wishlist_common.R as Rwishlist

class ProductListFragment: BaseDaggerFragment(),
    ProductListSectionContract.View,
    ProductListener,
    TickerListener,
    SuggestionListener,
    RecommendationListener,
    InspirationCarouselListener,
    BroadMatchListener,
    QuickFilterElevation,
    SortFilterBottomSheet.Callback,
    SearchNavigationClickListener,
    TopAdsImageViewListener,
    ChooseAddressListener,
    ProductListParameterListener,
    QueryKeyProvider,
    SearchParameterProvider,
    FragmentProvider,
    ClassNameProvider {

    companion object {
        private const val SCREEN_SEARCH_PAGE_PRODUCT_TAB = "Search result - Product tab"
        private const val REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123
        private const val SEARCH_RESULT_ENHANCE_ANALYTIC = "SEARCH_RESULT_ENHANCE_ANALYTIC"
        private const val LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT"
        private const val EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER"
        private const val REQUEST_CODE_LOGIN = 561
        private const val CLICK_TYPE_WISHLIST = "&click_type=wishlist"

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
    lateinit var inspirationCarouselTrackingUnification: InspirationCarouselTrackingUnification

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
    lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    private var refreshLayout: SwipeRefreshLayout? = null
    private var staggeredGridLayoutLoadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var searchNavigationListener: SearchNavigationListener? = null
    private var performanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var redirectionListener: RedirectionListener? = null
    private var searchParameter: SearchParameter? = null
    private var irisSession: IrisSession? = null
    private var searchSortFilter: SortFilter? = null
    private var shimmeringView: LinearLayout? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private val filterTrackingData by lazy {
        FilterTrackingData(
                FilterEventTracking.Event.CLICK_SEARCH_RESULT,
                FilterEventTracking.Category.FILTER_PRODUCT,
                "",
                FilterEventTracking.Category.PREFIX_SEARCH_RESULT_PAGE
        )
    }
    private var coachMark: CoachMark2? = null

    override val carouselRecycledViewPool = RecyclerView.RecycledViewPool()
    override var productCardLifecycleObserver: ProductCardLifecycleObserver? = null
        private set

    private val productVideoAutoplay : VideoPlayerAutoplay by lazy {
        VideoPlayerAutoplay(remoteConfig)
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
            searchParameter = SearchParameter(searchParameterToCopy)
        }
    }

    private fun initProductCardLifecycleObserver() {
        if (!remoteConfig.getBoolean(ENABLE_MPC_LIFECYCLE_OBSERVER)) return

        ProductCardLifecycleObserver().also {
            productCardLifecycleObserver = it
            lifecycle.addObserver(it)
        }
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

        if (userVisibleHint) {
            setupSearchNavigation()
        }
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
        val layoutManager = staggeredGridLayoutManager ?: return
        staggeredGridLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
    }

    private fun getEndlessRecyclerViewListener(
        recyclerViewLayoutManager: RecyclerView.LayoutManager,
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                val searchParameterMap = searchParameter?.getSearchParameterMap() ?: return
                presenter?.loadMoreData(searchParameterMap)
            }
        }
    }

    private fun setupRecyclerView(rootView: View) {
        recyclerViewUpdater.initialize(
            rootView.findViewById(R.id.recyclerview),
            staggeredGridLayoutManager,
            staggeredGridLayoutLoadMoreTriggerListener,
            onBoardingListenerDelegate.createScrollListener(),
            createProductListTypeFactory(),
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
            inspirationCarouselTrackingUnification,
            this,
            this
        )

        return ProductListTypeFactoryImpl(
            fragmentProvider = this,
            productListener = this,
            tickerListener = this,
            suggestionListener = this,
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
            inspirationCarouselListener = this,
            broadMatchListener = this,
            inspirationCardListener = inspirationWidgetListenerDelegate,
            searchInTokopediaListener = SearchInTokopediaListenerDelegate(activity),
            searchNavigationListener = this,
            topAdsImageViewListener = this,
            chooseAddressListener = this,
            bannerListener = BannerListenerDelegate(iris, activity),
            lastFilterListener = lastFilterListenerDelegate,
            inspirationSizeListener = inspirationWidgetListenerDelegate,
            violationListener = ViolationListenerDelegate(activity),
            videoCarouselListener = videoCarouselListenerDelegate,
            videoCarouselWidgetCoordinator = videoCarouselWidgetCoordinator,
            networkMonitor = networkMonitor,
            isUsingViewStub = remoteConfig.getBoolean(ENABLE_PRODUCT_CARD_VIEWSTUB),
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

    private fun getDimension90(): String =
        Dimension90Utils.getDimension90(getSearchParameterMap())

    private fun getSearchParameterMap(): Map<String, Any> =
        searchParameter?.getSearchParameterMap() ?: mapOf()
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

    //region Setup Search Navigation (layout switching small grid, list, and big grid)
    override fun setupSearchNavigation() {
        searchNavigationListener?.setupSearchNavigation(object : SearchNavigationListener.ClickListener {
            override fun onChangeGridClick() {
                switchLayoutType()
            }
        })
        refreshMenuItemGridIcon()
    }

    private fun switchLayoutType() {
        val productListAdapter = recyclerViewUpdater.productListAdapter ?: return
        if (!userVisibleHint) return

        when (productListAdapter.getCurrentLayoutType()) {
            SearchConstant.ViewType.LIST -> {
                switchLayoutTypeTo(SearchConstant.ViewType.BIG_GRID)
                SearchTracking.eventSearchResultChangeGrid("grid 1", screenName)
            }
            SearchConstant.ViewType.SMALL_GRID -> {
                switchLayoutTypeTo(SearchConstant.ViewType.LIST)
                SearchTracking.eventSearchResultChangeGrid("list", screenName)
            }
            SearchConstant.ViewType.BIG_GRID -> {
                switchLayoutTypeTo(SearchConstant.ViewType.SMALL_GRID)
                SearchTracking.eventSearchResultChangeGrid("grid 2", screenName)
            }
        }
    }

    private fun switchLayoutTypeTo(layoutType: SearchConstant.ViewType) {
        val productListAdapter = recyclerViewUpdater.productListAdapter ?: return
        if (!userVisibleHint) return

        when (layoutType) {
            SearchConstant.ViewType.LIST -> {
                staggeredGridLayoutManager.spanCount = 1
                productListAdapter.changeListView()
            }
            SearchConstant.ViewType.SMALL_GRID -> {
                staggeredGridLayoutManager.spanCount = smallGridSpanCount()
                productListAdapter.changeDoubleGridView()
            }
            SearchConstant.ViewType.BIG_GRID -> {
                staggeredGridLayoutManager.spanCount = 1
                productListAdapter.changeSingleGridView()
            }
        }

        refreshMenuItemGridIcon()
    }

    private fun refreshMenuItemGridIcon() {
        val productListAdapter = recyclerViewUpdater.productListAdapter ?: return

        searchNavigationListener?.refreshMenuItemGridIcon(
                productListAdapter.getTitleTypeRecyclerView(),
                productListAdapter.getIconTypeRecyclerView()
        )
    }
    //endregion

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
    }

    override fun setProductList(list: List<Visitable<*>>) {
        recyclerViewUpdater.setItems(list)
    }

    override fun addRecommendationList(list: List<Visitable<*>>) {
        recyclerViewUpdater.appendItems(list)
    }

    override fun setBannedProductsErrorMessage(bannedProductsErrorMessageAsList: List<Visitable<*>>) {
        recyclerViewUpdater.appendItems(bannedProductsErrorMessageAsList)
    }

    override fun addLoading() {
        recyclerViewUpdater.addLoading()
    }

    override fun removeLoading() {
        removeSearchPageLoading()
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
                presenter?.loadMoreData(searchParameter.getSearchParameterMap())
            }
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity) {
                addLoading()
                presenter?.loadMoreData(searchParameter.getSearchParameterMap())
            }.showRetrySnackbar()
        }
    }
    //endregion

    //region onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_GOTO_PRODUCT_DETAIL) {
            val wishlistUpdatedPosition = data?.extras?.getInt(
                    SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION,
                    -1
            ) ?: -1

            val isWishlist = data?.extras?.getBoolean(
                    SearchConstant.Wishlist.WISHLIST_STATUS_IS_WISHLIST,
                    false
            ) ?: false

            if (wishlistUpdatedPosition != -1)
                updateWishlistFromPDP(wishlistUpdatedPosition, isWishlist)
        }

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
        }
    }

    private fun updateWishlistFromPDP(position: Int, isWishlist: Boolean) {
        val productListAdapter = recyclerViewUpdater.productListAdapter ?: return

        val isProductOrRecommendation =
                productListAdapter.isProductItem(position) || productListAdapter.isRecommendationItem(position)

        if (isProductOrRecommendation)
            productListAdapter.updateWishlistStatus(position, isWishlist)
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        presenter?.handleWishlistAction(productCardOptionsModel)
    }
    //endregion

    override fun onPause() {
        super.onPause()

        coachMark?.dismissCoachMark()
        trackingQueue?.sendAll()
    }

    //region product item (organic and topads) impression, click, and 3 dots click
    override fun onProductImpressed(item: ProductItemDataView?, adapterPosition: Int) {
        presenter?.onProductImpressed(item, adapterPosition)
    }

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
            item.dimension115,
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
        product.categoryBreadcrumb = item.categoryBreadcrumb

        return product
    }

    private fun createTopAdsProductFreeOngkirForTracking(item: ProductItemDataView?): FreeOngkir? {
        return if (item?.freeOngkirDataView != null) {
            FreeOngkir(
                    item.freeOngkirDataView.isActive,
                    item.freeOngkirDataView.imageUrl
            )
        } else null
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
            item.dimension115,
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
            item.getProductAsObjectDataLayer(filterSortParams, pageComponentId),
            eventLabel,
            userId,
            productAnalyticsData,
        )
    }

    override fun routeToProductDetail(item: ProductItemDataView?, adapterPosition: Int) {
        item ?: return
        val intent = getProductIntent(item.productID, item.warehouseID, item.applink) ?: return

        intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition)
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
        intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, item.position)

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

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {

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

    //region Ticker
    override fun onTickerImpressed(tickerDataView: TickerDataView) {
        tickerDataView.impress(iris)
    }

    override fun onTickerClicked(tickerDataView: TickerDataView) {
        tickerDataView.click(TrackApp.getInstance().gtm)

        applyParamsFromTicker(UrlParamUtils.getParamMap(tickerDataView.query))
    }

    private fun applyParamsFromTicker(tickerParams: HashMap<String?, String?>) {
        val params = HashMap(filterController.getParameter().addFilterOrigin())
        params.putAll(tickerParams)

        refreshSearchParameter(params)

        reloadData()
    }

    override fun onTickerDismissed() {
        presenter?.onPriceFilterTickerDismissed()
        recyclerViewUpdater.productListAdapter?.removePriceFilterTicker()
    }

    override val isTickerHasDismissed
        get() = presenter?.isTickerHasDismissed ?: false
    //endregion

    //region Suggestion
    override fun onSuggestionImpressed(suggestionDataView: SuggestionDataView) {
        suggestionDataView.impress(iris)
    }

    override fun onSuggestionClicked(suggestionDataView: SuggestionDataView) {
        suggestionDataView.click(TrackApp.getInstance().gtm)
        performNewProductSearch(suggestionDataView.suggestedQuery)
    }

    private fun performNewProductSearch(queryParams: String) {
        val applinkToSearchResult = ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + queryParams
        val modifiedApplinkToSearchResult = modifyApplinkToSearchResult(applinkToSearchResult)

        redirectionListener?.startActivityWithApplink(modifiedApplinkToSearchResult)
    }
    //endregion

    override fun modifyApplinkToSearchResult(applink: String): String {
        val urlParser = URLParser(applink)

        val params = urlParser.paramKeyValueMap
        params[SearchApiConst.PREVIOUS_KEYWORD] = queryKey

        return ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + UrlParamUtils.generateUrlParamString(params)
    }

    //region Quick Filter
    override fun isFilterSelected(option: Option?): Boolean {
        option ?: return false

        return filterController.getFilterViewState(option.uniqueId)
    }

    override fun onQuickFilterSelected(filter: Filter, option: Option) {
        val isQuickFilterSelectedReversed = !isFilterSelected(option)
        setFilterToQuickFilterController(option, isQuickFilterSelectedReversed)

        val queryParams = filterController.getParameter().addFilterOrigin()
        refreshSearchParameter(queryParams)

        lastFilterListenerDelegate.updateLastFilter()

        reloadData()

        trackEventSearchResultQuickFilter(option.key, option.value, isQuickFilterSelectedReversed)
    }

    private fun setFilterToQuickFilterController(option: Option, isQuickFilterSelected: Boolean) {
        if (option.isCategoryOption)
            filterController.setFilter(option, isQuickFilterSelected, true)
        else
            filterController.setFilter(option, isQuickFilterSelected)
    }

    private fun trackEventSearchResultQuickFilter(filterName: String, filterValue: String, isSelected: Boolean) {
        SearchTracking.trackEventClickQuickFilter(filterName, filterValue, isSelected, getUserId())
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

    private fun setSortFilterIndicatorCounter() {
        val searchParameter = searchParameter ?: return
        searchSortFilter?.indicatorCounter = getSortFilterCount(searchParameter.getSearchParameterMap())
    }
    //endregion

    //region banned products
    override fun trackEventImpressionBannedProducts(isEmptySearch: Boolean) {
        if (isEmptySearch)
            SearchTracking.trackEventImpressionBannedProductsEmptySearch(queryKey)
        else
            SearchTracking.trackEventImpressionBannedProductsWithResult(queryKey)
    }
    //endregion

    override fun reloadData() {
        val searchParameter = getSearchParameter() ?: return

        showRefreshLayout()

        coachMark?.dismissCoachMark()
        presenter?.clearData()
        recyclerViewUpdater.productListAdapter?.clearData()
        productVideoAutoplay.stopVideoAutoplay()

        hideSearchSortFilter()

        presenter?.loadData(searchParameter.getSearchParameterMap())

        setSortFilterIndicatorCounter()
    }

    //region Change product card layout
    override fun setDefaultLayoutType(defaultView: Int) {
        when (defaultView) {
            SearchConstant.DefaultViewType.SMALL_GRID -> switchLayoutTypeTo(SearchConstant.ViewType.SMALL_GRID)
            SearchConstant.DefaultViewType.LIST -> switchLayoutTypeTo(SearchConstant.ViewType.LIST)
            else -> switchLayoutTypeTo(SearchConstant.ViewType.SMALL_GRID)
        }
    }

    override fun onChangeViewClicked(position: Int) {
        val currentLayoutType = recyclerViewUpdater.productListAdapter?.getCurrentLayoutType()
            ?: SearchConstant.ViewType.SMALL_GRID
        presenter?.handleChangeView(position, currentLayoutType)
    }

    override fun trackEventSearchResultChangeView(viewType: String) {
        SearchTracking.eventSearchResultChangeGrid(viewType, screenName)
    }

    override fun switchSearchNavigationLayoutTypeToListView(position: Int) {
        if (!userVisibleHint) return

        staggeredGridLayoutManager.spanCount = 1
        recyclerViewUpdater.productListAdapter?.changeSearchNavigationListView(position)
    }

    override fun switchSearchNavigationLayoutTypeToBigGridView(position: Int) {
        if (!userVisibleHint) return

        staggeredGridLayoutManager.spanCount = 1
        recyclerViewUpdater.productListAdapter?.changeSearchNavigationSingleGridView(position)
    }


    override fun switchSearchNavigationLayoutTypeToSmallGridView(position: Int) {
        if (!userVisibleHint) return

        staggeredGridLayoutManager.spanCount = smallGridSpanCount()
        recyclerViewUpdater.productListAdapter?.changeSearchNavigationDoubleGridView(position)
    }
    //endregion

    override fun backToTop() {
        smoothScrollRecyclerView()
    }

    private fun smoothScrollRecyclerView() {
        recyclerViewUpdater.recyclerView?.smoothScrollToPosition(0)
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

    private fun removeSearchPageLoading() {
        if (isFirstActiveTab)
            searchNavigationListener?.removeSearchPageLoading()
    }

    override fun setAutocompleteApplink(autocompleteApplink: String?) {
        redirectionListener?.setAutocompleteApplink(autocompleteApplink)
    }

    override fun updateScrollListener() {
        staggeredGridLayoutLoadMoreTriggerListener?.updateStateAfterGetData()

        tryForceLoadNextPage()
    }

    private fun tryForceLoadNextPage() {
        val recyclerView = recyclerViewUpdater.recyclerView ?: return

        recyclerView.post {
            if (!recyclerView.canScrollVertically(1))
                staggeredGridLayoutLoadMoreTriggerListener?.loadMoreNextPage()
        }
    }

    override fun launchLoginActivity(productId: String?) {
        val extras = Bundle().apply {
            putString("product_id", productId)
        }

        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN).apply {
            putExtras(extras)
        }

        startActivityForResult(intent, REQUEST_CODE_LOGIN)
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

    //region Inspiration Carousel
    override fun onInspirationCarouselInfoProductClicked(product: InspirationCarouselDataView.Option.Product) {
        redirectionStartActivity(product.applink, product.url)

        val products = ArrayList<Any>()
        products.add(product.getInspirationCarouselInfoProductAsObjectDataLayer())

        SearchTracking.trackEventClickInspirationCarouselInfoProduct(product.inspirationCarouselType, queryKey, products)
    }

    override fun onInspirationCarouselSeeAllClicked(
        inspirationCarouselDataViewOption: InspirationCarouselDataView.Option,
    ) {
        redirectionStartActivity(inspirationCarouselDataViewOption.applink, inspirationCarouselDataViewOption.url)

        inspirationCarouselTrackingUnification.trackCarouselClickSeeAll(
            queryKey,
            inspirationCarouselDataViewOption,
        )
    }

    override fun onInspirationCarouselGridBannerClicked(option: InspirationCarouselDataView.Option) {
        redirectionStartActivity(option.bannerApplinkUrl, option.bannerLinkUrl)

        SearchTracking.trackEventClickInspirationCarouselGridBanner(
                option.inspirationCarouselType, queryKey, option.getBannerDataLayer(queryKey), getUserId()
        )
    }

    override fun onImpressedInspirationCarouselInfoProduct(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue ?: return

        val products = ArrayList<Any>()
        products.add(product.getInspirationCarouselInfoProductAsObjectDataLayer())

        SearchTracking.trackImpressionInspirationCarouselInfo(
                trackingQueue,
                product.inspirationCarouselType,
                queryKey,
                products
        )
    }

    override fun onInspirationCarouselListProductImpressed(
        product: InspirationCarouselDataView.Option.Product
    ) {
        presenter?.onInspirationCarouselProductImpressed(product)
    }

    override fun onInspirationCarouselListProductClicked(
        product: InspirationCarouselDataView.Option.Product
    ) {
        presenter?.onInspirationCarouselProductClick(product)
    }

    override fun trackEventImpressionInspirationCarouselGridItem(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue ?: return
        val data = createCarouselTrackingUnificationData(product, searchParameter)

        inspirationCarouselTrackingUnification.trackCarouselImpression(trackingQueue, data)
    }

    override fun trackEventImpressionInspirationCarouselListItem(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue ?: return
        val data = createCarouselTrackingUnificationData(product, searchParameter)

        inspirationCarouselTrackingUnification.trackCarouselImpression(trackingQueue, data)
    }

    override fun trackEventClickInspirationCarouselGridItem(product: InspirationCarouselDataView.Option.Product) {
        val data = createCarouselTrackingUnificationData(product, searchParameter)

        inspirationCarouselTrackingUnification.trackCarouselClick(data)
    }

    override fun trackEventClickInspirationCarouselListItem(product: InspirationCarouselDataView.Option.Product) {
        val data = createCarouselTrackingUnificationData(product, searchParameter)

        inspirationCarouselTrackingUnification.trackCarouselClick(data)
    }

    override fun onInspirationCarouselGridProductImpressed(
        product: InspirationCarouselDataView.Option.Product
    ) {
        presenter?.onInspirationCarouselProductImpressed(product)
    }

    override fun onInspirationCarouselGridProductClicked(
        product: InspirationCarouselDataView.Option.Product
    ) {
        presenter?.onInspirationCarouselProductClick(product)
    }

    override fun trackEventImpressionInspirationCarouselChipsItem(product: InspirationCarouselDataView.Option.Product) {
        val data = createCarouselTrackingUnificationData(product, searchParameter)
        val trackingQueue = trackingQueue ?: return

        inspirationCarouselTrackingUnification.trackCarouselImpression(trackingQueue, data)
    }

    override fun trackEventClickInspirationCarouselChipsItem(product: InspirationCarouselDataView.Option.Product) {
        val data = createCarouselTrackingUnificationData(product, searchParameter)

        inspirationCarouselTrackingUnification.trackCarouselClick(data)
    }

    override fun onInspirationCarouselChipsProductClicked(
        product: InspirationCarouselDataView.Option.Product
    ) {
        presenter?.onInspirationCarouselProductClick(product)
    }

    override fun onImpressedInspirationCarouselChipsProduct(
        product: InspirationCarouselDataView.Option.Product,
    ) {
        presenter?.onInspirationCarouselProductImpressed(product)
    }

    override fun onInspirationCarouselChipsSeeAllClicked(
        inspirationCarouselDataViewOption: InspirationCarouselDataView.Option,
    ) {
        redirectionStartActivity(
            inspirationCarouselDataViewOption.applink,
            inspirationCarouselDataViewOption.url
        )

        inspirationCarouselTrackingUnification.trackCarouselClickSeeAll(
            queryKey,
            inspirationCarouselDataViewOption,
        )
    }

    override fun onInspirationCarouselChipsClicked(
        inspirationCarouselAdapterPosition: Int,
        inspirationCarouselViewModel: InspirationCarouselDataView,
        inspirationCarouselOption: InspirationCarouselDataView.Option,
    ) {
        presenter?.onInspirationCarouselChipsClick(
            adapterPosition = inspirationCarouselAdapterPosition,
            inspirationCarouselViewModel = inspirationCarouselViewModel,
            clickedInspirationCarouselOption = inspirationCarouselOption,
            searchParameter = getSearchParameter()?.getSearchParameterMap() ?: mapOf()
        )
    }

    override fun trackInspirationCarouselChipsClicked(option: InspirationCarouselDataView.Option) {
        inspirationCarouselTrackingUnification.trackCarouselClickSeeAll(queryKey, option)
    }

    override fun trackDynamicProductCarouselImpression(
        dynamicProductCarousel: BroadMatchItemDataView,
        type: String,
        inspirationCarouselProduct: InspirationCarouselDataView.Option.Product
    ) {
        val trackingQueue = trackingQueue ?: return
        val data = createCarouselTrackingUnificationData(inspirationCarouselProduct, searchParameter)

        inspirationCarouselTrackingUnification.trackCarouselImpression(trackingQueue, data)
    }

    override fun trackDynamicProductCarouselClick(
        dynamicProductCarousel: BroadMatchItemDataView,
        type: String,
        inspirationCarouselProduct: InspirationCarouselDataView.Option.Product,
    ) {
        val data = createCarouselTrackingUnificationData(inspirationCarouselProduct, searchParameter)

        inspirationCarouselTrackingUnification.trackCarouselClick(data)
    }

    override fun trackEventClickSeeMoreDynamicProductCarousel(
        dynamicProductCarousel: BroadMatchDataView,
        type: String,
        inspirationCarouselOption: InspirationCarouselDataView.Option
    ) {
        inspirationCarouselTrackingUnification.trackCarouselClickSeeAll(
            queryKey,
            inspirationCarouselOption,
        )
    }
    //endregion

    //region Wishlist
    override fun trackWishlistRecommendationProductLoginUser(isAddWishlist: Boolean) {
        RecommendationTracking.eventUserClickProductToWishlistForUserLogin(isAddWishlist)
    }

    override fun trackWishlistRecommendationProductNonLoginUser() {
        RecommendationTracking.eventUserClickProductToWishlistForNonLogin()
    }

    override fun trackWishlistProduct(wishlistTrackingModel: WishlistTrackingModel) {
        SearchTracking.eventSuccessWishlistSearchResultProduct(wishlistTrackingModel)
    }

    override fun updateWishlistStatus(productId: String?, isWishlisted: Boolean) {
        recyclerViewUpdater.productListAdapter?.updateWishlistStatus(productId!!, isWishlisted)
    }

    override fun showMessageSuccessWishlistAction(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        val view = view ?: return

        if (wishlistResult.isUsingWishlistV2) {
            if (wishlistResult.isAddWishlist) {
                context?.let {
                    AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(wishlistResult, it, view)
                }
            } else {
                context?.let {
                    AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(wishlistResult, it, view)
                }
            }
        } else {
            if (wishlistResult.isAddWishlist)
                Toaster.build(view, getString(R.string.msg_add_wishlist),
                    Snackbar.LENGTH_SHORT, TYPE_NORMAL, getString(R.string.cta_add_wishlist))
                { goToWishlistPage() }.show()
            else
                Toaster.build(view, getString(R.string.msg_remove_wishlist),
                    Snackbar.LENGTH_SHORT, TYPE_NORMAL).show()
        }
    }

    override fun hitWishlistClickUrl(productCardOptionsModel: ProductCardOptionsModel) {
        context?.let {
            TopAdsUrlHitter(it).hitClickUrl(
                this::class.java.simpleName,
                productCardOptionsModel.topAdsClickUrl+ CLICK_TYPE_WISHLIST,
                productCardOptionsModel.productId,
                productCardOptionsModel.productName,
                productCardOptionsModel.productImageUrl
            )
        }
    }

    private fun goToWishlistPage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.NEW_WISHLIST)
        startActivity(intent)
    }

    override fun showMessageFailedWishlistAction(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        val view = view ?: return

        if (wishlistResult.isUsingWishlistV2) {
            val errorMessage = if (wishlistResult.messageV2.isNotEmpty()) {
                wishlistResult.messageV2
            } else if (wishlistResult.isAddWishlist) {
                getString(Rwishlist.string.on_failed_add_to_wishlist_msg)
            } else {
                getString(Rwishlist.string.on_failed_remove_from_wishlist_msg)
            }

            val ctaText = wishlistResult.ctaTextV2.ifEmpty { "" }

            context?.let {
                AddRemoveWishlistV2Handler.showWishlistV2ErrorToasterWithCta(errorMessage, ctaText,
                    wishlistResult.ctaActionV2, view, it)
            }

        } else {
            if (wishlistResult.isAddWishlist)
                Toaster.build(view, ErrorHandler.getErrorMessage(context,
                    MessageErrorException(getString(R.string.msg_add_wishlist_failed))),
                    Snackbar.LENGTH_SHORT, TYPE_ERROR).show()
            else
                Toaster.build(view, ErrorHandler.getErrorMessage(context,
                    MessageErrorException(getString(R.string.msg_remove_wishlist_failed))),
                    Snackbar.LENGTH_SHORT, TYPE_ERROR).show()
        }
    }
    //endregion

    //region Broad Match
    override fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView) {
        presenter?.onBroadMatchItemImpressed(broadMatchItemDataView)
    }

    override fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView) {
        presenter?.onBroadMatchItemClick(broadMatchItemDataView)
    }

    override fun trackEventClickBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView) {
        val broadMatchItem = ArrayList<Any>()
        broadMatchItem.add(broadMatchItemDataView.asClickObjectDataLayer())

        SearchTracking.trackEventClickBroadMatchItem(
            queryKey,
            broadMatchItemDataView.alternativeKeyword,
            getUserId(),
            broadMatchItemDataView.isOrganicAds,
            broadMatchItemDataView.componentId,
            broadMatchItem,
        )
    }

    override fun onBroadMatchImpressed(broadMatchDataView: BroadMatchDataView) {
        presenter?.onBroadMatchImpressed(broadMatchDataView)
    }

    override fun onBroadMatchSeeMoreClicked(broadMatchDataView: BroadMatchDataView) {
        presenter?.onBroadMatchSeeMoreClick(broadMatchDataView)
    }

    override fun onBroadMatchThreeDotsClicked(broadMatchItemDataView: BroadMatchItemDataView) {
        showProductCardOptions(this, createProductCardOptionsModel(broadMatchItemDataView))
    }

    override fun onBroadMatchViewAllCardClicked(broadMatchDataView: BroadMatchDataView) {
        presenter?.onBroadMatchViewAllCardClicked(broadMatchDataView)
    }

    private fun createProductCardOptionsModel(item: BroadMatchItemDataView): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()

        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.hasSimilarSearch = true
        productCardOptionsModel.isWishlisted = item.isWishlisted
        productCardOptionsModel.keyword = getSearchParameter()!!.getSearchQuery()
        productCardOptionsModel.productId = item.id
        productCardOptionsModel.screenName = SearchEventTracking.Category.SEARCH_RESULT
        productCardOptionsModel.seeSimilarProductEvent = SearchTracking.EVENT_CLICK_SEARCH_RESULT
        productCardOptionsModel.isTopAds = item.isOrganicAds
        productCardOptionsModel.topAdsWishlistUrl = item.topAdsWishlistUrl
        productCardOptionsModel.topAdsClickUrl = item.topAdsClickUrl

        return productCardOptionsModel
    }

    override fun trackEventImpressionBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView) {
        val trackingQueue = trackingQueue ?: return
        val broadMatchItemAsObjectDataLayer = ArrayList<Any>()
        broadMatchItemAsObjectDataLayer.add(broadMatchItemDataView.asImpressionObjectDataLayer())

        SearchTracking.trackEventImpressionBroadMatch(
                trackingQueue,
                queryKey,
                broadMatchItemDataView.alternativeKeyword,
                getUserId(),
                broadMatchItemAsObjectDataLayer,
        )
    }

    override fun trackEventImpressionBroadMatch(broadMatchDataView: BroadMatchDataView) {
        SearchTracking.trackEventImpressionBroadMatch(
            iris,
            broadMatchDataView,
        )
    }

    override fun trackEventClickSeeMoreBroadMatch(broadMatchDataView: BroadMatchDataView) {
        SearchTracking.trackEventClickBroadMatchSeeMore(
            broadMatchDataView,
            queryKey,
            broadMatchDataView.keyword,
            broadMatchDataView.dimension90,
        )
    }
    //endregion

    //region on boarding / coachmark
    override fun showOnBoarding(firstProductPosition: Int) {
        onBoardingListenerDelegate.showOnBoarding(firstProductPosition)
    }
    //endregion

    //region Bottom Sheet Filter
    private fun openBottomSheetFilterRevamp() {
        presenter?.openFilterPage(getSearchParameter()?.getSearchParameterMap())
    }

    override fun sendTrackingOpenFilterPage() {
        FilterTracking.eventOpenFilterPage(filterTrackingData)
    }

    override fun openBottomSheetFilter(dynamicFilterModel: DynamicFilterModel?) {
        if (!isAdded) return

        sortFilterBottomSheet = SortFilterBottomSheet()
        sortFilterBottomSheet?.show(
                parentFragmentManager,
                searchParameter?.getSearchParameterHashMap(),
                dynamicFilterModel,
                this
        )
        sortFilterBottomSheet?.setOnDismissListener {
            sortFilterBottomSheet = null
            presenter?.onBottomSheetFilterDismissed()
        }
    }

    override fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel) {
        val searchParameterMap = searchParameter?.getSearchParameterHashMap() ?: mapOf()

        filterController.appendFilterList(searchParameterMap, dynamicFilterModel.data.filter)

        sortFilterBottomSheet?.setDynamicFilterModel(dynamicFilterModel)
    }

    override fun onApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
        presenter?.onApplySortFilter(applySortFilterModel.mapParameter)

        sortFilterBottomSheet = null

        applySort(applySortFilterModel)
        applyFilter(applySortFilterModel)

        refreshSearchParameter(applySortFilterModel.mapParameter)

        lastFilterListenerDelegate.updateLastFilter()

        reloadData()
    }

    private fun applySort(applySortFilterModel: ApplySortFilterModel) {
        if (applySortFilterModel.selectedSortName.isEmpty()
                || applySortFilterModel.selectedSortMapParameter.isEmpty()) return

        SearchTracking.eventSearchResultSort(screenName, applySortFilterModel.selectedSortName, getUserId())
    }

    private fun applyFilter(applySortFilterModel: ApplySortFilterModel) {
        FilterTracking
                .eventApplyFilter(filterTrackingData, screenName, applySortFilterModel.selectedFilterMapParameter)
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        presenter?.getProductCount(mapParameter)
    }

    override fun setProductCount(productCountText: String?) {
        sortFilterBottomSheet?.setResultCountText(getFilterCountText(productCountText))
    }

    private fun getFilterCountText(productCountText: String?): String =
        if (productCountText.isNullOrBlank()) {
            getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_no_count)
        } else {
            String.format(
                getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_template_text),
                productCountText
            )
        }
    //endregion

    //region TopAdsImageView / TDN
    override fun onTopAdsImageViewImpressed(
            className: String?,
            searchTopAdsImageDataView: SearchProductTopAdsImageDataView,
    ) {
        if (className == null || context == null) return

        context?.let {
            TopAdsUrlHitter(it).hitImpressionUrl(
                    className,
                    searchTopAdsImageDataView.topAdsImageViewModel.adViewUrl,
                    "",
                    "",
                    searchTopAdsImageDataView.topAdsImageViewModel.imageUrl
            )
        }
    }

    override fun onTopAdsImageViewClick(searchTopAdsImageDataView: SearchProductTopAdsImageDataView) {
        RouteManager.route(context, searchTopAdsImageDataView.topAdsImageViewModel.applink)
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

        val queryParams = filterController.getParameter().addFilterOrigin()
        refreshSearchParameter(queryParams)

        lastFilterListenerDelegate.updateLastFilter()

        reloadData()
    }

    override fun trackEventClickDropdownQuickFilter(filterTitle: String) {
        SearchTracking.trackEventClickDropdownQuickFilter(filterTitle)
    }

    override fun trackEventApplyDropdownQuickFilter(optionList: List<Option>?) {
        SearchTracking.trackEventApplyDropdownQuickFilter(optionList)
    }
    //endregion
}
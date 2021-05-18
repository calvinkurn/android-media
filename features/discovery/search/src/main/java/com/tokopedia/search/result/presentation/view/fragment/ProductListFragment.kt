package com.tokopedia.search.result.presentation.view.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
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
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery.common.manager.ProductCardOptionsResult
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.combinePriceFilterIfExists
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.generateOptionFromUniqueId
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.emptyAddress
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.search.R
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.analytics.RecommendationTracking
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.di.module.SearchContextModule
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.BannerDataView
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.EmptySearchProductDataView
import com.tokopedia.search.result.presentation.model.GlobalNavDataView
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter.OnItemChangeView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ProductItemDecoration
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.BannerListener
import com.tokopedia.search.result.presentation.view.listener.BroadMatchListener
import com.tokopedia.search.result.presentation.view.listener.ChooseAddressListener
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.presentation.view.listener.GlobalNavListener
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.result.presentation.view.listener.QuickFilterElevation
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener
import com.tokopedia.search.result.presentation.view.listener.SearchInTokopediaListener
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationClickListener
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.result.presentation.view.listener.SearchPerformanceMonitoringListener
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener
import com.tokopedia.search.result.presentation.view.listener.TickerListener
import com.tokopedia.search.result.presentation.view.listener.TopAdsImageViewListener
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactoryImpl
import com.tokopedia.search.utils.SearchLogger
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.search.utils.applyQuickFilterElevation
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.search.utils.getFilterParams
import com.tokopedia.search.utils.getSortFilterCount
import com.tokopedia.search.utils.getSortFilterParamsString
import com.tokopedia.search.utils.isSortHasDefaultValue
import com.tokopedia.search.utils.removeQuickFilterElevation
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.base.Config
import com.tokopedia.topads.sdk.base.Endpoint
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.topads.sdk.domain.model.Category
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.FreeOngkir
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import org.json.JSONArray
import java.util.*
import javax.inject.Inject

class ProductListFragment: BaseDaggerFragment(),
        OnItemChangeView,
        ProductListSectionContract.View,
        ProductListener,
        TickerListener,
        SuggestionListener,
        GlobalNavListener,
        BannerAdsListener,
        EmptyStateListener,
        RecommendationListener,
        InspirationCarouselListener,
        BroadMatchListener,
        InspirationCardListener,
        QuickFilterElevation,
        SortFilterBottomSheet.Callback,
        SearchInTokopediaListener,
        SearchNavigationClickListener,
        TopAdsImageViewListener,
        ChooseAddressListener,
        BannerListener {

    companion object {
        private const val SCREEN_SEARCH_PAGE_PRODUCT_TAB = "Search result - Product tab"
        private const val REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123
        private const val SEARCH_RESULT_ENHANCE_ANALYTIC = "SEARCH_RESULT_ENHANCE_ANALYTIC"
        private const val LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT"
        private const val SEARCH_PRODUCT_TRACE = "search_product_trace"
        private const val EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER"
        private const val SEARCH_RESULT_PRODUCT_ONBOARDING_TAG = "SEARCH_RESULT_PRODUCT_ONBOARDING_TAG"
        private const val REQUEST_CODE_LOGIN = 561
        private const val SHOP = "shop"
        private const val DEFAULT_SPAN_COUNT = 2

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

    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var staggeredGridLayoutLoadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var searchNavigationListener: SearchNavigationListener? = null
    private var redirectionListener: RedirectionListener? = null
    private var searchPerformanceMonitoringListener: SearchPerformanceMonitoringListener? = null
    private var recyclerView: RecyclerView? = null
    private var productListAdapter: ProductListAdapter? = null
    private var trackingQueue: TrackingQueue? = null
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var topAdsConfig: Config? = null
    private var searchParameter: SearchParameter? = null
    private val filterController = FilterController()
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

    override val carouselRecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadDataFromArguments()
        initTrackingQueue()
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

    private fun initTrackingQueue() {
        context?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    override fun initInjector() {
        activity?.let {
            DaggerProductListViewComponent.builder()
                    .baseAppComponent(getComponent(BaseAppComponent::class.java))
                    .searchContextModule(SearchContextModule(it))
                    .build()
                    .inject(this)
        }
    }


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreInstanceState(savedInstanceState)
        initViews(view)
        addDefaultSelectedSort()

        presenter?.onViewCreated()
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) return

        copySearchParameter(savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER))
    }

    private fun initViews(view: View) {
        initRecyclerView(view)
        initLayoutManager()
        initSwipeToRefresh(view)
        initTopAdsConfig()
        initTopAdsParams()
        initAdapter()
        initLoadMoreListener()
        initSearchQuickSortFilter(view)
        initShimmeringView(view)

        setupRecyclerView()

        if (userVisibleHint) {
            setupSearchNavigation()
        }
    }

    private fun initRecyclerView(rootView: View) {
        recyclerView = rootView.findViewById(R.id.recyclerview)
    }

    private fun initLayoutManager() {
        staggeredGridLayoutManager = StaggeredGridLayoutManager(
                DEFAULT_SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL
        ).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }

    private fun initSwipeToRefresh(view: View) {
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        refreshLayout?.setOnRefreshListener(this::onSwipeToRefresh)
    }

    private fun initTopAdsConfig() {
        topAdsConfig = Config.Builder()
                .setSessionId(getRegistrationId())
                .setUserId(getUserId())
                .setEndpoint(Endpoint.PRODUCT)
                .build()
    }

    private fun initTopAdsParams() {
        val adsParams = TopAdsParams()
        adsParams.param[TopAdsParams.KEY_SRC] = SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH
        adsParams.param[TopAdsParams.KEY_QUERY] = queryKey
        adsParams.param[TopAdsParams.KEY_USER_ID] = getUserId()

        getSearchParameter()?.let {
            it.cleanUpNullValuesInMap()
            adsParams.param.putAll(it.getSearchParameterHashMap())
        }

        topAdsConfig?.topAdsParams = adsParams
    }

    private fun initAdapter() {
        val topAdsConfig = topAdsConfig ?: return

        val productListTypeFactory = ProductListTypeFactoryImpl(
                productListener = this,
                tickerListener = this,
                suggestionListener = this,
                globalNavListener = this,
                bannerAdsListener = this,
                emptyStateListener = this,
                recommendationListener = this,
                inspirationCarouselListener = this,
                broadMatchListener = this,
                inspirationCardListener = this,
                searchInTokopediaListener = this,
                searchNavigationListener = this,
                topAdsImageViewListener = this,
                chooseAddressListener = this,
                bannerListener = this,
                topAdsConfig = topAdsConfig,
        )

        productListAdapter = ProductListAdapter(itemChangeView = this, typeFactory = productListTypeFactory)
    }

    private fun initLoadMoreListener() {
        staggeredGridLayoutManager?.let {
            staggeredGridLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(it)
        }
    }

    private fun getEndlessRecyclerViewListener(
            recyclerViewLayoutManager: RecyclerView.LayoutManager,
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (isAllowLoadMore()) {
                    val searchParameterMap = searchParameter?.getSearchParameterMap() ?: return
                    presenter?.loadMoreData(searchParameterMap)
                } else {
                    productListAdapter?.removeLoading()
                }
            }
        }
    }

    private fun isAllowLoadMore(): Boolean {
        return userVisibleHint
                && (presenter?.hasNextPage() == true)
    }

    private fun initSearchQuickSortFilter(rootView: View) {
        searchSortFilter = rootView.findViewById(R.id.search_product_quick_sort_filter)
    }

    private fun initShimmeringView(view: View) {
        shimmeringView = view.findViewById(R.id.shimmeringView)
    }

    private fun setupRecyclerView() {
        val onScrollListener = staggeredGridLayoutLoadMoreTriggerListener ?: return

        recyclerView?.apply {
            layoutManager = staggeredGridLayoutManager
            adapter = productListAdapter
            addItemDecoration(createProductItemDecoration())
            addOnScrollListener(onScrollListener)
        }
    }

    private fun createProductItemDecoration(): ProductItemDecoration {
        val spacing = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16) ?: 0
        return ProductItemDecoration(spacing)
    }

    override val isFirstActiveTab
        get() = getActiveTab() == SearchConstant.ActiveTab.PRODUCT

    private fun getActiveTab() = searchParameter?.get(SearchApiConst.ACTIVE_TAB) ?: ""

    override fun showRefreshLayout() {
        refreshLayout?.isRefreshing = true
    }

    override fun hideRefreshLayout() {
        refreshLayout?.isRefreshing = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        searchNavigationListener = castContextToSearchNavigationListener(context)
        redirectionListener = castContextToRedirectionListener(context)
        searchPerformanceMonitoringListener = castContextToSearchPerformanceMonitoring(context)
    }

    private fun castContextToSearchNavigationListener(context: Context): SearchNavigationListener? {
        return if (context is SearchNavigationListener) context else null
    }

    private fun castContextToRedirectionListener(context: Context): RedirectionListener? {
        return if (context is RedirectionListener) context else null
    }

    private fun castContextToSearchPerformanceMonitoring(context: Context): SearchPerformanceMonitoringListener? {
        return if (context is SearchPerformanceMonitoringListener) context else null
    }

    override fun onResume() {
        super.onResume()

        presenter?.onViewResumed()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        presenter?.onViewVisibilityChanged(isVisibleToUser, isAdded)
    }

    override fun setupSearchNavigation() {
        searchNavigationListener?.setupSearchNavigation(object : SearchNavigationListener.ClickListener {
            override fun onChangeGridClick() {
                switchLayoutType()
            }
        })
        refreshMenuItemGridIcon()
    }

    private fun switchLayoutType() {
        val productListAdapter = productListAdapter ?: return
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
        val productListAdapter = productListAdapter ?: return
        if (!userVisibleHint) return

        when (layoutType) {
            SearchConstant.ViewType.LIST -> {
                staggeredGridLayoutManager?.spanCount = 1
                productListAdapter.changeListView()
            }
            SearchConstant.ViewType.SMALL_GRID -> {
                staggeredGridLayoutManager?.spanCount = 2
                productListAdapter.changeDoubleGridView()
            }
            SearchConstant.ViewType.BIG_GRID -> {
                staggeredGridLayoutManager?.spanCount = 1
                productListAdapter.changeSingleGridView()
            }
        }

        refreshMenuItemGridIcon()
    }

    private fun refreshMenuItemGridIcon() {
        val productListAdapter = productListAdapter ?: return

        searchNavigationListener?.refreshMenuItemGridIcon(
                productListAdapter.getTitleTypeRecyclerView(),
                productListAdapter.getIconTypeRecyclerView()
        )
    }

    private fun addDefaultSelectedSort() {
        val searchParameter = searchParameter ?: return
        if (searchParameter.get(SearchApiConst.OB).isEmpty())
            searchParameter.set(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
    }

    override fun trackScreenAuthenticated() {
        if (userVisibleHint
                && activity != null
                && activity?.applicationContext != null) {
            SearchTracking.screenTrackSearchSectionFragment(screenName)
        }
    }

    override fun addProductList(list: List<Visitable<*>>) {
        productListAdapter?.appendItems(list)
    }

    override fun setProductList(list: List<Visitable<*>>) {
        productListAdapter?.clearData()

        stopSearchResultPagePerformanceMonitoring()
        addProductList(list)
    }

    override fun addRecommendationList(list: List<Visitable<*>>) {
        productListAdapter?.appendItems(list)
    }

    private fun stopSearchResultPagePerformanceMonitoring() {
        recyclerView?.viewTreeObserver?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                searchPerformanceMonitoringListener?.let {
                    it.stopRenderPerformanceMonitoring()
                    it.stopPerformanceMonitoring()
                }

                recyclerView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun sendProductImpressionTrackingEvent(
            item: ProductItemDataView,
            suggestedRelatedKeyword: String,
            dimension90: String,
    ) {
        val userId = getUserId()
        val eventLabel = getSearchProductTrackingEventLabel(item, suggestedRelatedKeyword)
        val dataLayerList = mutableListOf<Any>()
        val productItemDataViews = mutableListOf<ProductItemDataView>()
        val irisSessionId = irisSession?.getSessionId() ?: ""
        val filterSortParams = searchParameter?.let {
            getSortFilterParamsString(it.getSearchParameterMap() as Map<String?, Any?>)
        } ?: ""

        dataLayerList.add(item.getProductAsObjectDataLayer(filterSortParams, dimension90))
        productItemDataViews.add(item)

        trackingQueue?.let {
            SearchTracking.eventImpressionSearchResultProduct(it, dataLayerList, eventLabel, irisSessionId, userId)
        }
    }

    private fun getSearchProductTrackingEventLabel(item: ProductItemDataView, suggestedRelatedKeyword: String): String {
        val keyword = if (suggestedRelatedKeyword.isEmpty()) queryKey else suggestedRelatedKeyword
        val pageTitle = item.pageTitle ?: ""

        return if (pageTitle.isEmpty()) keyword else pageTitle
    }

    override fun showNetworkError(startRow: Int) {
        val productListAdapter = productListAdapter ?: return

        if (productListAdapter.isListEmpty())
            showNetworkErrorOnEmptyList()
        else
            showNetworkErrorOnLoadMore()
    }

    private fun showNetworkErrorOnEmptyList() {
        hideViewOnError()

        NetworkErrorHelper.showEmptyState(activity, view) {
            refreshLayout?.visible()
            reloadData()
        }
    }

    private fun hideViewOnError() {
        searchSortFilter?.gone()
        shimmeringView?.gone()
        refreshLayout?.gone()
    }

    private fun showNetworkErrorOnLoadMore() {
        val searchParameter = searchParameter ?: return

        NetworkErrorHelper.createSnackbarWithAction(activity) {
            addLoading()
            presenter?.loadMoreData(searchParameter.getSearchParameterMap())
        }.showRetrySnackbar()
    }

    private fun getScreenNameId() = SCREEN_SEARCH_PAGE_PRODUCT_TAB

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
                    },
                    addToCartCallback = object : ProductCardOptionsResult {
                        override fun onReceiveResult(productCardOptionsModel: ProductCardOptionsModel) {
                            handleAddToCartAction(productCardOptionsModel)
                        }
                    },
                    visitShopCallback = object : ProductCardOptionsResult {
                        override fun onReceiveResult(productCardOptionsModel: ProductCardOptionsModel) {
                            handleVisitShopAction()
                        }
                    },
                    shareProductCallback = object : ProductCardOptionsResult {
                        override fun onReceiveResult(productCardOptionsModel: ProductCardOptionsModel) {
                            handleShareProductAction(productCardOptionsModel)
                        }
                    }
            )
        }
    }

    private fun updateWishlistFromPDP(position: Int, isWishlist: Boolean) {
        val productListAdapter = productListAdapter ?: return

        val isProductOrRecommendation =
                productListAdapter.isProductItem(position) || productListAdapter.isRecommendationItem(position)

        if (isProductOrRecommendation)
            productListAdapter.updateWishlistStatus(position, isWishlist)
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        presenter?.handleWishlistAction(productCardOptionsModel)
    }

    private fun handleAddToCartAction(productCardOptionsModel: ProductCardOptionsModel) {
        presenter?.handleAddToCartAction(productCardOptionsModel)
    }

    override fun trackSuccessAddToCartEvent(isAds: Boolean, addToCartDataLayer: Any) {
        SearchTracking.trackEventAddToCart(queryKey, isAds, addToCartDataLayer)
    }

    override fun showAddToCartSuccessMessage() {
        view?.let {
            Toaster.make(it, getString(R.string.search_add_to_cart_success), LENGTH_SHORT, TYPE_NORMAL)
        }
    }

    override fun showAddToCartFailedMessage(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage, LENGTH_SHORT, TYPE_ERROR)
        }
    }

    private fun handleVisitShopAction() {
        presenter?.handleVisitShopAction()
    }

    override fun routeToShopPage(shopId: String?) {
        context?.let {
            RouteManager.route(it, ApplinkConst.SHOP, shopId)
        }
    }

    override fun trackEventGoToShopPage(dataLayer: Any) {
        SearchTracking.trackEventGoToShopPage(queryKey, dataLayer)
    }

    private fun handleShareProductAction(productCardOptionsModel: ProductCardOptionsModel) {
        SearchTracking.trackEventShareProduct(queryKey, productCardOptionsModel.productId)
    }

    override fun onPause() {
        super.onPause()

        val irisSessionId = irisSession?.getSessionId() ?: ""

        TopAdsGtmTracker.getInstance().eventSearchResultProductView(
                trackingQueue,
                queryKey,
                SCREEN_SEARCH_PAGE_PRODUCT_TAB,
                irisSessionId,
                getUserId(),
        )

        trackingQueue?.sendAll()
    }

    override fun onProductImpressed(item: ProductItemDataView?, adapterPosition: Int) {
        presenter?.onProductImpressed(item, adapterPosition)
    }

    override fun sendTopAdsGTMTrackingProductImpression(item: ProductItemDataView) {
        val product: Product = createTopAdsProductForTracking(item)
        TopAdsGtmTracker.getInstance().addSearchResultProductViewImpressions(product, item.position)
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

    override fun onGlobalNavWidgetClicked(item: GlobalNavDataView.Item?, keyword: String?) {
        if (item == null) return

        redirectionStartActivity(item.applink, item.url)

        SearchTracking.trackEventClickGlobalNavWidgetItem(
                item.getGlobalNavItemAsObjectDataLayer(item.name),
                keyword ?: "",
                item.name,
                item.applink
        )
    }

    override fun onGlobalNavWidgetClickSeeAll(model: GlobalNavDataView?) {
        if (model == null) return

        redirectionStartActivity(model.seeAllApplink, model.seeAllUrl)

        SearchTracking.eventUserClickSeeAllGlobalNavWidget(
                model.keyword,
                model.title,
                model.seeAllApplink
        )
    }

    override fun redirectionStartActivity(applink: String?, url: String?) {
        if (!applink.isNullOrEmpty())
            redirectionListener?.startActivityWithApplink(applink.decodeQueryParameter())
        else
            redirectionListener?.startActivityWithUrl(url)
    }

    override fun onItemClicked(item: ProductItemDataView?, adapterPosition: Int) {
        presenter?.onProductClick(item, adapterPosition)
    }

    override fun sendTopAdsGTMTrackingProductClick(item: ProductItemDataView) {
        val product = createTopAdsProductForTracking(item)

        TopAdsGtmTracker.eventSearchResultProductClick(
                context,
                queryKey,
                product,
                item.position,
                SCREEN_SEARCH_PAGE_PRODUCT_TAB,
                getUserId(),
        )
    }

    override fun sendGTMTrackingProductClick(
            item: ProductItemDataView,
            userId: String,
            suggestedRelatedKeyword: String,
            dimension90: String,
    ) {
        val eventLabel = getSearchProductTrackingEventLabel(item, suggestedRelatedKeyword)
        val filterSortParams = searchParameter?.let {
            getSortFilterParamsString(it.getSearchParameterMap() as Map<String?, Any?>)
        } ?: ""

        SearchTracking.trackEventClickSearchResultProduct(
                item.getProductAsObjectDataLayer(filterSortParams, dimension90),
                item.isOrganicAds,
                eventLabel,
                filterSortParams,
                userId,
        )
    }

    override fun routeToProductDetail(item: ProductItemDataView?, adapterPosition: Int) {
        item ?: return
        val intent = getProductIntent(item.productID, item.warehouseID) ?: return

        intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition)
        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL)
    }

    private fun getProductIntent(productId: String, warehouseId: String): Intent? {
        return context?.let {
            if (warehouseId.isNotEmpty())
                RouteManager.getIntent(
                        it,
                        ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_WAREHOUSE_ID,
                        productId,
                        warehouseId
                )
            else
                RouteManager.getIntent(
                        it,
                        ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                        productId
                )
        }
    }

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

    override fun onTickerClicked(tickerDataView: TickerDataView) {
        SearchTracking.trackEventClickTicker(queryKey, tickerDataView.typeId)
        applyParamsFromTicker(UrlParamUtils.getParamMap(tickerDataView.query))
    }

    private fun applyParamsFromTicker(tickerParams: HashMap<String?, String?>) {
        val params = HashMap(filterController.getParameter())
        params.putAll(tickerParams)

        refreshSearchParameter(params)
        refreshFilterController(params)

        reloadData()
    }

    private fun refreshSearchParameter(queryParams: Map<String, String>) {
        searchParameter?.apply {
            getSearchParameterHashMap().clear()
            getSearchParameterHashMap().putAll(queryParams)
            getSearchParameterHashMap()[SearchApiConst.ORIGIN_FILTER] =
                    SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        }
    }

    private fun refreshFilterController(queryParams: HashMap<String, String>) {
        val params = HashMap(queryParams)
        params[SearchApiConst.ORIGIN_FILTER] =
                SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE

        filterController.refreshMapParameter(params)
    }

    override fun onTickerDismissed() {
        presenter?.onPriceFilterTickerDismissed()
        productListAdapter?.removePriceFilterTicker()
    }

    override val isTickerHasDismissed
        get() = presenter?.isTickerHasDismissed ?: false

    override fun onSuggestionClicked(suggestionDataView: SuggestionDataView?) {
        if (suggestionDataView == null) return

        SearchTracking.eventClickSuggestedSearch(queryKey, suggestionDataView.suggestion)
        performNewProductSearch(suggestionDataView.suggestedQuery)
    }

    private fun performNewProductSearch(queryParams: String) {
        val applinkToSearchResult = ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + queryParams
        val modifiedApplinkToSearchResult = modifyApplinkToSearchResult(applinkToSearchResult)

        redirectionListener?.startActivityWithApplink(modifiedApplinkToSearchResult)
    }

    private fun modifyApplinkToSearchResult(applink: String): String {
        val urlParser = URLParser(applink)

        val params = urlParser.paramKeyValueMap
        params[SearchApiConst.PREVIOUS_KEYWORD] = queryKey

        return ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + UrlParamUtils.generateUrlParamString(params)
    }

    override fun isQuickFilterSelected(option: Option?): Boolean {
        option ?: return false

        return filterController.getFilterViewState(option.uniqueId)
    }

    override fun onQuickFilterSelected(option: Option) {
        val isQuickFilterSelectedReversed = !isQuickFilterSelected(option)
        setFilterToQuickFilterController(option, isQuickFilterSelectedReversed)

        val queryParams = filterController.getParameter()
        refreshSearchParameter(queryParams)
        refreshFilterController(HashMap(queryParams))

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

    override fun onSelectedFilterRemoved(uniqueId: String?) {
        removeSelectedFilter(uniqueId ?: "")
    }

    private fun removeSelectedFilter(uniqueId: String) {
        val option = generateOptionFromUniqueId(uniqueId)

        removeFilterFromFilterController(option)
        refreshSearchParameter(filterController.getParameter())
        refreshFilterController(HashMap(filterController.getParameter()))

        reloadData()
    }

    private fun removeFilterFromFilterController(option: Option) {
        val optionKey = option.key

        if (Option.KEY_CATEGORY == optionKey) {
            filterController.setFilter(option, isFilterApplied = false, isCleanUpExistingFilterWithSameKey = true)
        } else if (Option.KEY_PRICE_MIN == optionKey || Option.KEY_PRICE_MAX == optionKey) {
            filterController.setFilter(
                    generatePriceOption(Option.KEY_PRICE_MIN),
                    isFilterApplied = false,
                    isCleanUpExistingFilterWithSameKey = true
            )
            filterController.setFilter(
                    generatePriceOption(Option.KEY_PRICE_MAX),
                    isFilterApplied = false,
                    isCleanUpExistingFilterWithSameKey = true
            )
        } else {
            filterController.setFilter(option, false)
        }
    }

    private fun generatePriceOption(priceOptionKey: String) =
            Option().apply { key = priceOptionKey }

    override fun onEmptyButtonClicked() {
        SearchTracking.eventUserClickNewSearchOnEmptySearchProduct(queryKey)
        showSearchInputView()
    }

    private fun showSearchInputView() {
        redirectionListener?.showSearchInputView()
    }

    override fun getSelectedFilterAsOptionList(): List<Option> {
        return getOptionListFromFilterController()
    }

    private fun getOptionListFromFilterController(): List<Option> {
        return combinePriceFilterIfExists(
                filterController.getActiveFilterOptionList(),
                resources.getString(R.string.empty_state_selected_filter_price_name)
        )
    }

    override fun onEmptySearchToGlobalSearchClicked(applink: String?) {
        redirectionListener?.startActivityWithApplink(applink)
    }

    override fun stopTracePerformanceMonitoring() {
        performanceMonitoring?.stopTrace()
    }

    override fun getUserId() = presenter?.userId ?: "0"

    override val queryKey: String
        get() = searchParameter?.getSearchQuery() ?: ""

    override val previousKeyword: String
        get() = searchParameter?.get(SearchApiConst.PREVIOUS_KEYWORD) ?: ""

    override fun setEmptyProduct(globalNavDataView: GlobalNavDataView?, emptySearchProductDataView: EmptySearchProductDataView) {
        productListAdapter?.showEmptyState(globalNavDataView, emptySearchProductDataView)
    }

    private fun setSortFilterIndicatorCounter() {
        val searchParameter = searchParameter ?: return
        searchSortFilter?.indicatorCounter = getSortFilterCount(searchParameter.getSearchParameterMap())
    }

    override fun setBannedProductsErrorMessage(bannedProductsErrorMessageAsList: List<Visitable<*>>) {
        productListAdapter?.appendItems(bannedProductsErrorMessageAsList)
    }

    override fun trackEventImpressionBannedProducts(isEmptySearch: Boolean) {
        if (isEmptySearch)
            SearchTracking.trackEventImpressionBannedProductsEmptySearch(queryKey)
        else
            SearchTracking.trackEventImpressionBannedProductsWithResult(queryKey)
    }

    override fun trackEventImpressionTicker(typeId: Int) {
        SearchTracking.trackEventImpressionTicker(queryKey, typeId)
    }

    override fun reloadData() {
        val searchParameter = getSearchParameter() ?: return

        showRefreshLayout()

        presenter?.clearData()
        productListAdapter?.clearData()

        hideSearchSortFilter()
        initTopAdsParams()

        performanceMonitoring = PerformanceMonitoring.start(SEARCH_PRODUCT_TRACE)
        presenter?.loadData(searchParameter.getSearchParameterMap())

        TopAdsGtmTracker.getInstance().clearDataLayerList()

        setSortFilterIndicatorCounter()
    }

    private fun hideSearchSortFilter() {
        searchSortFilter?.gone()
        shimmeringView?.visible()
    }

    private fun onSwipeToRefresh() {
        reloadData()
    }

    override fun onChangeList() {
        recyclerView?.requestLayout()
    }

    override fun onChangeDoubleGrid() {
        recyclerView?.requestLayout()
    }

    override fun onChangeSingleGrid() {
        recyclerView?.requestLayout()
    }

    private fun getSearchParameter(): SearchParameter? {
        return searchParameter
    }

    override fun backToTop() {
        smoothScrollRecyclerView()
    }

    private fun smoothScrollRecyclerView() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    override fun getScreenName(): String {
        return getScreenNameId()
    }

    override fun addLoading() {
        productListAdapter?.addLoading()
    }

    override fun removeLoading() {
        removeSearchPageLoading()
        productListAdapter?.removeLoading()
    }

    private fun removeSearchPageLoading() {
        if (isFirstActiveTab)
            searchNavigationListener?.removeSearchPageLoading()
    }

    override fun setAutocompleteApplink(autocompleteApplink: String?) {
        redirectionListener?.setAutocompleteApplink(autocompleteApplink)
    }

    override fun sendTrackingEventAppsFlyerViewListingSearch(
            afProdIds: JSONArray?,
            query: String?,
            prodIdArray: ArrayList<String?>?,
    ) {
        SearchTracking.eventAppsFlyerViewListingSearch(afProdIds!!, query!!, prodIdArray!!)
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

    override fun sendImpressionGlobalNav(globalNavDataView: GlobalNavDataView) {
        val dataLayerList = globalNavDataView.itemList.map { item ->
            item.getGlobalNavItemAsObjectDataLayer(item.applink)
        }

        trackingQueue?.let {
            SearchTracking.trackEventImpressionGlobalNavWidgetItem(it, dataLayerList, globalNavDataView.keyword)
        }
    }

    override val isAnyFilterActive: Boolean
        get() = filterController.isFilterActive()

    override val isAnySortActive: Boolean
        get() = (searchParameter?.getSearchParameterMap()?.isSortHasDefaultValue() == false)

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

    override fun updateScrollListener() {
        staggeredGridLayoutLoadMoreTriggerListener?.updateStateAfterGetData()
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

    override fun sendTrackingForNoResult(resultCode: String?, alternativeKeyword: String?, keywordProcess: String?) {
        val searchParameter = searchParameter ?: return

        @Suppress("UNCHECKED_CAST")
        val searchParameterHashMap = searchParameter.getSearchParameterHashMap() as Map<String?, String?>
        val filterMapParameter = getFilterParams(searchParameterHashMap)

        SearchTracking.eventSearchNoResult(
                queryKey,
                screenName,
                filterMapParameter,
                alternativeKeyword,
                resultCode,
                keywordProcess
        )
    }

    override fun setDefaultLayoutType(defaultView: Int) {
        when (defaultView) {
            SearchConstant.DefaultViewType.SMALL_GRID -> switchLayoutTypeTo(SearchConstant.ViewType.SMALL_GRID)
            SearchConstant.DefaultViewType.LIST -> switchLayoutTypeTo(SearchConstant.ViewType.LIST)
            else -> switchLayoutTypeTo(SearchConstant.ViewType.SMALL_GRID)
        }
    }

    override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
        if (applink == null || data == null) return

        redirectionListener?.startActivityWithApplink(applink)
        trackBannerAdsClicked(position, applink, data)
    }

    private fun trackBannerAdsClicked(position: Int, applink: String, data: CpmData) {
        if (applink.contains(SHOP)) {
            TopAdsGtmTracker.eventTopAdsHeadlineShopClick(position, queryKey, data, getUserId())
            TopAdsGtmTracker.eventSearchResultPromoShopClick(activity, data, position)
        } else {
            TopAdsGtmTracker.eventTopAdsHeadlineProductClick(position, queryKey, data, getUserId())
            TopAdsGtmTracker.eventSearchResultPromoProductClick(activity, data, position)
        }
    }

    override fun onBannerAdsImpressionListener(position: Int, data: CpmData?) {
        if (data == null) return

        TopAdsGtmTracker.eventTopAdsHeadlineShopView(position, data, queryKey, getUserId())
        TopAdsGtmTracker.eventSearchResultPromoView(activity, data, position)
    }

    override fun getRegistrationId() = presenter?.deviceId ?: ""

    override fun onInspirationCarouselListProductClicked(product: InspirationCarouselDataView.Option.Product) {
        redirectionStartActivity(product.applink, product.url)

        val products: MutableList<Any> = ArrayList()
        products.add(product.getInspirationCarouselListProductAsObjectDataLayer())

        SearchTracking.trackEventClickInspirationCarouselListProduct(product.inspirationCarouselType, queryKey, products)
    }

    override fun onInspirationCarouselInfoProductClicked(product: InspirationCarouselDataView.Option.Product) {
        redirectionStartActivity(product.applink, product.url)

        val products: MutableList<Any> = ArrayList()
        products.add(product.getInspirationCarouselInfoProductAsObjectDataLayer())

        SearchTracking.trackEventClickInspirationCarouselInfoProduct(product.inspirationCarouselType, queryKey, products)
    }

    override fun onInspirationCarouselSeeAllClicked(inspirationCarouselDataViewOption: InspirationCarouselDataView.Option) {
        redirectionStartActivity(inspirationCarouselDataViewOption.applink, inspirationCarouselDataViewOption.url)

        val keywordBefore = queryKey
        val applink = Uri.parse(inspirationCarouselDataViewOption.applink)
        val keywordAfter = applink.getQueryParameter(SearchApiConst.Q) ?: ""

        SearchTracking.trackEventClickInspirationCarouselOptionSeeAll(
                inspirationCarouselDataViewOption.inspirationCarouselType,
                keywordBefore,
                keywordAfter
        )
    }

    override fun onInspirationCarouselGridProductClicked(product: InspirationCarouselDataView.Option.Product) {
        redirectionStartActivity(product.applink, product.url)

        val products: MutableList<Any> = ArrayList()
        products.add(product.getInspirationCarouselListProductAsObjectDataLayer())

        SearchTracking.trackEventClickInspirationCarouselListProduct(product.inspirationCarouselType, queryKey, products)
    }

    override fun onInspirationCarouselGridBannerClicked(option: InspirationCarouselDataView.Option) {
        redirectionStartActivity(option.bannerApplinkUrl, option.bannerLinkUrl)

        SearchTracking.trackEventClickInspirationCarouselGridBanner(
                option.inspirationCarouselType, queryKey, option.getBannerDataLayer(queryKey), getUserId()
        )
    }

    override fun onImpressedInspirationCarouselInfoProduct(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue ?: return

        val products: MutableList<Any> = ArrayList()
        products.add(product.getInspirationCarouselInfoProductAsObjectDataLayer())

        SearchTracking.trackImpressionInspirationCarouselInfo(
                trackingQueue,
                product.inspirationCarouselType,
                queryKey,
                products
        )
    }


    override fun onImpressedInspirationCarouselListProduct(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue ?: return

        val products: MutableList<Any> = ArrayList()
        products.add(product.getInspirationCarouselListProductImpressionAsObjectDataLayer())

        SearchTracking.trackImpressionInspirationCarouselList(
                trackingQueue,
                product.inspirationCarouselType,
                queryKey,
                products
        )
    }

    override fun onImpressedInspirationCarouselGridProduct(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue ?: return

        val products: MutableList<Any> = ArrayList()
        products.add(product.getInspirationCarouselListProductImpressionAsObjectDataLayer())

        SearchTracking.trackImpressionInspirationCarouselList(
                trackingQueue,
                product.inspirationCarouselType,
                queryKey,
                products
        )
    }

    override fun onInspirationCarouselChipsProductClicked(product: InspirationCarouselDataView.Option.Product) {
        redirectionStartActivity(product.applink, product.url)

        val filterSortParams = getSortFilterParamStringFromSearchParameter()

        val products: MutableList<Any> = ArrayList()
        products.add(product.getInspirationCarouselChipsProductAsObjectDataLayer(filterSortParams))

        SearchTracking.trackEventClickInspirationCarouselChipsProduct(
                product.inspirationCarouselType,
                queryKey,
                product.optionTitle,
                getUserId(),
                products
        )
    }

    private fun getSortFilterParamStringFromSearchParameter() =
            searchParameter?.let {
                @Suppress("UNCHECKED_CAST")
                getSortFilterParamsString(it.getSearchParameterMap() as Map<String?, String?>)
            } ?: ""

    override fun onImpressedInspirationCarouselChipsProduct(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue ?: return
        val filterSortParams = getSortFilterParamStringFromSearchParameter()

        val products: MutableList<Any> = ArrayList()
        products.add(product.getInspirationCarouselChipsProductAsObjectDataLayer(filterSortParams))

        SearchTracking.trackImpressionInspirationCarouselChips(
                trackingQueue,
                product.inspirationCarouselType,
                queryKey,
                product.optionTitle,
                getUserId(),
                products
        )
    }

    override fun onInspirationCarouselChipsSeeAllClicked(option: InspirationCarouselDataView.Option) {
        redirectionStartActivity(option.applink, option.url)

        SearchTracking.trackEventClickInspirationCarouselChipsSeeAll(
                option.inspirationCarouselType,
                queryKey,
                option.title,
                getUserId()
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
        SearchTracking.trackEventClickInspirationCarouselChipsVariant(
                option.inspirationCarouselType,
                queryKey,
                option.title,
                getUserId()
        )
    }

    override val abTestRemoteConfig: RemoteConfig?
        get() = RemoteConfigInstance.getInstance().abTestPlatform

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
        productListAdapter?.updateWishlistStatus(productId!!, isWishlisted)
    }

    override fun showMessageSuccessWishlistAction(isWishlisted: Boolean) {
        val view = view ?: return

        if (isWishlisted)
            Toaster.build(view, getString(R.string.msg_add_wishlist), Snackbar.LENGTH_SHORT, TYPE_NORMAL).show()
        else
            Toaster.build(view, getString(R.string.msg_remove_wishlist), Snackbar.LENGTH_SHORT, TYPE_NORMAL).show()
    }

    override fun showMessageFailedWishlistAction(isWishlisted: Boolean) {
        val view = view ?: return

        if (isWishlisted)
            Toaster.build(view, getString(R.string.msg_add_wishlist_failed), Snackbar.LENGTH_SHORT, TYPE_ERROR).show()
        else
            Toaster.build(view, getString(R.string.msg_remove_wishlist_failed), Snackbar.LENGTH_SHORT, TYPE_ERROR).show()
    }

    override val isLandingPage: Boolean
        get() = searchParameter?.getBoolean(SearchApiConst.LANDING_PAGE) ?: false

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter)
    }

    override fun logWarning(message: String?, throwable: Throwable?) {
        SearchLogger().logWarning(message, throwable)
    }

    override fun stopPreparePagePerformanceMonitoring() {
        searchPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        searchPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        searchPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        searchPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
    }

    override fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView) {
        presenter?.onBroadMatchItemImpressed(broadMatchItemDataView)
    }

    override fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView) {
        presenter?.onBroadMatchItemClick(broadMatchItemDataView)
    }

    override fun trackEventClickBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView) {
        val broadMatchItem: MutableList<Any> = ArrayList()
        broadMatchItem.add(broadMatchItemDataView.asClickObjectDataLayer())

        SearchTracking.trackEventClickBroadMatchItem(
                queryKey,
                broadMatchItemDataView.alternativeKeyword,
                getUserId(),
                broadMatchItem
        )
    }

    override fun onBroadMatchSeeMoreClicked(broadMatchDataView: BroadMatchDataView) {
        SearchTracking.trackEventClickBroadMatchSeeMore(queryKey, broadMatchDataView.keyword)

        val applink = if (broadMatchDataView.applink.startsWith(ApplinkConst.DISCOVERY_SEARCH))
            modifyApplinkToSearchResult(broadMatchDataView.applink)
        else broadMatchDataView.applink

        redirectionStartActivity(applink, broadMatchDataView.url)
    }

    override fun onBroadMatchThreeDotsClicked(broadMatchItemDataView: BroadMatchItemDataView) {
        showProductCardOptions(this, createProductCardOptionsModel(broadMatchItemDataView))
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

        return productCardOptionsModel
    }

    override fun trackBroadMatchImpression(broadMatchItemDataView: BroadMatchItemDataView) {
        val trackingQueue = trackingQueue ?: return
        val broadMatchItemAsObjectDataLayer: MutableList<Any> = ArrayList()
        broadMatchItemAsObjectDataLayer.add(broadMatchItemDataView.asImpressionObjectDataLayer())

        SearchTracking.trackEventImpressionBroadMatch(
                trackingQueue,
                queryKey,
                broadMatchItemDataView.alternativeKeyword,
                getUserId(),
                broadMatchItemAsObjectDataLayer
        )
    }

    override fun onInspirationCardOptionClicked(optionData: InspirationCardOptionDataView) {
        trackEventClickInspirationCardOption(optionData)

        redirectionStartActivity(optionData.applink, optionData.url)
    }

    private fun trackEventClickInspirationCardOption(option: InspirationCardOptionDataView) {
        val label = option.inspirationCardType + " - " + queryKey + " - " + option.text
        SearchTracking.trackEventClickInspirationCardOption(label)
    }

    override fun initFilterControllerForQuickFilter(quickFilterList: List<Filter>) {
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
        val quickFilterOptionList = presenter?.quickFilterOptionList ?: listOf()
        for (i in items.indices) {
            if (i >= quickFilterOptionList.size) break
            val item = items[i]
            val quickFilterOption = quickFilterOptionList[i]

            sortFilterItemShowNew(item, quickFilterOption.isNew)
        }
    }

    private fun sortFilterItemShowNew(item: SortFilterItem, isNew: Boolean) {
        item.refChipUnify.showNewNotification = isNew
    }

    override fun showOnBoarding(firstProductPositionWithBOELabel: Int) {
        val productWithBOELabel = getFirstProductWithBOELabel(firstProductPositionWithBOELabel)

        recyclerView?.postDelayed({
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                buildCoachMark(productWithBOELabel)
            } else {
                buildCoachMark2(productWithBOELabel)
            }
        }, 200)
    }

    private fun getFirstProductWithBOELabel(firstProductPositionWithBOELabel: Int): View? {
        val viewHolder = recyclerView?.findViewHolderForAdapterPosition(firstProductPositionWithBOELabel)
                ?: return null

        return if (viewHolder.itemView is IProductCardView) viewHolder.itemView
        else null
    }

    private fun buildCoachMark(view: View?) {
        val coachMarkItemList = createCoachMarkItemList(view)
        if (coachMarkItemList.isEmpty()) return

        val builder = CoachMarkBuilder()
        builder.allowPreviousButton(false)
        builder.build().show(activity, SEARCH_RESULT_PRODUCT_ONBOARDING_TAG, coachMarkItemList)
    }

    private fun createCoachMarkItemList(boeLabelProductCard: View?): ArrayList<CoachMarkItem> {
        val coachMarkItemList = ArrayList<CoachMarkItem>()

        if (boeLabelProductCard != null)
            coachMarkItemList.add(createBOELabelCoachMarkItem(boeLabelProductCard))

        return coachMarkItemList
    }

    private fun createBOELabelCoachMarkItem(boeLabelProductCard: View): CoachMarkItem {
        return CoachMarkItem(
                boeLabelProductCard,
                getString(R.string.search_product_boe_label_onboarding_title),
                getString(R.string.search_product_boe_label_onboarding_description)
        )
    }

    private fun buildCoachMark2(view: View?) {
        context?.let {
            val coachMark2ItemList = createCoachMark2ItemList(view)
            if (coachMark2ItemList.isEmpty()) return

            val coachMark = CoachMark2(it)
            coachMark.showCoachMark(coachMark2ItemList, null, 0)
        }
    }

    private fun createCoachMark2ItemList(boeLabelProductCard: View?): ArrayList<CoachMark2Item> {
        val coachMarkItemList = ArrayList<CoachMark2Item>()

        if (boeLabelProductCard != null)
            coachMarkItemList.add(createBOELabelCoachMark2Item(boeLabelProductCard))

        return coachMarkItemList
    }

    private fun createBOELabelCoachMark2Item(boeLabelProductCard: View): CoachMark2Item {
        return CoachMark2Item(
                boeLabelProductCard,
                getString(R.string.search_product_boe_label_onboarding_title),
                getString(R.string.search_product_boe_label_onboarding_description),
                CoachMark2.POSITION_TOP
        )
    }

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
        sortFilterBottomSheet = null

        applySort(applySortFilterModel)
        applyFilter(applySortFilterModel)

        filterController.refreshMapParameter(applySortFilterModel.mapParameter)
        searchParameter?.getSearchParameterHashMap()?.clear()
        searchParameter?.getSearchParameterHashMap()?.putAll(applySortFilterModel.mapParameter)

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
        sortFilterBottomSheet?.setResultCountText(String.format(
                getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_template_text),
                productCountText
        ))
    }

    override val className: String
        get() = activity?.javaClass?.name ?: ""


    override fun configure(shouldRemove: Boolean) {
        if (shouldRemove)
            removeQuickFilterElevation(searchSortFilter)
        else
            applyQuickFilterElevation(context, searchSortFilter)
    }

    override fun onSearchInTokopediaClick(applink: String) {
        RouteManager.route(activity, applink)
    }

    override fun addLocalSearchRecommendation(visitableList: List<Visitable<*>>) {
        productListAdapter?.appendItems(visitableList)
    }

    override fun onChangeViewClicked(position: Int) {
        val currentLayoutType = productListAdapter?.getCurrentLayoutType() ?: SearchConstant.ViewType.SMALL_GRID
        presenter?.handleChangeView(position, currentLayoutType)
    }

    override fun trackEventSearchResultChangeView(viewType: String) {
        SearchTracking.eventSearchResultChangeGrid(viewType, screenName)
    }

    override fun switchSearchNavigationLayoutTypeToListView(position: Int) {
        if (!userVisibleHint) return

        staggeredGridLayoutManager?.spanCount = 1
        productListAdapter?.changeSearchNavigationListView(position)
    }

    override fun switchSearchNavigationLayoutTypeToBigGridView(position: Int) {
        if (!userVisibleHint) return

        staggeredGridLayoutManager?.spanCount = 1
        productListAdapter?.changeSearchNavigationSingleGridView(position)
    }


    override fun switchSearchNavigationLayoutTypeToSmallGridView(position: Int) {
        if (!userVisibleHint) return

        staggeredGridLayoutManager?.spanCount = 2
        productListAdapter?.changeSearchNavigationDoubleGridView(position)
    }

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

    override fun onLocalizingAddressSelected() {
        presenter?.onLocalizingAddressSelected()
    }

    override fun getFragment() = this

    override val isChooseAddressWidgetEnabled: Boolean
        get() = context?.let {
            try {
                ChooseAddressUtils.isRollOutUser(it)
            } catch (e: Throwable) {
                false
            }
        } ?: false


    override val chooseAddressData: LocalCacheModel
        get() = context?.let {
            try {
                ChooseAddressUtils.getLocalizingAddressData(it)
            } catch (e: Throwable) {
                emptyAddress
            }
        } ?: emptyAddress


    override fun getIsLocalizingAddressHasUpdated(currentChooseAddressData: LocalCacheModel): Boolean {
        return context?.let {
            try {
                ChooseAddressUtils.isLocalizingAddressHasUpdated(it, currentChooseAddressData)
            } catch (e: Throwable) {
                false
            }
        } ?: false
    }

    override fun refreshItemAtIndex(index: Int) {
        productListAdapter?.refreshItemAtIndex(index)
    }

    override fun onBannerClicked(bannerDataView: BannerDataView) {
        redirectionStartActivity(bannerDataView.applink, "")
    }
}
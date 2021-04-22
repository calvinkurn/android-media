package com.tokopedia.search.result.shop.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.State
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.search.R
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.presentation.view.listener.QuickFilterElevation
import com.tokopedia.search.result.presentation.viewmodel.SearchViewModel
import com.tokopedia.search.result.shop.presentation.adapter.ShopListAdapter
import com.tokopedia.search.result.shop.presentation.itemdecoration.ShopListItemDecoration
import com.tokopedia.search.result.shop.presentation.listener.ShopListener
import com.tokopedia.search.result.shop.presentation.model.ShopDataView
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactory
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactoryImpl
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModel
import com.tokopedia.search.utils.applyQuickFilterElevation
import com.tokopedia.search.utils.convertValuesToString
import com.tokopedia.search.utils.removeQuickFilterElevation
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.CpmData
import kotlinx.android.synthetic.main.search_result_shop_fragment_layout.*

internal class ShopListFragment:
        TkpdBaseV4Fragment(),
        ShopListener,
        EmptyStateListener,
        BannerAdsListener,
        QuickFilterElevation,
        SortFilterBottomSheet.Callback {

    companion object {
        private const val SHOP = "shop"
        private const val SEARCH_SHOP_TRACE = "search_shop_trace"

        @JvmStatic
        fun newInstance(): ShopListFragment {
            return ShopListFragment()
        }
    }

    private var gridLayoutManager: GridLayoutManager? = null
    private var shopListAdapter: ShopListAdapter? = null
    private var recyclerViewSearchShop: RecyclerView? = null
    private var gridLayoutLoadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var searchShopViewModel: SearchShopViewModel? = null
    private var searchViewModel: SearchViewModel? = null
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private val filterTrackingData by lazy {
        FilterTrackingData(
                FilterEventTracking.Event.CLICK_SEARCH_RESULT,
                FilterEventTracking.Category.FILTER_SHOP,
                "",
                FilterEventTracking.Category.PREFIX_SEARCH_RESULT_PAGE
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_result_shop_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews()
        observeViewModelData()

        searchShopViewModel?.onViewCreated()
    }

    private fun initViewModel() {
        activity?.let { activity ->
            searchShopViewModel = ViewModelProviders.of(activity).get(SearchShopViewModel::class.java)
            searchViewModel = ViewModelProviders.of(activity).get(SearchViewModel::class.java)
        }
    }

    private fun initViews() {
        initRefreshLayout()
        initGridLayoutManager()
        initLoadMoreListener()
        initRecyclerView()
    }

    private fun initRefreshLayout() {
        refreshLayout = view?.findViewById(R.id.swipeRefreshLayoutSearchShop)

        refreshLayout?.setOnRefreshListener {
            searchShopViewModel?.onViewReloadData()
        }
    }

    private fun initGridLayoutManager() {
        gridLayoutManager = GridLayoutManager(activity, 1)
    }

    private fun initLoadMoreListener() {
        gridLayoutLoadMoreTriggerListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                searchShopViewModel?.onViewLoadMore(userVisibleHint)
            }
        }
    }

    private fun initRecyclerView() {
        activity?.let { activity ->
            initShopListAdapter()

            recyclerViewSearchShop = view?.findViewById(R.id.recyclerViewSearchShop)
            recyclerViewSearchShop?.layoutManager = gridLayoutManager
            recyclerViewSearchShop?.adapter = shopListAdapter
            recyclerViewSearchShop?.addItemDecoration(createShopItemDecoration(activity))
            gridLayoutLoadMoreTriggerListener?.let {
                recyclerViewSearchShop?.addOnScrollListener(it)
            }
        }
    }

    private fun initShopListAdapter() {
        val shopListTypeFactory = createShopListTypeFactory()
        shopListAdapter = ShopListAdapter(shopListTypeFactory)
    }

    private fun createShopListTypeFactory(): ShopListTypeFactory {
        return ShopListTypeFactoryImpl(this, this, this)
    }

    private fun createShopItemDecoration(activity: Activity): RecyclerView.ItemDecoration {
        return ShopListItemDecoration(
                activity.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                activity.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                activity.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                activity.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
        )
    }

    private fun observeViewModelData() {
        observeSearchShopLiveData()
        observeGetDynamicFilterEvent()
        observeTrackingOpenFilterPageEvent()
        observeOpenFilterPageEvent()
        observeShopCountLiveData()
        observeTrackingShopItemImpressionEvent()
        observeTrackingProductPreviewImpressionEvent()
        observeTrackingEmptySearchEvent()
        observePerformanceMonitoringEvent()
        observeRoutePageEvent()
        observeTrackingImpressionShopRecommendation()
        observeTrackingImpressionShopRecommendationProduct()
        observeTrackingClickShopItem()
        observeTrackingClickNotActiveShop()
        observeTrackingClickShopRecommendation()
        observeTrackingClickProductItem()
        observeTrackingClickProductRecommendation()
        observeQuickFilterLiveData()
        observeTrackingClickQuickFilterEvent()
        observeRefreshLayoutVisibility()
        observeShimmeringLayoutVisibility()
        observeQuickFilterVisibility()
        observeActiveFilterCount()
    }

    private fun observeSearchShopLiveData() {
        searchShopViewModel?.getSearchShopLiveData()?.observe(viewLifecycleOwner, Observer {
            updateAdapter(it)
        })
    }

    private fun updateAdapter(searchShopLiveData: State<List<Visitable<*>>>?) {
        when(searchShopLiveData) {
            is State.Loading -> {
                showRefreshLayout()
                updateList(searchShopLiveData)
                updateScrollListener()
            }
            is State.Success -> {
                hideRefreshLayout()
                updateList(searchShopLiveData)
                updateScrollListener()
                hideSearchPageLoading()
            }
            is State.Error -> {
                hideRefreshLayout()
                showRetryLayout(searchShopLiveData)
                hideSearchPageLoading()
            }
        }
    }

    private fun showRefreshLayout() {
        refreshLayout?.isRefreshing = true
    }

    private fun hideRefreshLayout() {
        refreshLayout?.isRefreshing = false
    }

    private fun updateList(searchShopLiveData: State<List<Visitable<*>>>) {
        shopListAdapter?.updateList(searchShopLiveData.data ?: listOf())
    }

    private fun updateScrollListener() {
        gridLayoutLoadMoreTriggerListener?.updateStateAfterGetData()
        gridLayoutLoadMoreTriggerListener?.setHasNextPage(searchShopViewModel?.getHasNextPage() ?: false)
    }

    private fun hideSearchPageLoading() {
        searchViewModel?.hideSearchPageLoading()
    }

    private fun showRetryLayout(searchShopLiveData: State<List<Visitable<*>>>) {
        activity?.let { activity ->
            val retryClickedListener = NetworkErrorHelper.RetryClickedListener {
                searchShopViewModel?.onViewClickRetry()
            }

            if (isSearchShopDataEmpty(searchShopLiveData)) {
                showNetworkErrorOnEmptyList(activity, retryClickedListener)
            } else {
                showNetworkErrorOnLoadMore(activity, retryClickedListener)
            }
        }
    }

    private fun isSearchShopDataEmpty(searchShopLiveData: State<List<Visitable<*>>>): Boolean {
        return searchShopLiveData.data?.size == 0
    }

    private fun showNetworkErrorOnEmptyList(activity: Activity, retryClickedListener: NetworkErrorHelper.RetryClickedListener) {
        hideViewOnError()

        NetworkErrorHelper.showEmptyState(activity, view, retryClickedListener)
    }

    private fun hideViewOnError() {
        searchShopQuickSortFilter?.hide()
        shimmeringViewShopQuickFilter?.hide()
        refreshLayout?.hide()
    }

    private fun showNetworkErrorOnLoadMore(activity: Activity, retryClickedListener: NetworkErrorHelper.RetryClickedListener) {
        NetworkErrorHelper.createSnackbarWithAction(activity, retryClickedListener).showRetrySnackbar()
    }

    private fun observeGetDynamicFilterEvent() {
        searchShopViewModel?.getDynamicFilterEventLiveData()?.observe(viewLifecycleOwner, EventObserver { isSuccessGetDynamicFilter ->
            handleEventGetDynamicFilter(isSuccessGetDynamicFilter)
        })
    }

    private fun handleEventGetDynamicFilter(isSuccessGetDynamicFilter: Boolean) {
        activity?.let { activity ->
            if (!isSuccessGetDynamicFilter) {
                NetworkErrorHelper.showSnackbar(activity, activity.getString(R.string.error_get_dynamic_filter))
            }
        }
    }

    private fun observeTrackingOpenFilterPageEvent() {
        searchShopViewModel?.getOpenFilterPageTrackingEventLiveData()?.observe(viewLifecycleOwner, Observer {
            FilterTracking.eventOpenFilterPage(filterTrackingData)
        })
    }

    private fun observeOpenFilterPageEvent() {
        searchShopViewModel?.getOpenFilterPageEventLiveData()?.observe(viewLifecycleOwner, EventObserver { isSuccessOpenFilterPage ->
            handleEventOpenFilterPage(isSuccessOpenFilterPage)
        })
    }

    private fun handleEventOpenFilterPage(isSuccessOpenFilterPage: Boolean) {
        if (isSuccessOpenFilterPage) {
            openFilterPage()
        }
        else {
            activity?.let { activity ->
                NetworkErrorHelper.showSnackbar(activity, activity.getString(R.string.error_filter_data_not_ready))
            }
        }
    }

    private fun openFilterPage() {
        sortFilterBottomSheet = SortFilterBottomSheet()

        sortFilterBottomSheet?.setOnDismissListener {
            sortFilterBottomSheet = null
        }

        sortFilterBottomSheet?.show(
                requireFragmentManager(),
                searchShopViewModel?.getSearchParameter().convertValuesToString(),
                searchShopViewModel?.dynamicFilterModel,
                this
        )
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        FilterTracking.eventApplyFilter(filterTrackingData, screenName, applySortFilterModel.selectedFilterMapParameter)
        searchShopViewModel?.onViewApplyFilter(applySortFilterModel.mapParameter)
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        searchShopViewModel?.onViewRequestShopCount(mapParameter)
    }

    private fun observeShopCountLiveData() {
        searchShopViewModel?.getShopCountLiveData()?.observe(viewLifecycleOwner, Observer {
            sortFilterBottomSheet?.setResultCountText(String.format(getString(R.string.shop_apply_filter), it))
        })
    }

    private fun observeTrackingShopItemImpressionEvent() {
        searchShopViewModel?.getShopItemImpressionTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver { trackingObjectList ->
            trackEventShopItemImpression(trackingObjectList)
        })
    }

    private fun trackEventShopItemImpression(trackingObjectList: List<Any>) {
        val keyword = searchShopViewModel?.getSearchParameterQuery()
        SearchTracking.trackImpressionSearchResultShop(trackingObjectList, keyword)
    }

    private fun observeTrackingProductPreviewImpressionEvent() {
        searchShopViewModel?.getProductPreviewImpressionTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver { trackingObjectList ->
            trackEventProductPreviewImpression(trackingObjectList)
        })
    }

    private fun trackEventProductPreviewImpression(trackingObjectList: List<Any>) {
        val keyword = searchShopViewModel?.getSearchParameterQuery()
        SearchTracking.eventImpressionSearchResultShopProductPreview(trackingObjectList, keyword)
    }

    private fun observeTrackingEmptySearchEvent() {
        searchShopViewModel?.getEmptySearchTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver { isTrackEmptySearch ->
            trackEventEmptySearch(isTrackEmptySearch)
        })
    }

    private fun trackEventEmptySearch(isTrackEmptySearch: Boolean) {
        if (isTrackEmptySearch) {
            activity?.let { activity ->
                val keyword = searchShopViewModel?.getSearchParameterQuery()
                val selectedFilterMap = searchShopViewModel?.getActiveFilterMapForEmptySearchTracking() ?: mapOf()
                SearchTracking.eventSearchNoResult(activity, keyword, screenName, selectedFilterMap)
            }
        }
    }

    private fun observePerformanceMonitoringEvent() {
        searchShopViewModel?.getSearchShopFirstPagePerformanceMonitoringEventLiveData()?.observe(viewLifecycleOwner, EventObserver { isStartPerformanceMonitoring ->
            triggerPerformanceMonitoring(isStartPerformanceMonitoring)
        })
    }

    private fun triggerPerformanceMonitoring(isStartPerformanceMonitoring: Boolean) {
        if (isStartPerformanceMonitoring) {
            startPerformanceMonitoring()
        }
        else {
            stopPerformanceMonitoring()
        }
    }

    private fun startPerformanceMonitoring() {
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_SHOP_TRACE)
    }

    private fun stopPerformanceMonitoring() {
        performanceMonitoring?.stopTrace()

        performanceMonitoring = null
    }

    private fun observeRoutePageEvent() {
        searchShopViewModel?.getRoutePageEventLiveData()?.observe(viewLifecycleOwner, EventObserver { applink ->
            route(applink)
        })
    }

    private fun route(applink: String) {
        activity?.let { activity ->
            if (applink.isNotEmpty()) {
                RouteManager.route(activity, applink)
            }
        }
    }

    private fun observeTrackingImpressionShopRecommendation() {
        searchShopViewModel?.getShopRecommendationItemImpressionTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver { trackingObjectList ->
            trackEventImpressionShopRecommendation(trackingObjectList)
        })
    }

    private fun trackEventImpressionShopRecommendation(trackingObjectList: List<Any>) {
        val keyword = searchShopViewModel?.getSearchParameterQuery()
        SearchTracking.trackEventImpressionShopRecommendation(trackingObjectList, keyword)
    }

    private fun observeTrackingImpressionShopRecommendationProduct() {
        searchShopViewModel?.getShopRecommendationProductPreviewImpressionTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver { trackingObjectList ->
            trackEventImpressionShopRecommendationProductPreview(trackingObjectList)
        })
    }

    private fun trackEventImpressionShopRecommendationProductPreview(trackingObjectList: List<Any>) {
        val keyword = searchShopViewModel?.getSearchParameterQuery()
        SearchTracking.trackEventImpressionShopRecommendationProductPreview(trackingObjectList, keyword)
    }

    private fun observeTrackingClickShopItem() {
        searchShopViewModel?.getClickShopItemTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver { shopItem ->
            trackEventClickShopItem(shopItem)
        })
    }

    private fun trackEventClickShopItem(shopDataItem: ShopDataView.ShopItem) {
        val keyword = searchShopViewModel?.getSearchParameterQuery() ?: ""
        SearchTracking.eventSearchResultShopItemClick(shopDataItem.getShopAsObjectDataLayer(), shopDataItem.id, keyword)
    }

    private fun observeTrackingClickNotActiveShop() {
        searchShopViewModel?.getClickNotActiveShopItemTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver { shopItem ->
            trackEventClickNotActiveShop(shopItem)
        })
    }

    private fun trackEventClickNotActiveShop(shopDataItem: ShopDataView.ShopItem) {
        val keyword = searchShopViewModel?.getSearchParameterQuery() ?: ""
        SearchTracking.eventSearchResultShopItemClosedClick(shopDataItem.getShopAsObjectDataLayer(), shopDataItem.id, keyword)
    }

    private fun observeTrackingClickShopRecommendation() {
        searchShopViewModel?.getClickShopRecommendationItemTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver { shopItem ->
            trackEventClickShopRecommendation(shopItem)
        })
    }

    private fun trackEventClickShopRecommendation(shopDataItem: ShopDataView.ShopItem) {
        val keyword = searchShopViewModel?.getSearchParameterQuery() ?: ""
        SearchTracking.trackEventClickShopRecommendation(shopDataItem.getShopRecommendationAsObjectDataLayer(), shopDataItem.id, keyword)
    }

    private fun observeTrackingClickProductItem() {
        searchShopViewModel?.getClickProductItemTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver { shopItemProduct ->
            trackEventClickProductItem(shopItemProduct)
        })
    }

    private fun trackEventClickProductItem(shopDataItemProduct: ShopDataView.ShopItem.ShopItemProduct) {
        val keyword = searchShopViewModel?.getSearchParameterQuery() ?: ""
        SearchTracking.eventSearchResultShopProductPreviewClick(shopDataItemProduct.getShopProductPreviewAsObjectDataLayer(), keyword)
    }

    private fun observeTrackingClickProductRecommendation() {
        searchShopViewModel?.getClickProductRecommendationItemTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver { shopItemProduct ->
            trackEventClickProductRecommendation(shopItemProduct)
        })
    }

    private fun trackEventClickProductRecommendation(shopDataItemProduct: ShopDataView.ShopItem.ShopItemProduct) {
        val keyword = searchShopViewModel?.getSearchParameterQuery() ?: ""
        SearchTracking.trackEventClickShopRecommendationProductPreview(shopDataItemProduct.getShopRecommendationProductPreviewAsObjectDataLayer(), keyword)
    }

    private fun observeQuickFilterLiveData() {
        searchShopViewModel?.getSortFilterItemListLiveData()?.observe(viewLifecycleOwner, Observer {
            showQuickFilterView(it)
        })
    }

    private fun showQuickFilterView(sortFilterItemList: List<SortFilterItem>?) {
        if (sortFilterItemList == null) return

        searchShopQuickSortFilter?.let {
            it.sortFilterItems.removeAllViews()
            it.visible()
            it.sortFilterHorizontalScrollView.scrollX = 0
            it.addItem(sortFilterItemList as ArrayList<SortFilterItem>)
            it.textView?.text = getString(R.string.search_filter)
            it.parentListener = {
                searchShopViewModel?.onViewOpenFilterPage()
            }
        }
    }

    private fun observeTrackingClickQuickFilterEvent() {
        searchShopViewModel?.getClickQuickFilterTrackingEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            val userId = searchShopViewModel?.getUserId() ?: "0"
            SearchTracking.trackEventClickQuickFilter(it.option.key, it.option.value, it.isSelected, userId)
        })
    }

    private fun observeRefreshLayoutVisibility() {
        searchShopViewModel?.getRefreshLayoutIsVisibleLiveData()?.observe(viewLifecycleOwner, Observer {
            refreshLayout?.showWithCondition(it)
        })
    }

    private fun observeShimmeringLayoutVisibility() {
        searchShopViewModel?.getShimmeringQuickFilterIsVisibleLiveData()?.observe(viewLifecycleOwner, Observer {
            shimmeringViewShopQuickFilter?.showWithCondition(it)
        })
    }

    private fun observeQuickFilterVisibility() {
        searchShopViewModel?.getQuickFilterIsVisibleLiveData()?.observe(viewLifecycleOwner, Observer {
            searchShopQuickSortFilter?.showWithCondition(it)
        })
    }

    private fun observeActiveFilterCount() {
        searchShopViewModel?.getActiveFilterCountLiveData()?.observe(viewLifecycleOwner, Observer {
            searchShopQuickSortFilter?.indicatorCounter = it
        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        trackScreen()

        searchViewModel?.changeBottomNavigationVisibility(false)
        searchShopViewModel?.onViewVisibilityChanged(isVisibleToUser, isAdded)
    }

    private fun trackScreen() {
        if (userVisibleHint) {
            SearchTracking.screenTrackSearchSectionFragment(screenName)
        }
    }

    override fun getScreenName(): String {
        return SearchShopViewModel.SCREEN_SEARCH_PAGE_SHOP_TAB
    }

    override fun onItemClicked(shopDataItem: ShopDataView.ShopItem) {
        searchShopViewModel?.onViewClickShop(shopDataItem)
    }

    override fun onProductItemClicked(shopDataItemProduct: ShopDataView.ShopItem.ShopItemProduct) {
        searchShopViewModel?.onViewClickProductPreview(shopDataItemProduct)
    }

    override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
        if (applink == null) return

        trackBannerAdsClick(position, applink, data)
        route(applink)
    }

    private fun trackBannerAdsClick(position: Int, applink: String, data: CpmData?) {
        if (applink.contains(SHOP)) {
            TopAdsGtmTracker.eventSearchResultPromoShopClick(activity, data, position)
        } else {
            TopAdsGtmTracker.eventSearchResultPromoProductClick(activity, data, position)
        }
    }

    override fun onBannerAdsImpressionListener(position: Int, data: CpmData?) {
        TopAdsGtmTracker.eventSearchResultPromoView(activity, data, position)
    }

    override fun onEmptyButtonClicked() {
        SearchTracking.eventUserClickNewSearchOnEmptySearch(context, screenName)
        searchViewModel?.showAutoCompleteView()
    }

    override fun onSelectedFilterRemoved(uniqueId: String?) {
        searchShopViewModel?.onViewRemoveSelectedFilterAfterEmptySearch(uniqueId)
    }

    override fun onEmptySearchToGlobalSearchClicked(applink: String?) {
        // No implementation here
    }

    override fun getRegistrationId(): String {
        return searchShopViewModel?.getRegistrationId() ?: ""
    }

    override fun getUserId(): String {
        return searchShopViewModel?.getUserId() ?: ""
    }

    override fun getSelectedFilterAsOptionList(): List<Option> {
        val activeFilterOptionList = searchShopViewModel?.getActiveFilterOptionListForEmptySearch() ?: return mutableListOf()

        return OptionHelper.combinePriceFilterIfExists(
                    activeFilterOptionList,
                    resources.getString(R.string.empty_state_selected_filter_price_name)
            )
    }

    fun backToTop() {
        recyclerViewSearchShop?.smoothScrollToPosition(0)
    }

    override fun configure(shouldRemove: Boolean) {
        if (shouldRemove) removeQuickFilterElevation(searchShopQuickSortFilter)
        else applyQuickFilterElevation(context, searchShopQuickSortFilter)
    }
}

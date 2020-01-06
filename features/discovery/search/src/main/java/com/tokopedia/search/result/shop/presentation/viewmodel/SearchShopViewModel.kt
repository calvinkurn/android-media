package com.tokopedia.search.result.shop.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_ID
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.search.result.common.Event
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.common.State.*
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopCpmViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopRecommendationTitleViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopTotalCountViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.utils.convertValuesToString
import com.tokopedia.topads.sdk.domain.model.Cpm
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface

internal class SearchShopViewModel(
        dispatcher: DispatcherProvider,
        searchParameter: Map<String, Any>,
        private val searchShopFirstPageUseCase: UseCase<SearchShopModel>,
        private val searchShopLoadMoreUseCase: UseCase<SearchShopModel>,
        private val getDynamicFilterUseCase: UseCase<DynamicFilterModel>,
        private val shopCpmViewModelMapper: Mapper<SearchShopModel, ShopCpmViewModel>,
        private val shopTotalCountViewModelMapper: Mapper<SearchShopModel, ShopTotalCountViewModel>,
        private val shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
        private val searchLocalCacheHandler: SearchLocalCacheHandler,
        private val userSession: UserSessionInterface,
        private val localCacheHandler: LocalCacheHandler
) : BaseViewModel(dispatcher.ui()) {

    companion object {
        const val START_ROW_FIRST_TIME_LOAD = 0
        const val SHOP_TAB_TITLE = "Toko"
        const val SCREEN_SEARCH_PAGE_SHOP_TAB = "Search result - Store tab"
    }

    private val searchShopLiveData = MutableLiveData<State<List<Visitable<*>>>>()
    private val searchShopMutableList = mutableListOf<Visitable<*>>()
    private val searchParameter = searchParameter.toMutableMap()
    private val loadingMoreModel = LoadingMoreModel()
    private var hasLoadData = false
    private var hasNextPage = false
    private var isEmptySearchShop = false
    private var isFilterDataAvailable = false
    private val filterController = FilterController()
    private val dynamicFilterEventLiveData = MutableLiveData<Event<Boolean>>()
    private val openFilterPageEventLiveData = MutableLiveData<Event<Boolean>>()
    private val shopItemImpressionTrackingEventLiveData = MutableLiveData<Event<List<Any>>>()
    private val productPreviewImpressionTrackingEventLiveData = MutableLiveData<Event<List<Any>>>()
    private val emptySearchTrackingEventLiveData = MutableLiveData<Event<Boolean>>()
    private val searchShopFirstPagePerformanceMonitoringEventLiveData = MutableLiveData<Event<Boolean>>()
    private val shopRecommendationItemImpressionTrackingEventLiveData = MutableLiveData<Event<List<Any>>>()
    private val shopRecommendationProductPreviewImpressionTrackingEventLiveData = MutableLiveData<Event<List<Any>>>()
    private val clickShopItemTrackingEventLiveData = MutableLiveData<Event<ShopViewModel.ShopItem>>()
    private val clickNotActiveShopItemTrackingEventLiveData = MutableLiveData<Event<ShopViewModel.ShopItem>>()
    private val clickShopRecommendationItemTrackingEventLiveData = MutableLiveData<Event<ShopViewModel.ShopItem>>()
    private val routePageEventLiveData = MutableLiveData<Event<String>>()
    private val clickProductItemTrackingEventLiveData = MutableLiveData<Event<ShopViewModel.ShopItem.ShopItemProduct>>()
    private val clickProductRecommendationItemTrackingEventLiveData = MutableLiveData<Event<ShopViewModel.ShopItem.ShopItemProduct>>()

    init {
        setSearchParameterUniqueId()
        setSearchParameterUserId()
        setSearchParameterStartRow(START_ROW_FIRST_TIME_LOAD)
    }

    private fun setSearchParameterUniqueId() {
        this.searchParameter[SearchApiConst.UNIQUE_ID] = getUniqueId()
    }

    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn)
            AuthHelper.getMD5Hash(userSession.userId)
        else
            AuthHelper.getMD5Hash(getRegistrationId())
    }

    fun getRegistrationId(): String {
        return localCacheHandler.getString(GCM_ID, "")
    }

    private fun setSearchParameterUserId() {
        this.searchParameter[SearchApiConst.USER_ID] = getUserId()
    }

    fun getUserId(): String {
        return if (userSession.isLoggedIn) userSession.userId
        else "0"
    }

    private fun setSearchParameterStartRow(startRow: Int) {
        searchParameter[SearchApiConst.START] = startRow
    }

    fun onViewCreated() {
        if (shouldLoadDataOnViewCreated() && !hasLoadData) {
            hasLoadData = true
            searchShop()
        }
    }

    private fun shouldLoadDataOnViewCreated(): Boolean {
        return searchParameter[SearchApiConst.ACTIVE_TAB] == SearchConstant.ActiveTab.SHOP
    }

    fun onViewVisibilityChanged(isViewVisible: Boolean, isViewAdded: Boolean) {
        if (isViewVisible && isViewAdded && !hasLoadData) {
            hasLoadData = true
            searchShop()
        }
    }

    private fun searchShop() {
        startSearchShopFirstPagePerformanceMonitoring()

        updateSearchShopLiveDataStateToLoading()

        setSearchParameterStartRow(START_ROW_FIRST_TIME_LOAD)

        val requestParams = createSearchShopParam()

        searchShopFirstPageUseCase.execute(this::onSearchShopSuccess, this::catchSearchShopException, requestParams)
    }

    private fun startSearchShopFirstPagePerformanceMonitoring() {
        searchShopFirstPagePerformanceMonitoringEventLiveData.postValue(Event(true))
    }

    private fun updateSearchShopLiveDataStateToLoading() {
        searchShopLiveData.postValue(Loading())
    }

    private fun onSearchShopSuccess(searchShopModel: SearchShopModel) {
        processSearchShopFirstPageSuccess(searchShopModel)

        endSearchShopFirstPagePerformanceMonitoring()

        getDynamicFilter()
    }

    private fun createSearchShopParam(): RequestParams {
        val requestParams = RequestParams.create()

        putRequestParamsParameters(requestParams)
        requestParams.putAll(searchParameter)

        return requestParams
    }

    private fun putRequestParamsParameters(requestParams: RequestParams) {
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)
        requestParams.putString(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        requestParams.putString(SearchApiConst.ROWS, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS)
        requestParams.putString(SearchApiConst.IMAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE)
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE)
    }

    private fun processSearchShopFirstPageSuccess(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) return

        updateIsHasNextPage(searchShopModel)
        updateIsSearchShopEmpty(searchShopModel)

        val visitableList = createVisitableListFromModel(searchShopModel)

        updateSearchShopListWithNewData(visitableList)
        updateSearchShopLiveDataStateToSuccess()

        postImpressionTrackingEvent(visitableList)
        postEmptySearchTrackingEvent()
        postRecommendationItemTrackingEvent(visitableList)
    }

    private fun updateIsHasNextPage(searchShopModel: SearchShopModel) {
        hasNextPage = searchShopModel.aceSearchShop.paging.uriNext.isNotEmpty()
    }

    private fun updateIsSearchShopEmpty(searchShopModel: SearchShopModel) {
        isEmptySearchShop = searchShopModel.aceSearchShop.shopList.isEmpty()
    }

    private fun createVisitableListFromModel(searchShopModel: SearchShopModel): List<Visitable<*>> {
        return if (isEmptySearchShop) {
            createSearchShopEmptyResultList(searchShopModel)
        }
        else {
            createSearchShopListWithHeader(searchShopModel)
        }
    }

    private fun createSearchShopEmptyResultList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        val emptySearchViewModel = ShopEmptySearchViewModel(SHOP_TAB_TITLE, getSearchParameterQuery(), false)
        visitableList.add(emptySearchViewModel)

        if (hasShopRecommendation(searchShopModel)) {
            val shopRecommendationVisitableList = createShopRecommendationVisitableList(searchShopModel)
            visitableList.addAll(shopRecommendationVisitableList)
        }

        return visitableList
    }

    private fun hasShopRecommendation(searchShopModel: SearchShopModel): Boolean {
        return searchShopModel.aceSearchShop.topShopList.isNotEmpty()
    }

    private fun createShopRecommendationVisitableList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val shopRecommendationVisitableList = mutableListOf<Visitable<*>>()

        val recommendationTitleViewModel = ShopRecommendationTitleViewModel()
        shopRecommendationVisitableList.add(recommendationTitleViewModel)

        val searchShopViewModel = shopViewModelMapper.convert(searchShopModel)
        setShopItemPositionWithStartPosition(0, searchShopViewModel.recommendationShopItemList)
        shopRecommendationVisitableList.addAll(searchShopViewModel.recommendationShopItemList)

        return shopRecommendationVisitableList
    }

    private fun createSearchShopListWithHeader(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        val shouldShowCpmShop = shouldShowCpmShop(searchShopModel)
        if (shouldShowCpmShop) {
            val shopCpmViewModel = createShopCpmViewModel(searchShopModel)
            visitableList.add(shopCpmViewModel)
        }

        val shopTotalCountViewModel = createShopTotalCountViewModel(searchShopModel, shouldShowCpmShop)
        visitableList.add(shopTotalCountViewModel)

        val shopViewModelList = createShopItemViewModelList(searchShopModel)
        visitableList.addAll(shopViewModelList)

        addLoadingMoreModel(visitableList)

        return visitableList
    }

    private fun shouldShowCpmShop(searchShopModel: SearchShopModel): Boolean {
        if (searchShopModel.cpmModel.data.size <= 0) return false

        val cpm = searchShopModel.cpmModel.data?.first()?.cpm ?: return false

        return if (isViewWillRenderCpmShop(cpm)) true
        else isViewWillRenderCpmDigital(cpm)
    }

    private fun isViewWillRenderCpmShop(cpm: Cpm): Boolean {
        return cpm.cpmShop != null
                && cpm.cta.isNotEmpty()
                && cpm.promotedText.isNotEmpty()
    }

    private fun isViewWillRenderCpmDigital(cpm: Cpm): Boolean {
        return cpm.templateId == 4
    }

    private fun createShopCpmViewModel(searchShopModel: SearchShopModel): Visitable<*> {
        return shopCpmViewModelMapper.convert(searchShopModel)
    }

    private fun createShopTotalCountViewModel(searchShopModel: SearchShopModel, isAdsBannerVisible: Boolean): Visitable<*> {
        val shopTotalCountViewModel = shopTotalCountViewModelMapper.convert(searchShopModel)
        shopTotalCountViewModel.query = getSearchParameterQuery()
        shopTotalCountViewModel.isAdsBannerVisible = isAdsBannerVisible

        return shopTotalCountViewModel
    }

    private fun createShopItemViewModelList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val shopViewModel = shopViewModelMapper.convert(searchShopModel)
        setShopItemPosition(shopViewModel.shopItemList)

        return shopViewModel.shopItemList
    }

    private fun setShopItemPosition(shopViewItemList: List<ShopViewModel.ShopItem>) {
        setShopItemPositionWithStartPosition(getSearchParameterStartRow(), shopViewItemList)
    }

    private fun setShopItemPositionWithStartPosition(startPosition: Int, shopViewItemList: List<ShopViewModel.ShopItem>) {
        var position = startPosition
        for (shopItem in shopViewItemList) {
            position++
            shopItem.position = position
            setShopProductItemPosition(shopItem.productList)
        }
    }

    private fun getSearchParameterStartRow() = (searchParameter[SearchApiConst.START] ?: "").toString().toIntOrNull() ?: 0

    private fun setShopProductItemPosition(shopItemProductList: List<ShopViewModel.ShopItem.ShopItemProduct>) {
        for ((index, shopItemProduct) in shopItemProductList.withIndex()) {
            shopItemProduct.position = index + 1
        }
    }

    private fun addLoadingMoreModel(visitableList: MutableList<Visitable<*>>) {
        if (hasNextPage) {
            visitableList.add(loadingMoreModel)
        }
    }

    private fun updateSearchShopListWithNewData(visitableList: List<Visitable<*>>) {
        searchShopMutableList.remove(loadingMoreModel)
        searchShopMutableList.addAll(visitableList)
    }

    private fun updateSearchShopLiveDataStateToSuccess() {
        searchShopLiveData.postValue(Success(searchShopMutableList))
    }

    private fun postImpressionTrackingEvent(visitableList: List<Visitable<*>>) {
        val dataLayerShopItemList = mutableListOf<Any>()
        val dataLayerShopItemProductList = mutableListOf<Any>()

        for (shopItem in visitableList) {
            if (shopItem is ShopViewModel.ShopItem) {
                dataLayerShopItemList.add(shopItem.getShopAsObjectDataLayer())
                dataLayerShopItemProductList.addAll(createShopProductPreviewDataLayerObjectList(shopItem))
            }
        }

        shopItemImpressionTrackingEventLiveData.postValue(Event(dataLayerShopItemList))
        productPreviewImpressionTrackingEventLiveData.postValue(Event(dataLayerShopItemProductList))
    }

    private fun createShopProductPreviewDataLayerObjectList(shopItem: ShopViewModel.ShopItem): List<Any> {
        val dataLayerShopItemProductList = mutableListOf<Any>()

        shopItem.productList.forEach { productItem ->
            dataLayerShopItemProductList.add(productItem.getShopProductPreviewAsObjectDataLayerList())
        }

        return dataLayerShopItemProductList
    }

    private fun postEmptySearchTrackingEvent() {
        if (isEmptySearchShop) {
            emptySearchTrackingEventLiveData.postValue(Event(true))
        }
    }

    private fun postRecommendationItemTrackingEvent(visitableList: List<Visitable<*>>) {
        val dataLayerShopItemList = mutableListOf<Any>()
        val dataLayerShopItemProductList = mutableListOf<Any>()

        for (shopItem in visitableList) {
            if (shopItem is ShopViewModel.ShopItem) {
                dataLayerShopItemList.add(shopItem.getShopRecommendationAsObjectDataLayer())
                dataLayerShopItemProductList.addAll(createShopRecommendationProductPreviewDataLayerObjectList(shopItem))
            }
        }

        shopRecommendationItemImpressionTrackingEventLiveData.postValue(Event(dataLayerShopItemList))
        shopRecommendationProductPreviewImpressionTrackingEventLiveData.postValue(Event(dataLayerShopItemProductList))
    }

    private fun createShopRecommendationProductPreviewDataLayerObjectList(shopItem: ShopViewModel.ShopItem): List<Any> {
        val dataLayerShopItemProductList = mutableListOf<Any>()

        shopItem.productList.forEach { productItem ->
            dataLayerShopItemProductList.add(productItem.getShopRecommendationProductPreviewAsObjectDataLayerList())
        }

        return dataLayerShopItemProductList
    }

    private fun endSearchShopFirstPagePerformanceMonitoring() {
        searchShopFirstPagePerformanceMonitoringEventLiveData.postValue(Event(false))
    }

    private fun catchSearchShopException(e: Throwable?) {
        e?.printStackTrace()

        hasNextPage = false
        searchShopLiveData.postValue(Error("", searchShopMutableList))
    }

    private fun getDynamicFilter() {
        val requestParams = createGetDynamicFilterParams()

        getDynamicFilterUseCase.execute(this::onGetDynamicFilterSuccess, this::catchGetDynamicFilterException, requestParams)
    }

    private fun onGetDynamicFilterSuccess(dynamicFilterModel: DynamicFilterModel) {
        dynamicFilterEventLiveData.postValue(Event(true))

        isFilterDataAvailable = dynamicFilterModel.data.filter.isNotEmpty()

        saveDynamicFilterModel(dynamicFilterModel)
        processFilterData(dynamicFilterModel)
    }

    private fun createGetDynamicFilterParams(): RequestParams {
        val requestParams = RequestParams.create()

        requestParams.putAll(searchParameter)
        requestParams.putAll(generateParamsNetwork())
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SHOP)
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)

        return requestParams
    }

    private fun generateParamsNetwork(): Map<String, String> {
        return AuthHelper.generateParamsNetwork(
                userSession.userId,
                userSession.deviceId,
                mutableMapOf())
    }

    private fun saveDynamicFilterModel(dynamicFilterModel: DynamicFilterModel) {
        searchLocalCacheHandler.saveDynamicFilterModelLocally(SCREEN_SEARCH_PAGE_SHOP_TAB, dynamicFilterModel)
    }

    private fun processFilterData(dynamicFilterModel: DynamicFilterModel) {
        if (isEmptySearchShop) {
            processFilterIntoFilterController(dynamicFilterModel)
            updateEmptySearchViewModelWithFilter()
        }
    }

    private fun processFilterIntoFilterController(dynamicFilterModel: DynamicFilterModel) {
        dynamicFilterModel.data.filter.let { filterList ->
            val initializedFilterList = FilterHelper.initializeFilterList(filterList)
            filterController.initFilterController(searchParameter.convertValuesToString(), initializedFilterList)
        }
    }

    private fun updateEmptySearchViewModelWithFilter() {
        if (filterController.isFilterActive()) {
            updateEmptySearchInVisitableList()
            updateSearchShopLiveDataStateToSuccess()
        }
    }

    private fun updateEmptySearchInVisitableList() {
        val shopEmptySearchViewModel = searchShopMutableList.find { it is ShopEmptySearchViewModel } as ShopEmptySearchViewModel

        shopEmptySearchViewModel.isFilterActive = filterController.isFilterActive()
    }

    private fun catchGetDynamicFilterException(e: Throwable?) {
        e?.printStackTrace()

        dynamicFilterEventLiveData.postValue(Event(false))
    }

    fun onViewLoadMore(isViewVisible: Boolean) {
        if (hasNextPage && isViewVisible) {
            searchMoreShop()
        }
    }

    private fun searchMoreShop() {
        setSearchParameterStartRow(getTotalShopItemCount())

        val requestParams = createSearchShopParam()

        searchShopLoadMoreUseCase.execute(this::onSearchMoreShopSuccess, this::catchSearchShopException, requestParams)
    }

    private fun getTotalShopItemCount(): Int {
        return searchShopMutableList.count { it is ShopViewModel.ShopItem }
    }

    private fun onSearchMoreShopSuccess(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) return

        updateIsHasNextPage(searchShopModel)

        val visitableList = createSearchShopList(searchShopModel)

        updateSearchShopListWithNewData(visitableList)
        updateSearchShopLiveDataStateToSuccess()

        postImpressionTrackingEvent(visitableList)
    }

    private fun createSearchShopList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        val shopViewModelList = createShopItemViewModelList(searchShopModel)
        visitableList.addAll(shopViewModelList)

        addLoadingMoreModel(visitableList)

        return visitableList
    }

    fun onViewClickRetry() {
        if (isSearchShopLiveDataEmpty()) {
            searchShop()
        }
        else {
            searchMoreShop()
        }
    }

    private fun isSearchShopLiveDataEmpty(): Boolean {
        return searchShopMutableList.isEmpty()
    }

    fun onViewReloadData() {
        clearDataBeforeReload()
        searchShop()
    }

    private fun clearDataBeforeReload() {
        searchShopMutableList.clear()

        hasNextPage = false
        isEmptySearchShop = false
        isFilterDataAvailable = false
    }

    fun onViewOpenFilterPage() {
        if (isFilterDataAvailable) {
            openFilterPageEventLiveData.postValue(Event(true))
        }
        else {
            openFilterPageEventLiveData.postValue(Event(false))
        }
    }

    fun onViewApplyFilter(queryParameters: Map<String, String>?) {
        if (queryParameters == null) return

        applyFilterToSearchParameters(queryParameters)

        onViewReloadData()
    }

    private fun applyFilterToSearchParameters(queryParameters: Map<String, String>) {
        searchParameter.clear()
        searchParameter.putAll(queryParameters)
    }

    fun onViewRemoveSelectedFilterAfterEmptySearch(uniqueId: String?) {
        if (uniqueId == null) return
        if (!isEmptySearchShop) return

        removeFilterFromFilterController(uniqueId)

        onViewApplyFilter(filterController.getParameter())
    }

    private fun removeFilterFromFilterController(uniqueId: String) {
        val option = OptionHelper.generateOptionFromUniqueId(uniqueId)

        if (option.key == Option.KEY_CATEGORY) {
            filterController.setFilter(option, isFilterApplied = false, isCleanUpExistingFilterWithSameKey = true)
        }
        else if (option.key == Option.KEY_PRICE_MIN || option.key == Option.KEY_PRICE_MAX) {
            filterController.setFilter(createOptionWithKey(Option.KEY_PRICE_MIN), isFilterApplied = false, isCleanUpExistingFilterWithSameKey = true)
            filterController.setFilter(createOptionWithKey(Option.KEY_PRICE_MAX), isFilterApplied = false, isCleanUpExistingFilterWithSameKey = true)
        }
        else {
            filterController.setFilter(option, isFilterApplied = false)
        }
    }

    private fun createOptionWithKey(optionKey: String): Option {
        return Option().also {
            it.key = optionKey
        }
    }

    fun onViewClickShop(shopItem: ShopViewModel.ShopItem) {
        postClickNotActiveShopTrackingEvent(shopItem)
        postClickShopItemTrackingEvent(shopItem)
        postRoutePageEvent(shopItem.applink)
    }

    private fun postClickNotActiveShopTrackingEvent(shopItem: ShopViewModel.ShopItem) {
        if (isShopNotActive(shopItem)) {
            clickNotActiveShopItemTrackingEventLiveData.postValue(Event(shopItem))
        }
    }

    private fun isShopNotActive(shopItem: ShopViewModel.ShopItem): Boolean {
        return (shopItem.isClosed
                || shopItem.isModerated
                || shopItem.isInactive)
    }

    private fun postClickShopItemTrackingEvent(shopItem: ShopViewModel.ShopItem) {
        if (shopItem.isRecommendation) {
            clickShopRecommendationItemTrackingEventLiveData.postValue(Event(shopItem))
        }
        else {
            clickShopItemTrackingEventLiveData.postValue(Event(shopItem))
        }
    }

    private fun postRoutePageEvent(applink: String) {
        routePageEventLiveData.postValue(Event(applink))
    }

    fun onViewClickProductPreview(shopItemProduct: ShopViewModel.ShopItem.ShopItemProduct) {
        postRoutePageEvent(shopItemProduct.applink)
        postClickProductPreviewTrackingEvent(shopItemProduct)
    }

    private fun postClickProductPreviewTrackingEvent(shopItemProduct: ShopViewModel.ShopItem.ShopItemProduct) {
        if (shopItemProduct.isRecommendation) {
            clickProductRecommendationItemTrackingEventLiveData.postValue(Event(shopItemProduct))
        }
        else {
            clickProductItemTrackingEventLiveData.postValue(Event(shopItemProduct))
        }
    }

    fun getSearchParameter() = searchParameter.toMap()

    fun getSearchParameterQuery() : String = (searchParameter[SearchApiConst.Q] ?: "").toString()

    fun getSearchShopLiveData(): LiveData<State<List<Visitable<*>>>> {
        return searchShopLiveData
    }

    fun getHasNextPage(): Boolean {
        return hasNextPage
    }

    fun getDynamicFilterEventLiveData(): LiveData<Event<Boolean>> {
        return dynamicFilterEventLiveData
    }

    fun getOpenFilterPageEventLiveData(): LiveData<Event<Boolean>> {
        return openFilterPageEventLiveData
    }

    fun getActiveFilterOptionListForEmptySearch(): List<Option> {
        return filterController.getActiveFilterOptionList()
    }

    fun getShopItemImpressionTrackingEventLiveData(): LiveData<Event<List<Any>>> {
        return shopItemImpressionTrackingEventLiveData
    }

    fun getProductPreviewImpressionTrackingEventLiveData(): LiveData<Event<List<Any>>> {
        return productPreviewImpressionTrackingEventLiveData
    }

    fun getActiveFilterMapForEmptySearchTracking(): Map<String, String> {
        return filterController.getActiveFilterMap()
    }

    fun getEmptySearchTrackingEventLiveData(): LiveData<Event<Boolean>> {
        return emptySearchTrackingEventLiveData
    }

    fun getSearchShopFirstPagePerformanceMonitoringEventLiveData(): LiveData<Event<Boolean>> {
        return searchShopFirstPagePerformanceMonitoringEventLiveData
    }

    fun getShopRecommendationItemImpressionTrackingEventLiveData(): LiveData<Event<List<Any>>> {
        return shopRecommendationItemImpressionTrackingEventLiveData
    }

    fun getShopRecommendationProductPreviewImpressionTrackingEventLiveData(): LiveData<Event<List<Any>>> {
        return shopRecommendationProductPreviewImpressionTrackingEventLiveData
    }

    fun getClickShopItemTrackingEventLiveData(): LiveData<Event<ShopViewModel.ShopItem>> {
        return clickShopItemTrackingEventLiveData
    }
    fun getClickNotActiveShopItemTrackingEventLiveData(): LiveData<Event<ShopViewModel.ShopItem>> {
        return clickNotActiveShopItemTrackingEventLiveData
    }

    fun getClickShopRecommendationItemTrackingEventLiveData(): LiveData<Event<ShopViewModel.ShopItem>> {
        return clickShopRecommendationItemTrackingEventLiveData
    }

    fun getRoutePageEventLiveData(): LiveData<Event<String>> {
        return routePageEventLiveData
    }

    fun getClickProductItemTrackingEventLiveData(): LiveData<Event<ShopViewModel.ShopItem.ShopItemProduct>> {
        return clickProductItemTrackingEventLiveData
    }

    fun getClickProductRecommendationItemTrackingEventLiveData(): LiveData<Event<ShopViewModel.ShopItem.ShopItemProduct>> {
        return clickProductRecommendationItemTrackingEventLiveData
    }
}

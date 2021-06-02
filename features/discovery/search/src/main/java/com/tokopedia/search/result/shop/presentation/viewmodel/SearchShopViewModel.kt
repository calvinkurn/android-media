package com.tokopedia.search.result.shop.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.State.*
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.*
import com.tokopedia.search.utils.convertValuesToString
import com.tokopedia.search.utils.createSearchShopDefaultQuickFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.domain.model.Cpm
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy

internal class SearchShopViewModel(
        dispatcher: CoroutineDispatchers,
        searchParameter: Map<String, Any>,
        private val searchShopFirstPageUseCase: Lazy<UseCase<SearchShopModel>>,
        private val searchShopLoadMoreUseCase: Lazy<UseCase<SearchShopModel>>,
        private val getDynamicFilterUseCase: Lazy<UseCase<DynamicFilterModel>>,
        private val getShopCountUseCase: Lazy<UseCase<Int>>,
        private val shopCpmDataViewMapper: Lazy<Mapper<SearchShopModel, ShopCpmDataView>>,
        private val shopDataViewMapper: Lazy<Mapper<SearchShopModel, ShopDataView>>,
        private val userSession: Lazy<UserSessionInterface>
) : BaseViewModel(dispatcher.main) {

    companion object {
        const val START_ROW_FIRST_TIME_LOAD = 0
        const val SHOP_TAB_TITLE = "Toko"
        const val SCREEN_SEARCH_PAGE_SHOP_TAB = "Search result - Store tab"
        const val QUICK_FILTER_MINIMUM_SIZE = 2
    }

    private val searchShopLiveData = MutableLiveData<State<List<Visitable<*>>>>()
    private val searchShopMutableList = mutableListOf<Visitable<*>>()
    private val searchParameter = searchParameter.toMutableMap()
    private val loadingMoreModel = LoadingMoreModel()
    private var hasLoadData = false
    private var totalShopRetrieved = 0
    private var hasNextPage = false
    private var isEmptySearchShop = false
    private var isFilterDataAvailable = false
    private val filterController = FilterController()
    private val dynamicFilterEventLiveData = MutableLiveData<Event<Boolean>>()
    private val openFilterPageTrackingEventMutableLiveData = MutableLiveData<Event<Boolean>>()
    private val openFilterPageEventLiveData = MutableLiveData<Event<Boolean>>()
    private val shopItemImpressionTrackingEventLiveData = MutableLiveData<Event<List<Any>>>()
    private val productPreviewImpressionTrackingEventLiveData = MutableLiveData<Event<List<Any>>>()
    private val emptySearchTrackingEventLiveData = MutableLiveData<Event<Boolean>>()
    private val searchShopFirstPagePerformanceMonitoringEventLiveData = MutableLiveData<Event<Boolean>>()
    private val shopRecommendationItemImpressionTrackingEventLiveData = MutableLiveData<Event<List<Any>>>()
    private val shopRecommendationProductPreviewImpressionTrackingEventLiveData = MutableLiveData<Event<List<Any>>>()
    private val clickShopItemTrackingEventLiveData = MutableLiveData<Event<ShopDataView.ShopItem>>()
    private val clickNotActiveShopItemTrackingEventLiveData = MutableLiveData<Event<ShopDataView.ShopItem>>()
    private val clickShopRecommendationItemTrackingEventLiveData = MutableLiveData<Event<ShopDataView.ShopItem>>()
    private val routePageEventLiveData = MutableLiveData<Event<String>>()
    private val clickProductItemTrackingEventLiveData = MutableLiveData<Event<ShopDataView.ShopItem.ShopItemProduct>>()
    private val clickProductRecommendationItemTrackingEventLiveData = MutableLiveData<Event<ShopDataView.ShopItem.ShopItemProduct>>()
    private val sortFilterItemListLiveData = MutableLiveData<List<SortFilterItem>>()
    private val clickQuickFilterTrackingEventMutableLiveData = MutableLiveData<Event<QuickFilterTrackingData>>()
    private val quickFilterIsVisible = MutableLiveData<Boolean>()
    private val shimmeringQuickFilterIsVisible = MutableLiveData<Boolean>()
    private val refreshLayoutIsVisible = MutableLiveData<Boolean>()
    private val shopCountMutableLiveData = MutableLiveData<String>()
    private val activeFilterCountMutableLiveData = MutableLiveData<Int>()
    var dynamicFilterModel: DynamicFilterModel? = null

    init {
        setSearchParameterUniqueId()
        setSearchParameterUserId()
        setSearchParameterStartRow(START_ROW_FIRST_TIME_LOAD)
    }

    private fun setSearchParameterUniqueId() {
        this.searchParameter[SearchApiConst.UNIQUE_ID] = getUniqueId()
    }

    private fun getUniqueId(): String {
        return if (userSession.get().isLoggedIn)
            AuthHelper.getMD5Hash(userSession.get().userId)
        else
            AuthHelper.getMD5Hash(getRegistrationId())
    }

    fun getRegistrationId(): String {
        return userSession.get().deviceId
    }

    private fun setSearchParameterUserId() {
        this.searchParameter[SearchApiConst.USER_ID] = getUserId()
    }

    fun getUserId(): String {
        return if (userSession.get().isLoggedIn) userSession.get().userId
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

        searchShopFirstPageUseCase.get().execute(this::onSearchShopSuccess, this::catchSearchShopException, requestParams)
    }

    private fun startSearchShopFirstPagePerformanceMonitoring() {
        searchShopFirstPagePerformanceMonitoringEventLiveData.postValue(Event(true))
    }

    private fun updateSearchShopLiveDataStateToLoading() {
        searchShopLiveData.postValue(Loading())

        quickFilterIsVisible.value = false
        shimmeringQuickFilterIsVisible.value = true
        refreshLayoutIsVisible.value = true
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

    private fun onSearchShopSuccess(searchShopModel: SearchShopModel) {
        processSearchShopFirstPageSuccess(searchShopModel)

        endSearchShopFirstPagePerformanceMonitoring()

        getDynamicFilter()
    }

    private fun processSearchShopFirstPageSuccess(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) return

        updateSearchShopStatus(searchShopModel)

        val visitableList = createVisitableListFromModel(searchShopModel)

        updateSearchShopListWithNewData(visitableList)
        updateSearchShopLiveDataStateToSuccess()

        postLiveDataEventsAfterSearchShop(searchShopModel, visitableList)

        processQuickFilter(searchShopModel)
    }

    private fun updateSearchShopStatus(searchShopModel: SearchShopModel) {
        updateIsSearchShopEmpty(searchShopModel)
        updateTotalSearchShopRetrieved(searchShopModel)
        updateIsHasNextPage(searchShopModel)
    }

    private fun updateIsSearchShopEmpty(searchShopModel: SearchShopModel) {
        isEmptySearchShop = searchShopModel.aceSearchShop.shopList.isEmpty()
    }

    private fun updateTotalSearchShopRetrieved(searchShopModel: SearchShopModel) {
        val currentShopListSize = if (!isEmptySearchShop) {
            searchShopModel.aceSearchShop.shopList.size
        } else {
            searchShopModel.aceSearchShop.topShopList.size
        }

        totalShopRetrieved += currentShopListSize
    }

    private fun updateIsHasNextPage(searchShopModel: SearchShopModel) {
        hasNextPage = totalShopRetrieved < searchShopModel.aceSearchShop.totalShop
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

        val emptySearchViewModel = ShopEmptySearchDataView(SHOP_TAB_TITLE, getSearchParameterQuery(), false)
        visitableList.add(emptySearchViewModel)

        if (searchShopModel.hasRecommendationShopList()) {
            val shopRecommendationVisitableList = createShopRecommendationVisitableList(searchShopModel)
            visitableList.addAll(shopRecommendationVisitableList)
        }

        addLoadingMoreModel(visitableList)

        return visitableList
    }

    private fun createShopRecommendationVisitableList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val shopRecommendationVisitableList = mutableListOf<Visitable<*>>()

        val recommendationTitleViewModel = ShopRecommendationTitleDataView()
        shopRecommendationVisitableList.add(recommendationTitleViewModel)

        val searchShopViewModelList = createShopRecommendationItemViewModelList(searchShopModel)
        shopRecommendationVisitableList.addAll(searchShopViewModelList)

        return shopRecommendationVisitableList
    }

    private fun createShopRecommendationItemViewModelList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val shopViewModel = shopDataViewMapper.get().convert(searchShopModel)
        setShopItemPosition(shopViewModel.recommendationShopItemList)

        return shopViewModel.recommendationShopItemList
    }

    private fun createSearchShopListWithHeader(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        processCPM(searchShopModel, visitableList)
        processSuggestion(searchShopModel, visitableList)
        processShopItem(searchShopModel, visitableList)

        addLoadingMoreModel(visitableList)

        return visitableList
    }

    private fun processCPM(searchShopModel: SearchShopModel, visitableList: MutableList<Visitable<*>>) {
        val shouldShowCpmShop = shouldShowCpmShop(searchShopModel)

        if (shouldShowCpmShop) {
            val shopCpmViewModel = createShopCpmViewModel(searchShopModel)
            visitableList.add(shopCpmViewModel)
        }
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
        return shopCpmDataViewMapper.get().convert(searchShopModel)
    }

    private fun processSuggestion(searchShopModel: SearchShopModel, visitableList: MutableList<Visitable<*>>) {
        val suggestionModel = searchShopModel.aceSearchShop.suggestion
        val shouldShowSuggestion = suggestionModel.text.isNotEmpty()

        if (shouldShowSuggestion) {
            val suggestionViewModel = createSuggestionViewModel(suggestionModel)
            visitableList.add(suggestionViewModel)
        }
    }

    private fun createSuggestionViewModel(suggestionModel: SearchShopModel.AceSearchShop.Suggestion): ShopSuggestionDataView {
        return ShopSuggestionDataView(
                currentKeyword = suggestionModel.currentKeyword,
                query = suggestionModel.query,
                text = suggestionModel.text
        )
    }

    private fun processShopItem(searchShopModel: SearchShopModel, visitableList: MutableList<Visitable<*>>) {
        val shopViewModelList = createShopItemViewModelList(searchShopModel)
        visitableList.addAll(shopViewModelList)
    }

    private fun createShopItemViewModelList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val shopViewModel = shopDataViewMapper.get().convert(searchShopModel)
        setShopItemPosition(shopViewModel.shopItemList)

        return shopViewModel.shopItemList
    }

    private fun setShopItemPosition(shopDataViewItemList: List<ShopDataView.ShopItem>) {
        setShopItemPositionWithStartPosition(getSearchParameterStartRow(), shopDataViewItemList)
    }

    private fun setShopItemPositionWithStartPosition(startPosition: Int, shopDataViewItemList: List<ShopDataView.ShopItem>) {
        var position = startPosition
        for (shopItem in shopDataViewItemList) {
            position++
            shopItem.position = position
            setShopProductItemPosition(shopItem.productList)
        }
    }

    private fun getSearchParameterStartRow() = (searchParameter[SearchApiConst.START] ?: "").toString().toIntOrNull() ?: 0

    private fun setShopProductItemPosition(shopDataItemProductList: List<ShopDataView.ShopItem.ShopItemProduct>) {
        for ((index, shopItemProduct) in shopDataItemProductList.withIndex()) {
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

    private fun postLiveDataEventsAfterSearchShop(searchShopModel: SearchShopModel, visitableList: List<Visitable<*>>) {
        postImpressionTrackingEvent(searchShopModel, visitableList)
        postEmptySearchTrackingEvent()
        postRecommendationImpressionTrackingEvent(searchShopModel, visitableList)
    }

    private fun postImpressionTrackingEvent(searchShopModel: SearchShopModel, visitableList: List<Visitable<*>>) {
        if (!searchShopModel.hasShopList()) return

        val dataLayerShopItemList = mutableListOf<Any>()
        val dataLayerShopItemProductList = mutableListOf<Any>()

        for (shopItem in visitableList) {
            if (shopItem is ShopDataView.ShopItem) {
                dataLayerShopItemList.add(shopItem.getShopAsObjectDataLayer())
                dataLayerShopItemProductList.addAll(createShopProductPreviewDataLayerObjectList(shopItem))
            }
        }

        shopItemImpressionTrackingEventLiveData.postValue(Event(dataLayerShopItemList))
        productPreviewImpressionTrackingEventLiveData.postValue(Event(dataLayerShopItemProductList))
    }

    private fun createShopProductPreviewDataLayerObjectList(shopDataItem: ShopDataView.ShopItem): List<Any> {
        val dataLayerShopItemProductList = mutableListOf<Any>()

        shopDataItem.productList.forEach { productItem ->
            dataLayerShopItemProductList.add(productItem.getShopProductPreviewAsObjectDataLayerList())
        }

        return dataLayerShopItemProductList
    }

    private fun postEmptySearchTrackingEvent() {
        if (isEmptySearchShop) {
            emptySearchTrackingEventLiveData.postValue(Event(true))
        }
    }

    private fun postRecommendationImpressionTrackingEvent(searchShopModel: SearchShopModel, visitableList: List<Visitable<*>>) {
        if (!searchShopModel.hasRecommendationShopList()) return

        val dataLayerShopItemList = mutableListOf<Any>()
        val dataLayerShopItemProductList = mutableListOf<Any>()

        for (shopItem in visitableList) {
            if (shopItem is ShopDataView.ShopItem) {
                dataLayerShopItemList.add(shopItem.getShopRecommendationAsObjectDataLayer())
                dataLayerShopItemProductList.addAll(createShopRecommendationProductPreviewDataLayerObjectList(shopItem))
            }
        }

        shopRecommendationItemImpressionTrackingEventLiveData.postValue(Event(dataLayerShopItemList))
        shopRecommendationProductPreviewImpressionTrackingEventLiveData.postValue(Event(dataLayerShopItemProductList))
    }

    private fun createShopRecommendationProductPreviewDataLayerObjectList(shopDataItem: ShopDataView.ShopItem): List<Any> {
        val dataLayerShopItemProductList = mutableListOf<Any>()

        shopDataItem.productList.forEach { productItem ->
            dataLayerShopItemProductList.add(productItem.getShopRecommendationProductPreviewAsObjectDataLayerList())
        }

        return dataLayerShopItemProductList
    }

    private fun endSearchShopFirstPagePerformanceMonitoring() {
        searchShopFirstPagePerformanceMonitoringEventLiveData.postValue(Event(false))
    }

    private fun processQuickFilter(searchShopModel: SearchShopModel) {
        if (!isEmptySearchShop) {
            processQuickFilterNotEmptySearch(searchShopModel)
        }
        else {
            processQuickFilterEmptySearch()
        }
    }

    private fun processQuickFilterNotEmptySearch(searchShopModel: SearchShopModel) {
        if (searchShopModel.getFilterList().size < QUICK_FILTER_MINIMUM_SIZE)
            searchShopModel.quickFilter.data = createSearchShopDefaultQuickFilter()

        filterController.initFilterController(searchParameter.convertValuesToString(), searchShopModel.getFilterList())

        val sortFilterItemList = createSortFilterItemList(searchShopModel)
        sortFilterItemListLiveData.value = sortFilterItemList
        quickFilterIsVisible.value = true
        shimmeringQuickFilterIsVisible.value = false
    }

    private fun createSortFilterItemList(searchShopModel: SearchShopModel): List<SortFilterItem> {
        val quickFilterOptionList = searchShopModel.getFilterList().map { it.options }.flatten()

        return quickFilterOptionList.map(this::optionToSortFilterItem)
    }

    private fun optionToSortFilterItem(option: Option): SortFilterItem {
        val isSelected = filterController.getFilterViewState(option)
        val sortFilterItem = createSortFilterItem(option, isSelected)

        sortFilterItem.typeUpdated = false

        return sortFilterItem
    }

    private fun createSortFilterItem(option: Option, isSelected: Boolean): SortFilterItem {
        val type = if (isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

        return SortFilterItem(title = option.name, type = type) {
            sendTrackingClickQuickFilter(option, isSelected)

            filterController.setFilter(
                    option,
                    isFilterApplied = !isSelected,
                    isCleanUpExistingFilterWithSameKey = option.isCategoryOption
            )

            onViewApplyFilter(filterController.getParameter())
        }
    }

    private fun sendTrackingClickQuickFilter(option: Option, isSelected: Boolean) {
        val quickFilterTrackingData = QuickFilterTrackingData(option, !isSelected)
        clickQuickFilterTrackingEventMutableLiveData.postValue(Event(quickFilterTrackingData))
    }

    private fun processQuickFilterEmptySearch() {
        quickFilterIsVisible.value = false
        shimmeringQuickFilterIsVisible.value = false
    }

    private fun catchSearchShopException(e: Throwable?) {
        e?.printStackTrace()

        hasNextPage = false
        searchShopLiveData.postValue(Error("", searchShopMutableList))

        if (searchShopMutableList.isEmpty()) {
            quickFilterIsVisible.value = false
            shimmeringQuickFilterIsVisible.value = false
            refreshLayoutIsVisible.value = false
        }
    }

    private fun getDynamicFilter() {
        val requestParams = createGetDynamicFilterParams()

        getDynamicFilterUseCase.get().execute(this::onGetDynamicFilterSuccess, this::catchGetDynamicFilterException, requestParams)
    }

    private fun onGetDynamicFilterSuccess(dynamicFilterModel: DynamicFilterModel) {
        this.dynamicFilterModel = dynamicFilterModel
        dynamicFilterEventLiveData.postValue(Event(true))

        isFilterDataAvailable = dynamicFilterModel.data.filter.isNotEmpty()

        processFilterData()
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
                userSession.get().userId,
                userSession.get().deviceId,
                mutableMapOf())
    }

    private fun processFilterData() {
        processFilterIntoFilterController()

        activeFilterCountMutableLiveData.postValue(filterController.getFilterCount())

        if (isEmptySearchShop) {
            updateEmptySearchViewModelWithFilter()
        }
    }

    private fun processFilterIntoFilterController() {
        dynamicFilterModel?.data?.filter?.let { filterList ->
            val initializedFilterList = FilterHelper.initializeFilterList(filterList)
            filterController.appendFilterList(searchParameter.convertValuesToString(), initializedFilterList)
        }
    }

    private fun updateEmptySearchViewModelWithFilter() {
        if (filterController.isFilterActive()) {
            updateEmptySearchInVisitableList()
            updateSearchShopLiveDataStateToSuccess()
        }
    }

    private fun updateEmptySearchInVisitableList() {
        val shopEmptySearchViewModelIndex = searchShopMutableList.indexOfFirst { it is ShopEmptySearchDataView }
        searchShopMutableList[shopEmptySearchViewModelIndex] = ShopEmptySearchDataView(
                SHOP_TAB_TITLE, getSearchParameterQuery(), filterController.isFilterActive()
        )
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

        searchShopLoadMoreUseCase.get().execute(this::onSearchMoreShopSuccess, this::catchSearchShopException, requestParams)
    }

    private fun getTotalShopItemCount(): Int {
        return searchShopMutableList.count { it is ShopDataView.ShopItem }
    }

    private fun onSearchMoreShopSuccess(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) return

        updateTotalSearchShopRetrieved(searchShopModel)
        updateIsHasNextPage(searchShopModel)

        val visitableList = createSearchMoreShopVisitableList(searchShopModel)

        updateSearchShopListWithNewData(visitableList)
        updateSearchShopLiveDataStateToSuccess()

        postImpressionTrackingEvent(searchShopModel, visitableList)
        postRecommendationImpressionTrackingEvent(searchShopModel, visitableList)
    }

    private fun createSearchMoreShopVisitableList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        addSearchShopViewModelList(searchShopModel, visitableList)
        addLoadingMoreModel(visitableList)

        return visitableList
    }

    private fun addSearchShopViewModelList(searchShopModel: SearchShopModel, visitableList: MutableList<Visitable<*>>) {
        if (searchShopModel.hasRecommendationShopList()) {
            val searchShopViewModelList = createShopRecommendationItemViewModelList(searchShopModel)
            visitableList.addAll(searchShopViewModelList)
        }
        else {
            val shopViewModelList = createShopItemViewModelList(searchShopModel)
            visitableList.addAll(shopViewModelList)
        }
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

        totalShopRetrieved = 0
        hasNextPage = false
        isEmptySearchShop = false
        isFilterDataAvailable = false
    }

    fun onViewOpenFilterPage() {
        if (isFilterDataAvailable) {
            openFilterPageTrackingEventMutableLiveData.postValue(Event(true))
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

    fun onViewClickShop(shopDataItem: ShopDataView.ShopItem) {
        postClickNotActiveShopTrackingEvent(shopDataItem)
        postClickShopItemTrackingEvent(shopDataItem)
        postRoutePageEvent(shopDataItem.applink)
    }

    private fun postClickNotActiveShopTrackingEvent(shopDataItem: ShopDataView.ShopItem) {
        if (isShopNotActive(shopDataItem)) {
            clickNotActiveShopItemTrackingEventLiveData.postValue(Event(shopDataItem))
        }
    }

    private fun isShopNotActive(shopDataItem: ShopDataView.ShopItem): Boolean {
        return (shopDataItem.isClosed
                || shopDataItem.isModerated
                || shopDataItem.isInactive)
    }

    private fun postClickShopItemTrackingEvent(shopDataItem: ShopDataView.ShopItem) {
        if (shopDataItem.isRecommendation) {
            clickShopRecommendationItemTrackingEventLiveData.postValue(Event(shopDataItem))
        }
        else {
            clickShopItemTrackingEventLiveData.postValue(Event(shopDataItem))
        }
    }

    private fun postRoutePageEvent(applink: String) {
        routePageEventLiveData.postValue(Event(applink))
    }

    fun onViewClickProductPreview(shopDataItemProduct: ShopDataView.ShopItem.ShopItemProduct) {
        postRoutePageEvent(shopDataItemProduct.applink)
        postClickProductPreviewTrackingEvent(shopDataItemProduct)
    }

    private fun postClickProductPreviewTrackingEvent(shopDataItemProduct: ShopDataView.ShopItem.ShopItemProduct) {
        if (shopDataItemProduct.isRecommendation) {
            clickProductRecommendationItemTrackingEventLiveData.postValue(Event(shopDataItemProduct))
        }
        else {
            clickProductItemTrackingEventLiveData.postValue(Event(shopDataItemProduct))
        }
    }

    fun onViewRequestShopCount(mapParameter: Map<String, Any>) {
        getShopCountUseCase.get().execute(
                this::setShopCount,
                this::catchRequestShopCountError,
                createGetShopCountRequestParams(mapParameter)
        )
    }

    private fun setShopCount(shopCount: Int) {
        shopCountMutableLiveData.value = shopCount.toString()
    }

    private fun catchRequestShopCountError(throwable: Throwable) {
        setShopCount(0)
    }

    private fun createGetShopCountRequestParams(mapParameter: Map<String, Any>) =
        RequestParams.create().also {
            it.putAll(mapParameter)
        }

    fun getSearchParameter() = searchParameter.toMap()

    fun getSearchParameterQuery() : String = (searchParameter[SearchApiConst.Q] ?: "").toString()

    fun getSearchShopLiveData(): LiveData<State<List<Visitable<*>>>> = searchShopLiveData

    fun getHasNextPage() = hasNextPage

    fun getDynamicFilterEventLiveData(): LiveData<Event<Boolean>> = dynamicFilterEventLiveData

    fun getOpenFilterPageTrackingEventLiveData(): LiveData<Event<Boolean>> =
            openFilterPageTrackingEventMutableLiveData

    fun getOpenFilterPageEventLiveData(): LiveData<Event<Boolean>> = openFilterPageEventLiveData

    fun getActiveFilterOptionListForEmptySearch() = filterController.getActiveFilterOptionList()

    fun getShopItemImpressionTrackingEventLiveData(): LiveData<Event<List<Any>>> = shopItemImpressionTrackingEventLiveData

    fun getProductPreviewImpressionTrackingEventLiveData(): LiveData<Event<List<Any>>> =
            productPreviewImpressionTrackingEventLiveData

    fun getActiveFilterMapForEmptySearchTracking() = filterController.getActiveFilterMap()

    fun getEmptySearchTrackingEventLiveData(): LiveData<Event<Boolean>> = emptySearchTrackingEventLiveData

    fun getSearchShopFirstPagePerformanceMonitoringEventLiveData(): LiveData<Event<Boolean>> =
            searchShopFirstPagePerformanceMonitoringEventLiveData

    fun getShopRecommendationItemImpressionTrackingEventLiveData(): LiveData<Event<List<Any>>> =
            shopRecommendationItemImpressionTrackingEventLiveData

    fun getShopRecommendationProductPreviewImpressionTrackingEventLiveData(): LiveData<Event<List<Any>>> =
            shopRecommendationProductPreviewImpressionTrackingEventLiveData

    fun getClickShopItemTrackingEventLiveData(): LiveData<Event<ShopDataView.ShopItem>> = clickShopItemTrackingEventLiveData

    fun getClickNotActiveShopItemTrackingEventLiveData(): LiveData<Event<ShopDataView.ShopItem>> =
            clickNotActiveShopItemTrackingEventLiveData

    fun getClickShopRecommendationItemTrackingEventLiveData(): LiveData<Event<ShopDataView.ShopItem>> =
            clickShopRecommendationItemTrackingEventLiveData

    fun getRoutePageEventLiveData(): LiveData<Event<String>> = routePageEventLiveData

    fun getClickProductItemTrackingEventLiveData(): LiveData<Event<ShopDataView.ShopItem.ShopItemProduct>> =
            clickProductItemTrackingEventLiveData

    fun getClickProductRecommendationItemTrackingEventLiveData(): LiveData<Event<ShopDataView.ShopItem.ShopItemProduct>> =
            clickProductRecommendationItemTrackingEventLiveData

    fun getSortFilterItemListLiveData(): LiveData<List<SortFilterItem>> =
            sortFilterItemListLiveData

    fun getQuickFilterIsVisibleLiveData(): LiveData<Boolean> = quickFilterIsVisible

    fun getShimmeringQuickFilterIsVisibleLiveData(): LiveData<Boolean> = shimmeringQuickFilterIsVisible

    fun getRefreshLayoutIsVisibleLiveData(): LiveData<Boolean> = refreshLayoutIsVisible

    fun getClickQuickFilterTrackingEventLiveData(): LiveData<Event<QuickFilterTrackingData>> =
            clickQuickFilterTrackingEventMutableLiveData

    fun getShopCountLiveData(): LiveData<String> =
            shopCountMutableLiveData

    fun getActiveFilterCountLiveData(): LiveData<Int> = activeFilterCountMutableLiveData

    data class QuickFilterTrackingData(val option: Option, val isSelected: Boolean)
}

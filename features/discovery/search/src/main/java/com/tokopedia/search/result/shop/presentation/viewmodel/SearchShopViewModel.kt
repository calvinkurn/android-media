package com.tokopedia.search.result.shop.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
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
import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.search.result.common.Event
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.common.State.*
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.EmptySearchViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.utils.convertValuesToString
import com.tokopedia.search.utils.exists
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CancellationException

internal class SearchShopViewModel(
        dispatcher: DispatcherProvider,
        searchParameter: Map<String, Any>,
        private val searchShopFirstPageRepository: Repository<SearchShopModel>,
        private val searchShopLoadMoreRepository: Repository<SearchShopModel>,
        private val dynamicFilterRepository: Repository<DynamicFilterModel>,
        private val shopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel>,
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
    private var dynamicFilterModel: DynamicFilterModel? = null
    private val filterController = FilterController()
    private val dynamicFilterEventLiveData = MutableLiveData<Event<Boolean>>()

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
        return if (userSession.isLoggedIn) userSession.userId else "0"
    }

    private fun setSearchParameterStartRow(startRow: Int) {
        searchParameter[SearchApiConst.START] = startRow
    }

    fun onViewCreated() {
        if (shouldLoadDataOnViewCreated() && !hasLoadData) {
            searchShop()
        }
    }

    private fun shouldLoadDataOnViewCreated(): Boolean {
        return searchParameter[SearchApiConst.ACTIVE_TAB] == SearchConstant.ActiveTab.SHOP
    }

    fun onViewVisibilityChanged(isViewVisible: Boolean, isViewAdded: Boolean) {
        if (isViewVisible && isViewAdded && !hasLoadData) {
            searchShop()
        }
    }

    private fun searchShop() {
        launchCatchError(block = {
            trySearchShop()
        }, onError = {
            catchSearchShopException(it)
        })
    }

    private suspend fun trySearchShop() {
        updateHasLoadDataToTrue()
        updateSearchShopLiveDataStateToLoading()

        val searchShopModel = requestSearchShopModel(START_ROW_FIRST_TIME_LOAD, searchShopFirstPageRepository)

        processSearchShopFirstPageSuccess(searchShopModel)

        getDynamicFilter()
    }

    private fun updateHasLoadDataToTrue() {
        hasLoadData = true
    }

    private fun updateSearchShopLiveDataStateToLoading() {
        searchShopLiveData.postValue(Loading())
    }

    private suspend fun requestSearchShopModel(startRow: Int, searchShopRepository: Repository<SearchShopModel>): SearchShopModel? {
        setSearchParameterStartRow(startRow)

        val requestParams = createSearchShopParam()

        return searchShopRepository.getResponse(requestParams)
    }

    private fun createSearchShopParam(): Map<String, Any> {
        val requestParams = mutableMapOf<String, Any>()

        putRequestParamsParameters(requestParams)
        requestParams.putAll(searchParameter)

        return requestParams
    }

    private fun putRequestParamsParameters(requestParams: MutableMap<String, Any>) {
        requestParams[SearchApiConst.SOURCE] = SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH
        requestParams[SearchApiConst.DEVICE] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE
        requestParams[SearchApiConst.OB] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
        requestParams[SearchApiConst.ROWS] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS
        requestParams[SearchApiConst.IMAGE_SIZE] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE
        requestParams[SearchApiConst.IMAGE_SQUARE] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE
    }

    private fun processSearchShopFirstPageSuccess(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) return

        updateIsHasNextPage(searchShopModel)

        val visitableList = createVisitableListFromModel(searchShopModel)

        updateSearchShopListWithNewData(visitableList)
        updateSearchShopLiveDataStateToSuccess()
    }

    private fun updateIsHasNextPage(searchShopModel: SearchShopModel) {
        hasNextPage = searchShopModel.aceSearchShop.paging.uriNext.isNotEmpty()
    }

    private fun createVisitableListFromModel(searchShopModel: SearchShopModel): List<Visitable<*>> {
        return if (isSearchShopListEmpty(searchShopModel)) {
            createVisitableListWithEmptySearchViewModel(false)
        }
        else {
            createSearchShopListWithHeader(searchShopModel)
        }
    }

    private fun isSearchShopListEmpty(searchShopModel: SearchShopModel): Boolean {
        return searchShopModel.aceSearchShop.shopList.isEmpty()
    }

    private fun createVisitableListWithEmptySearchViewModel(isFilterActive: Boolean): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        val emptySearchViewModel = EmptySearchViewModel(SHOP_TAB_TITLE, getSearchParameterQuery(), isFilterActive)

        visitableList.add(emptySearchViewModel)

        return visitableList
    }

    private fun createSearchShopListWithHeader(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        val shopHeaderViewModel = createShopHeaderViewModelAsVisitable(searchShopModel)
        visitableList.add(shopHeaderViewModel)

        val shopViewModelList = createShopItemViewModelAsVisitableList(searchShopModel)
        visitableList.addAll(shopViewModelList)

        addLoadingMoreModel(visitableList)

        return visitableList
    }

    private fun createShopHeaderViewModelAsVisitable(searchShopModel: SearchShopModel): Visitable<*> {
        val shopHeaderViewModel = shopHeaderViewModelMapper.convert(searchShopModel)
        shopHeaderViewModel.query = getSearchParameterQuery()

        return shopHeaderViewModel
    }

    private fun createShopItemViewModelAsVisitableList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val shopViewModel = shopViewModelMapper.convert(searchShopModel)
        setShopItemPosition(shopViewModel.shopItemList)

        return shopViewModel.shopItemList
    }

    private fun setShopItemPosition(shopViewItemList: List<ShopViewModel.ShopItem>) {
        var position = getSearchParameterStartRow()
        for (shopItem in shopViewItemList) {
            position++
            shopItem.position = position
            setShopProductItemPosition(shopItem.productList)
        }
    }

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

    private fun catchSearchShopException(e: Throwable?) {
        if (e is CancellationException) {
            catchCancellationException(e)
        }
        else {
            catchSearchShopError(e)
        }
    }

    private fun catchCancellationException(e: Throwable?) {
        e?.printStackTrace()
    }

    private fun catchSearchShopError(e: Throwable?) {
        e?.printStackTrace()

        updateSearchShopLiveDataStateToError()
    }

    private fun updateSearchShopLiveDataStateToError() {
        searchShopLiveData.postValue(Error("", searchShopMutableList))
    }

    private fun getDynamicFilter() {
        launchCatchError(block = {
            tryGetDynamicFilter()
        }, onError = {
            catchGetDynamicFilterException(it)
        })
    }

    private suspend fun tryGetDynamicFilter() {
        val requestParams = createGetDynamicFilterParams()

        dynamicFilterModel = dynamicFilterRepository.getResponse(requestParams)
        dynamicFilterEventLiveData.postValue(Event(true))

        saveDynamicFilterModel()
        processFilterData()
    }

    private fun saveDynamicFilterModel() {
        dynamicFilterModel?.let {
            searchLocalCacheHandler.saveDynamicFilterModelLocally(SCREEN_SEARCH_PAGE_SHOP_TAB, it)
        }
    }

    private fun processFilterData() {
        dynamicFilterModel?.data?.filter?.let { filterList ->
            initializeFilterController(filterList)

            if (shouldUpdateEmptySearchViewModel()) {
                updateEmptySearchViewModelWithFilter(filterController.isFilterActive())
            }
        }
    }

    private fun initializeFilterController(filterList: List<Filter>) {
        val initializedFilterList = FilterHelper.initializeFilterList(filterList)
        filterController.initFilterController(searchParameter.convertValuesToString(), initializedFilterList)
    }

    private fun shouldUpdateEmptySearchViewModel(): Boolean {
        return searchShopMutableList.exists<EmptySearchViewModel>()
                && filterController.isFilterActive()
    }

    private fun updateEmptySearchViewModelWithFilter(isFilterActive: Boolean) {
        clearSearchShopList()

        val visitableList = createVisitableListWithEmptySearchViewModel(isFilterActive)
        updateSearchShopListWithNewData(visitableList)
        updateSearchShopLiveDataStateToSuccess()
    }

    private fun createGetDynamicFilterParams(): Map<String, Any> {
        val requestParams = mutableMapOf<String, Any>()

        requestParams.putAll(searchParameter)
        requestParams.putAll(generateParamsNetwork(requestParams))
        requestParams[SearchApiConst.SOURCE] = SearchApiConst.DEFAULT_VALUE_SOURCE_SHOP
        requestParams[SearchApiConst.DEVICE] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE

        return requestParams
    }

    private fun generateParamsNetwork(requestParams: MutableMap<String, Any>): Map<String, String> {
        return AuthHelper.generateParamsNetwork(
                userSession.userId,
                userSession.deviceId,
                requestParams.convertValuesToString().toMutableMap())
    }

    private fun catchGetDynamicFilterException(e: Throwable?) {
        if (e is CancellationException) {
            catchCancellationException(e)
        }
        else {
            catchGetDynamicFilterError(e)
        }
    }

    private fun catchGetDynamicFilterError(e: Throwable?) {
        e?.printStackTrace()

        dynamicFilterEventLiveData.postValue(Event(false))
    }

    fun onViewLoadMore(isViewVisible: Boolean) {
        if (hasNextPage && isViewVisible) {
            searchMoreShop()
        }
    }

    private fun searchMoreShop() {
        launchCatchError(block = {
            trySearchMoreShop()
        }, onError = {
            catchSearchShopException(it)
        })
    }

    private suspend fun trySearchMoreShop() {
        val searchShopModel = requestSearchShopModel(getTotalShopItemCount(), searchShopLoadMoreRepository)

        processSearchMoreShopSuccess(searchShopModel)
    }

    private fun getTotalShopItemCount(): Int {
        return searchShopMutableList.count { it is ShopViewModel.ShopItem }
    }

    private fun processSearchMoreShopSuccess(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) return

        updateIsHasNextPage(searchShopModel)

        val visitableList = createSearchShopList(searchShopModel)

        updateSearchShopListWithNewData(visitableList)
        updateSearchShopLiveDataStateToSuccess()
    }

    private fun createSearchShopList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        val shopViewModelList = createShopItemViewModelAsVisitableList(searchShopModel)
        visitableList.addAll(shopViewModelList)

        addLoadingMoreModel(visitableList)

        return visitableList
    }

    fun onViewClickRetry() {
        launchCatchError(block = {
            tryRetrySearchShop()
        }, onError = {
            catchSearchShopException(it)
        })
    }

    private suspend fun tryRetrySearchShop() {
        if (isSearchShopLiveDataEmpty()) {
            trySearchShop()
        }
        else {
            trySearchMoreShop()
        }
    }

    private fun isSearchShopLiveDataEmpty(): Boolean {
        return searchShopMutableList.isEmpty()
    }

    fun onViewReloadData() {
        launchCatchError(block = {
            tryReloadSearchShop()
        }, onError = {
            catchSearchShopException(it)
        })
    }

    private suspend fun tryReloadSearchShop() {
        clearSearchShopList()

        trySearchShop()
    }

    private fun clearSearchShopList() {
        searchShopMutableList.clear()
    }

    fun getSearchParameterQuery() = (searchParameter[SearchApiConst.Q] ?: "").toString()

    fun getSearchParameterStartRow() = (searchParameter[SearchApiConst.START] ?: "").toString().toIntOrNull() ?: 0

    fun getSearchShopLiveData(): LiveData<State<List<Visitable<*>>>> {
        return searchShopLiveData
    }

    fun getHasNextPage(): Boolean {
        return hasNextPage
    }

    fun getDynamicFilterEventLiveData(): LiveData<Event<Boolean>> {
        return dynamicFilterEventLiveData
    }

    override fun onCleared() {
        super.onCleared()
        clear()
    }
}

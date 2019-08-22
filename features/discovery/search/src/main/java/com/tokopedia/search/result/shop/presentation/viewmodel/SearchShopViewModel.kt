package com.tokopedia.search.result.shop.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant.GCM_ID
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.search.result.common.EmptySearchCreator
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.common.State.*
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher

class SearchShopViewModel(
        dispatcher: CoroutineDispatcher,
        searchParameter: Map<String, Any>,
        private val searchShopFirstPageUseCase: SearchUseCase<SearchShopModel>,
        private val searchShopLoadMoreUseCase: SearchUseCase<SearchShopModel>,
        private val shopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel>,
        private val shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
        private val emptySearchCreator: EmptySearchCreator,
        private val userSession: UserSessionInterface,
        private val localCacheHandler: LocalCacheHandler
) : BaseViewModel(dispatcher) {

    companion object {
        const val START_ROW_FIRST_TIME_LOAD = 0
        const val SHOP_TAB_TITLE = "Toko"
    }

    private val searchShopLiveData = MutableLiveData<State<List<Visitable<*>>>>()
    private val searchParameter = searchParameter.toMutableMap()
    private val loadingMoreModel = LoadingMoreModel()
    private var isHasNextPage = false

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
            AuthUtil.md5(userSession.userId)
        else
            AuthUtil.md5(getRegistrationId())
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

    fun searchShop() {
        launchCatchError(block = {
            trySearchShop()
        }, onError = {
            catchSearchShopError(it)
        })
    }

    private suspend fun trySearchShop() {
        if (isSearchShopLiveDataContainItems()) return

        updateSearchShopLiveDataStateToLoading()

        val searchShopModel = requestSearchShopModel(START_ROW_FIRST_TIME_LOAD, searchShopFirstPageUseCase)

        searchShopFirstPageSuccess(searchShopModel)
    }

    private fun isSearchShopLiveDataContainItems(): Boolean {
        return searchShopLiveData.value?.data?.size ?: 0 > 0
    }

    private fun updateSearchShopLiveDataStateToLoading() {
        searchShopLiveData.postValue(Loading())
    }

    private suspend fun requestSearchShopModel(startRow: Int, searchShopUseCase: SearchUseCase<SearchShopModel>): SearchShopModel? {
        setSearchParameterStartRow(startRow)

        val requestParams = createSearchShopParam(searchParameter)
        searchShopUseCase.setRequestParams(requestParams.parameters)

        return searchShopUseCase.executeOnBackground()
    }

    private fun createSearchShopParam(searchParameter: Map<String, Any>): RequestParams {
        val requestParams = RequestParams.create()

        putRequestParamsOtherParameters(requestParams)
        requestParams.putAll(searchParameter)

        return requestParams
    }

    private fun putRequestParamsOtherParameters(requestParams: RequestParams) {
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)
        requestParams.putString(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        requestParams.putString(SearchApiConst.ROWS, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS)
        requestParams.putString(SearchApiConst.IMAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE)
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE)
    }

    private fun searchShopFirstPageSuccess(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) return

        updateIsHasNextPage(searchShopModel)

        val visitableList = createVisitableListFromModel(searchShopModel)

        updateSearchShopLiveDataStateToSuccess(visitableList)
    }

    private fun updateIsHasNextPage(searchShopModel: SearchShopModel) {
        isHasNextPage = searchShopModel.aceSearchShop.paging.uriNext.isNotEmpty()
    }

    private fun createVisitableListFromModel(searchShopModel: SearchShopModel): List<Visitable<*>> {
        return if (isSearchShopListEmpty(searchShopModel)) {
            createEmptySearchViewModel()
        }
        else {
            createSearchShopListWithHeader(searchShopModel)
        }
    }

    private fun isSearchShopListEmpty(searchShopModel: SearchShopModel): Boolean {
        return searchShopModel.aceSearchShop.shopList.isEmpty()
    }

    private fun createEmptySearchViewModel(): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        val emptySearchViewModel = emptySearchCreator.createEmptySearchViewModel(
                getSearchParameterQuery(),
                false,
                SHOP_TAB_TITLE
        )

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
            shopItemProduct.position = index
        }
    }

    private fun addLoadingMoreModel(visitableList: MutableList<Visitable<*>>) {
        if (isHasNextPage) {
            visitableList.add(loadingMoreModel)
        }
    }

    private fun updateSearchShopLiveDataStateToSuccess(visitableList: List<Visitable<*>>) {
        val searchShopDataList = getSearchShopLiveDataMutableList()
        searchShopDataList.remove(loadingMoreModel)
        searchShopDataList.addAll(visitableList)

        searchShopLiveData.postValue(Success(searchShopDataList))
    }

    private fun getSearchShopLiveDataMutableList() : MutableList<Visitable<*>> {
        return searchShopLiveData.value?.data?.toMutableList() ?: mutableListOf()
    }

    private fun catchSearchShopError(e: Throwable?) {
        e?.printStackTrace()

        updateSearchShopLiveDataStateToError()
    }

    private fun updateSearchShopLiveDataStateToError() {
        val searchShopDataList = getSearchShopLiveDataMutableList()
        searchShopLiveData.postValue(Error("", searchShopDataList))
    }

    fun searchMoreShop() {
        launchCatchError(block = {
            trySearchMoreShop()
        }, onError = {
            catchSearchShopError(it)
        })
    }

    private suspend fun trySearchMoreShop() {
        if(!isHasNextPage) return

        val searchShopModel = requestSearchShopModel(getTotalShopItemCount(), searchShopLoadMoreUseCase)

        searchShopLoadMoreSuccess(searchShopModel)
    }

    private fun getTotalShopItemCount(): Int {
        return searchShopLiveData.value?.data?.count { it is ShopViewModel.ShopItem } ?: 0
    }

    private fun searchShopLoadMoreSuccess(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) return

        updateIsHasNextPage(searchShopModel)

        val visitableList = createSearchShopList(searchShopModel)
        updateSearchShopLiveDataStateToSuccess(visitableList)
    }

    private fun createSearchShopList(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        val shopViewModelList = createShopItemViewModelAsVisitableList(searchShopModel)
        visitableList.addAll(shopViewModelList)

        addLoadingMoreModel(visitableList)

        return visitableList
    }

    fun retrySearchShop() {
        launchCatchError(block = {
            tryRetrySearchShop()
        }, onError = {
            catchSearchShopError(it)
        })
    }

    private suspend fun tryRetrySearchShop() {
        if (isSearchShopLiveDataDoesNotContainItems()) {
            trySearchShop()
        }
        else {
            trySearchMoreShop()
        }
    }

    private fun isSearchShopLiveDataDoesNotContainItems(): Boolean {
        return !isSearchShopLiveDataContainItems()
    }

    fun reloadSearchShop() {
        launchCatchError(block = {
            tryReloadSearchShop()
        }, onError = {
            catchSearchShopError(it)
        })
    }

    private suspend fun tryReloadSearchShop() {
        updateSearchShopLiveDataStateToLoading()

        val searchShopModel = requestSearchShopModel(START_ROW_FIRST_TIME_LOAD, searchShopFirstPageUseCase)

        searchShopFirstPageSuccess(searchShopModel)
    }

    fun getSearchParameterQuery() = (searchParameter[SearchApiConst.Q] ?: "").toString()

    fun getSearchParameterStartRow() = (searchParameter[SearchApiConst.START] ?: "").toString().toIntOrNull() ?: 0

    fun getSearchShopLiveData(): LiveData<State<List<Visitable<*>>>> {
        return searchShopLiveData
    }

    fun getIsHasNextPage(): Boolean {
        return isHasNextPage
    }

    override fun onCleared() {
        super.onCleared()
        clear()
    }
}

package com.tokopedia.search.result.presentation.viewmodel.shop

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant.GCM_ID
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.viewmodel.State
import com.tokopedia.search.result.presentation.viewmodel.State.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class SearchShopViewModel(
        dispatcher: CoroutineDispatcher,
        searchParameter: Map<String, Any>,
        private val searchShopFirstPageUseCase: SearchUseCase<SearchShopModel>,
        private val searchShopLoadMoreUseCase: SearchUseCase<SearchShopModel>,
        private val shopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel>,
        private val shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
        private val userSession: UserSessionInterface,
        private val localCacheHandler: LocalCacheHandler
) : BaseViewModel(dispatcher) {

    companion object {
        const val START_ROW_FIRST_TIME_LOAD = 0
    }

    private val searchShopLiveData = MutableLiveData<State<List<Visitable<*>>>>()
    private val searchParameter = searchParameter.toMutableMap()
    private val loadingMoreModel = LoadingMoreModel()
    private var isHasNextPage = false

    init {
        this.searchParameter[SearchApiConst.UNIQUE_ID] = generateUniqueId()
        this.searchParameter[SearchApiConst.USER_ID] = generateUserId()

        setSearchParameterStartRow(START_ROW_FIRST_TIME_LOAD)
    }

    private fun generateUserId(): String {
        return if (userSession.isLoggedIn) userSession.userId else "0"
    }

    private fun generateUniqueId(): String {
        return if (userSession.isLoggedIn)
            AuthUtil.md5(userSession.userId)
        else
            AuthUtil.md5(getRegistrationId())
    }

    private fun getRegistrationId(): String {
        return localCacheHandler.getString(GCM_ID, "")
    }

    private fun setSearchParameterStartRow(startRow: Int) {
        searchParameter[SearchApiConst.START] = startRow
    }

    fun searchShop() {
        launch {
            try {
                trySearchShop()
            }
            catch(e: Throwable) {
                catchSearchShopError(e)
            }
        }
    }

    private suspend fun trySearchShop() {
        updateSearchShopLiveDataStateToLoading()

        setSearchParameterStartRow(START_ROW_FIRST_TIME_LOAD)

        val requestParams = createSearchShopParam(searchParameter)
        searchShopFirstPageUseCase.setRequestParams(requestParams.parameters)

        val searchShopModel = searchShopFirstPageUseCase.executeOnBackground()

        searchShopFirstPageSuccess(searchShopModel)
    }

    private fun updateSearchShopLiveDataStateToLoading() {
        searchShopLiveData.postValue(Loading())
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
        requestParams.putString(SearchApiConst.OB, requestParams.getString(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT))
        requestParams.putString(SearchApiConst.ROWS, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS)
        requestParams.putString(SearchApiConst.IMAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE)
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE)
    }

    private fun searchShopFirstPageSuccess(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) return

        updateIsHasNextPage(searchShopModel)

        val visitableList = createSearchShopListWithHeader(searchShopModel)
        updateSearchShopLiveDataStateToSuccess(visitableList)
    }

    private fun updateIsHasNextPage(searchShopModel: SearchShopModel) {
        isHasNextPage = searchShopModel.aceSearchShop.paging.uriNext.isNotEmpty()
    }

    private fun createSearchShopListWithHeader(searchShopModel: SearchShopModel): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        val shopHeaderViewModel = createShopHeaderViewModelAsVisitable(searchShopModel)
        visitableList.add(shopHeaderViewModel)

        val shopViewModelList = createShopItemViewModelAsVisitableList(searchShopModel)
        visitableList.addAll(shopViewModelList)

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
        searchShopDataList.remove(loadingMoreModel)

        searchShopLiveData.postValue(Error("", searchShopDataList))
    }

    fun searchMoreShop() {
        launch {
            try {
                trySearchMoreShop()
            }
            catch(e: Throwable) {
                catchSearchShopError(e)
            }
        }
    }

    private suspend fun trySearchMoreShop() {
        if(!isHasNextPage) return

        addLoadingMoreToSearchShopLiveData()

        setSearchParameterStartRow(getTotalShopItemCount())

        val requestParams = createSearchShopParam(searchParameter)
        searchShopLoadMoreUseCase.setRequestParams(requestParams.parameters)

        val searchShopModel = searchShopLoadMoreUseCase.executeOnBackground()

        searchShopLoadMoreSuccess(searchShopModel)
    }

    private fun addLoadingMoreToSearchShopLiveData() {
        val searchShopDataList = getSearchShopLiveDataMutableList()
        searchShopDataList.add(loadingMoreModel)

        searchShopLiveData.postValue(Success(searchShopDataList))
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

        return visitableList
    }

    fun getSearchParameterQuery() = (searchParameter[SearchApiConst.Q] ?: "").toString()

    fun getSearchParameterStartRow() = (searchParameter[SearchApiConst.START] ?: "").toString().toIntOrNull() ?: 0

    fun getSearchShopLiveData(): LiveData<State<List<Visitable<*>>>> {
        return searchShopLiveData
    }

    override fun onCleared() {
        super.onCleared()
        clear()
    }
}

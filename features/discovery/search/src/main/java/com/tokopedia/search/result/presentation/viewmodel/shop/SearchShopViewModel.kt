package com.tokopedia.search.result.presentation.viewmodel.shop

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant.GCM_ID
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.view.typefactory.ShopListTypeFactory
import com.tokopedia.search.result.presentation.viewmodel.State
import com.tokopedia.search.result.presentation.viewmodel.State.Error
import com.tokopedia.search.result.presentation.viewmodel.State.Success
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber

class SearchShopViewModel(
        dispatcher: CoroutineDispatcher,
        searchParameter: Map<String, Any>,
        private val searchShopFirstPageUseCase: UseCase<SearchShopModel>,
        private val searchShopLoadMoreUseCase: UseCase<SearchShopModel>,
        private val shopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel>,
        private val shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
        private val userSession: UserSessionInterface,
        private val localCacheHandler: LocalCacheHandler
) : BaseViewModel(dispatcher) {

    companion object {
        const val START_ROW_FIRST_TIME_LOAD = 0
    }

    private val searchShopLiveData = MutableLiveData<State<List<Visitable<ShopListTypeFactory>>>>()
    private val searchParameter = searchParameter.toMutableMap()

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
        setSearchParameterStartRow(START_ROW_FIRST_TIME_LOAD)

        searchShopFirstPageUseCase.unsubscribe()

        val requestParams = createSearchShopParam(searchParameter)

        searchShopFirstPageUseCase.execute(requestParams, createSearchShopFirstPageSubscriber())
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

    private fun createSearchShopFirstPageSubscriber(): Subscriber<SearchShopModel> {
        return object: Subscriber<SearchShopModel>() {
            override fun onNext(t: SearchShopModel?) {
                searchShopSuccess(t)
            }

            override fun onCompleted() {
                // Should get dynamic filter
            }

            override fun onError(e: Throwable?) {
                searchShopError(e)
            }
        }
    }

    private fun searchShopSuccess(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) return

        val visitableList = createVisitableList(searchShopModel)

        searchShopLiveData.postValue(Success(visitableList))
    }

    private fun createVisitableList(searchShopModel: SearchShopModel): List<Visitable<ShopListTypeFactory>> {
        val shopHeaderViewModel = createShopHeaderViewModelAsVisitable(searchShopModel)
        val shopViewModelList = createShopItemViewModelAsVisitableList(searchShopModel)

        val visitableList = mutableListOf<Visitable<ShopListTypeFactory>>()

        visitableList.add(shopHeaderViewModel)
        visitableList.addAll(shopViewModelList)

        return visitableList
    }

    private fun createShopHeaderViewModelAsVisitable(searchShopModel: SearchShopModel): Visitable<ShopListTypeFactory> {
        val shopHeaderViewModel = shopHeaderViewModelMapper.convert(searchShopModel)
        shopHeaderViewModel.query = getSearchParameterQuery()

        return shopHeaderViewModel
    }

    private fun createShopItemViewModelAsVisitableList(searchShopModel: SearchShopModel): List<Visitable<ShopListTypeFactory>> {
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

    private fun searchShopError(e: Throwable?) {
        e?.printStackTrace()
        searchShopLiveData.postValue(Error(""))
    }

    fun getSearchParameterQuery() = (searchParameter[SearchApiConst.Q] ?: "").toString()

    fun getSearchParameterStartRow() = (searchParameter[SearchApiConst.START] ?: "").toString().toIntOrNull() ?: 0

    fun getSearchShopLiveData(): LiveData<State<List<Visitable<ShopListTypeFactory>>>> {
        return searchShopLiveData
    }
}

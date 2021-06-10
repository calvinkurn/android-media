package com.tokopedia.tokomart.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokomart.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokomart.home.constant.HomeLayoutState
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.addEmptyStateIntoList
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.isNotStaticLayout
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapGlobalHomeLayoutData
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryGridData
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokomart.home.domain.mapper.TickerMapper.mapTickerData
import com.tokopedia.tokomart.home.domain.model.SearchPlaceholder
import com.tokopedia.tokomart.home.domain.model.Ticker
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokomart.home.presentation.fragment.TokoMartHomeFragment.Companion.CATEGORY_LEVEL_DEPTH
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.TokoMartHomeLayoutUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoMartHomeViewModel @Inject constructor(
    private val getHomeLayoutListUseCase: GetHomeLayoutListUseCase,
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
    private val getTickerUseCase: GetTickerUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.io) {

    val homeLayoutList: LiveData<Result<HomeLayoutListUiModel>>
        get() = _homeLayoutList
    val keywordSearch: LiveData<SearchPlaceholder>
        get() = _keywordSearch
    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress

    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()
    private val _keywordSearch = MutableLiveData<SearchPlaceholder>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()

    private var layoutList = listOf<Visitable<*>>()

    fun getLoadingState() {
        layoutList = addLoadingIntoList()
        val data = HomeLayoutListUiModel(
                result = layoutList,
                isLoadState = true,
                state = HomeLayoutState.LOADING
        )
        _homeLayoutList.value = Success(data)
    }

    fun getEmptyState(id: String) {
        layoutList = addEmptyStateIntoList(id)
        val data = HomeLayoutListUiModel(
                result = layoutList,
                state = HomeLayoutState.HIDE
        )
        _homeLayoutList.value = Success(data)
    }

    fun getHomeLayout(hasTickerBeenRemoved: Boolean) {
        launchCatchError(block = {
            val getTickerAsync = asyncCatchError(
                    context = dispatchers.io,
                    block = {
                        if (!hasTickerBeenRemoved) {
                            getTicker()
                        } else {
                            null
                        }
                    },
                    onError = {
                        _homeLayoutList.postValue(Fail(it))
                        null
                    })

            val getResponseAsync = asyncCatchError(
                    context = dispatchers.io,
                    block = {
                        getHomeLayoutListUseCase.execute()
                    },
                    onError = {
                        _homeLayoutList.postValue(Fail(it))
                        null
                    })

            getResponseAsync.await()?.let { homeLayoutResponse ->
                layoutList = mapHomeLayoutList(
                        homeLayoutResponse,
                        mapTickerData(getTickerAsync.await().orEmpty())
                )
                val data = HomeLayoutListUiModel(
                        result = layoutList,
                        isInitialLoad = true,
                        state = HomeLayoutState.SHOW
                )
                _homeLayoutList.postValue(Success(data))
            }
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getLayoutData(warehouseId: String) {
        launchCatchError(block = {
            val getDataForEachLayout = layoutList.filter { it.isNotStaticLayout() }.map {
                asyncCatchError(block = {
                    getHomeComponentData(it, warehouseId)
                    val data = HomeLayoutListUiModel(
                            result = layoutList,
                            state = HomeLayoutState.SHOW
                    )
                    _homeLayoutList.postValue(Success(data))
                }) {
                    _homeLayoutList.postValue(Fail(it))
                }
            }

            getDataForEachLayout.forEach { it.await() }
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getKeywordSearch(isFirstInstall: Boolean, deviceId: String, userId: String) {
        launchCatchError(coroutineContext, block = {
            val response = getKeywordSearchUseCase.execute(isFirstInstall, deviceId, userId)
            _keywordSearch.postValue(response.searchData)
        }) {}
    }

    fun getMiniCart(shopId: List<String>) {
        launchCatchError(block = {
            getMiniCartUseCase.setParams(shopId)
            getMiniCartUseCase.execute({
                _miniCart.postValue(Success(it))
            }, {
                _miniCart.postValue(Fail(it))
            })
        }) {
            _miniCart.postValue(Fail(it))
        }
    }

    fun getChooseAddress(source: String){
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress( {
            _chooseAddress.postValue(Success(it))
        },{
            _chooseAddress.postValue(Fail(it))
        }, source)
    }

    private suspend fun getTicker(): List<Ticker> {
        return getTickerUseCase.execute()
                .ticker
                .tickerList
    }

    private suspend fun getHomeComponentData(item: Visitable<*>, warehouseId: String) {
        when (item) {
            is TokoMartHomeLayoutUiModel -> getDataForTokoMartHomeComponent(item, warehouseId)
            is HomeComponentVisitable -> getDataForGlobalHomeComponent(item)
        }
    }

    private suspend fun getDataForTokoMartHomeComponent(item: TokoMartHomeLayoutUiModel, warehouseId: String) {
        when (item) {
            is HomeCategoryGridUiModel -> {
                val response = getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH)
                layoutList = layoutList.mapHomeCategoryGridData(item, response.data)
            }
        }
    }

    private suspend fun getDataForGlobalHomeComponent(item: HomeComponentVisitable) {
        val channelId = item.visitableId()
        val response = getHomeLayoutDataUseCase.execute(channelId)
        layoutList = layoutList.mapGlobalHomeLayoutData(item, response)
    }
}
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
import com.tokopedia.tokomart.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokomart.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokomart.home.constant.HomeLayoutState
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.addEmptyStateIntoList
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.isNotStaticLayout
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapGlobalHomeLayoutData
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryGridData
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokomart.home.domain.mapper.TickerMapper.mapTickerData
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.domain.model.SearchPlaceholder
import com.tokopedia.tokomart.home.domain.model.Ticker
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokomart.home.presentation.fragment.TokoMartHomeFragment.Companion.CATEGORY_LEVEL_DEPTH
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class TokoMartHomeViewModel @Inject constructor(
    private val getHomeLayoutListUseCase: GetHomeLayoutListUseCase,
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
    private val getTickerUseCase: GetTickerUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    dispatchers: CoroutineDispatchers,
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
            getHomeLayoutAsync().await()?.let { homeLayoutResponse ->
                layoutList = mapHomeLayoutList(
                        homeLayoutResponse,
                        mapTickerData(getTickerAsync(hasTickerBeenRemoved).await().orEmpty())
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
            layoutList.filter { it.isNotStaticLayout() }.map { item ->
                when(item) {
                    is HomeComponentVisitable -> {
                        getGlobalHomeComponentAsync(item)
                                .await()
                                ?.also {
                                    layoutList = it
                                }
                    }
                    is HomeCategoryGridUiModel -> {
                        getCategoryGridAsync(item, warehouseId)
                                .await()
                                ?.also {
                                    layoutList = it
                                }
                    }
                }
                val data = HomeLayoutListUiModel(
                        result = layoutList,
                        state = HomeLayoutState.SHOW
                )
                _homeLayoutList.postValue(Success(data))
            }
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
        if(!shopId.isNullOrEmpty()) {
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
    }

    fun getChooseAddress(source: String){
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress( {
            _chooseAddress.postValue(Success(it))
        },{
            _chooseAddress.postValue(Fail(it))
        }, source)
    }

    fun getCategoryGrid(item: HomeCategoryGridUiModel, warehouseId: String) {
        launchCatchError(block = {
            layoutList = layoutList.mapHomeCategoryGridData(item, getCategoryList(warehouseId))
            val data = HomeLayoutListUiModel(
                    result = layoutList,
                    state = HomeLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            layoutList = layoutList.mapHomeCategoryGridData(item, null)
            val data = HomeLayoutListUiModel(
                    result = layoutList,
                    state = HomeLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }
    }

    private suspend fun getCategoryList(warehouseId: String): List<CategoryResponse> {
        return getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH)
                .data
    }

    private suspend fun getHomeLayoutAsync(): Deferred<List<HomeLayoutResponse>?> {
        return asyncCatchError(
                block = {
                    getHomeLayoutListUseCase.execute()
                },
                onError = {
                    _homeLayoutList.postValue(Fail(it))
                    null
                })
    }

    private suspend fun getTickerAsync(hasTickerBeenRemoved: Boolean): Deferred<List<Ticker>?> {
        return asyncCatchError(
                block = {
                    if (!hasTickerBeenRemoved) {
                        getTickerUseCase.execute()
                                .ticker
                                .tickerList
                    } else {
                        null
                    }
                },
                onError = {
                    null
                })
    }

    private suspend fun getCategoryGridAsync(item: HomeCategoryGridUiModel, warehouseId: String): Deferred<List<Visitable<*>>?> {
        return asyncCatchError(
                block = {
                    layoutList.mapHomeCategoryGridData(item, getCategoryList(warehouseId))
                },
                onError = {
                    layoutList.mapHomeCategoryGridData(item, null)
                })
    }

    private suspend fun getGlobalHomeComponentAsync(item: HomeComponentVisitable): Deferred<List<Visitable<*>>?> {
        return asyncCatchError(
                block = {
                    val channelId = item.visitableId()
                    val response = getHomeLayoutDataUseCase.execute(channelId)
                    layoutList.mapGlobalHomeLayoutData(item, response)
                },
                onError = {
                    null
                })
    }
}
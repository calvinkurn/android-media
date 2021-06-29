package com.tokopedia.tokopedianow.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.home.constant.HomeLayoutState
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addEmptyStateIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapGlobalHomeLayoutData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryGridData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.shouldLoadLayout
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateStateToLoading
import com.tokopedia.tokopedianow.home.domain.mapper.TickerMapper.mapTickerData
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.domain.model.Ticker
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.CATEGORY_LEVEL_DEPTH
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoNowHomeViewModel @Inject constructor(
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

    private var homeLayoutItemList = listOf<HomeLayoutItemUiModel>()

    fun getLoadingState() {
        homeLayoutItemList = addLoadingIntoList()
        val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = HomeLayoutState.LOADING
        )
        _homeLayoutList.value = Success(data)
    }

    fun getEmptyState(id: String) {
        homeLayoutItemList = addEmptyStateIntoList(id)
        val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = HomeLayoutState.HIDE
        )
        _homeLayoutList.value = Success(data)
    }

    fun getHomeLayout(hasTickerBeenRemoved: Boolean) {
        launchCatchError(block = {
            val homeLayoutResponse = getHomeLayoutListUseCase.execute()
            val tickerList = getTicker(hasTickerBeenRemoved)
            homeLayoutItemList = mapHomeLayoutList(
                homeLayoutResponse,
                mapTickerData(tickerList)
            )
            val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = HomeLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getInitialLayoutData(index: Int, warehouseId: String, isLayoutVisible: Boolean) {
        launchCatchError(block = {
            val lastItemIndex = homeLayoutItemList.count() - 1
            val lastItemLoaded = index > lastItemIndex
            val isInitialLoadFinished = lastItemLoaded || !isLayoutVisible
            val item = homeLayoutItemList.getOrNull(index)

            if (item != null && isLayoutVisible && item.shouldLoadLayout()) {
                setItemStateToLoading(item)
                when (val layout = item.layout) {
                    is HomeComponentVisitable -> {
                        homeLayoutItemList = getGlobalHomeComponent(layout)
                    }
                    is HomeCategoryGridUiModel -> {
                        homeLayoutItemList = getCategoryGridData(layout, warehouseId)
                    }
                }
            }

            val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = HomeLayoutState.SHOW,
                nextItemIndex = index + 1,
                isInitialLoad = index == 0,
                isInitialLoadFinished = isInitialLoadFinished
            )

            withContext(dispatchers.main) {
                _homeLayoutList.value = Success(data)
            }
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getMoreLayoutData(warehouseId: String, firstVisibleItemIndex: Int, lastVisibleItemIndex: Int) {
        launchCatchError(block = {
            for (index in firstVisibleItemIndex..lastVisibleItemIndex) {
                val item = homeLayoutItemList.getOrNull(index)

                if (item != null && item.shouldLoadLayout()) {
                    setItemStateToLoading(item)
                    when (val layout = item.layout) {
                        is HomeComponentVisitable -> {
                            homeLayoutItemList = getGlobalHomeComponent(layout)
                        }
                        is HomeCategoryGridUiModel -> {
                            homeLayoutItemList = getCategoryGridData(layout, warehouseId)
                        }
                    }

                    val data = HomeLayoutListUiModel(
                        result = homeLayoutItemList,
                        state = HomeLayoutState.LOAD_MORE
                    )

                    withContext(dispatchers.main) {
                        _homeLayoutList.value = Success(data)
                    }
                }
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
            val response = getCategoryList(warehouseId)
            val homeLayoutItemList = homeLayoutItemList
                .mapHomeCategoryGridData(item, response)
            val data = HomeLayoutListUiModel(
                    result = homeLayoutItemList,
                    state = HomeLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            val homeLayoutItemList = homeLayoutItemList
                .mapHomeCategoryGridData(item, null)
            val data = HomeLayoutListUiModel(
                    result = homeLayoutItemList,
                    state = HomeLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }
    }

    private suspend fun getCategoryList(warehouseId: String): List<CategoryResponse> {
        return getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH).data
    }

    private suspend fun getTicker(hasTickerBeenRemoved: Boolean): List<Ticker> {
        return if (!hasTickerBeenRemoved) {
            getTickerUseCase.execute()
                .ticker
                .tickerList
        } else {
            emptyList()
        }
    }

    private suspend fun getCategoryGridData(
        item: HomeCategoryGridUiModel,
        warehouseId: String
    ): List<HomeLayoutItemUiModel> {
        val response = getCategoryList(warehouseId)
        return homeLayoutItemList.mapHomeCategoryGridData(item, response)
    }

    private suspend fun getGlobalHomeComponent(
        item: HomeComponentVisitable
    ): List<HomeLayoutItemUiModel> {
        val channelId = item.visitableId()
        val response = getHomeLayoutDataUseCase.execute(channelId)
        return homeLayoutItemList.mapGlobalHomeLayoutData(item, response)
    }

    private fun setItemStateToLoading(item: HomeLayoutItemUiModel) {
        homeLayoutItemList = homeLayoutItemList.updateStateToLoading(item)
    }
}
package com.tokopedia.tokomart.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokomart.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapGlobalHomeLayoutData
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryGridData
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokomart.home.domain.mapper.TickerMapper.mapTickerData
import com.tokopedia.tokomart.home.domain.model.SearchPlaceholder
import com.tokopedia.tokomart.home.domain.model.Ticker
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokomart.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokomart.home.presentation.uimodel.TokoMartHomeLayoutUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class TokoMartHomeViewModel @Inject constructor(
    private val getHomeLayoutListUseCase: GetHomeLayoutListUseCase,
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
    private val getTickerUseCase: GetTickerUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        /**
         * List of layout IDs that doesn't need to call GQL query from Toko Now Home
         * to fetch the data. For example: Choose Address Widget. The GQL call for
         * Choose Address Widget data is done internally, so Toko Now Home doesn't
         * need to call query to fetch data for it.
         */
        private val STATIC_LAYOUT_ID = listOf(
            HomeStaticLayoutId.CHOOSE_ADDRESS_WIDGET_ID,
            HomeStaticLayoutId.TICKER_WIDGET_ID
        )

        // Temp hardcoded wh_id
        private const val WAREHOUSE_ID = "1"
        private const val CATEGORY_LEVEL_DEPTH = 1
    }

    val homeLayoutList: LiveData<Result<HomeLayoutListUiModel>>
        get() = _homeLayoutList
    val keywordSearch: LiveData<SearchPlaceholder>
        get() = _keywordSearch

    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()
    private val _keywordSearch = MutableLiveData<SearchPlaceholder>()

    private var layoutList = listOf<Visitable<*>>()

    fun getHomeLayout() {
        launchCatchError(block = {
            val getTickerAsync = asyncCatchError(
                    context = dispatchers.io,
                    block = {
                        getTicker()
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
                val data = HomeLayoutListUiModel(layoutList, isInitialLoad = true)
                _homeLayoutList.postValue(Success(data))
            }
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getLayoutData() {
        launchCatchError(block = {
            val layoutItems = layoutList.toList()

            val getDataForEachLayout = layoutItems.filter { it.isNotStaticLayout() }.map {
                asyncCatchError(block = {
                    val layoutList = getHomeComponentData(it)
                    val data = HomeLayoutListUiModel(layoutList, isInitialLoad = false)
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

    private suspend fun getTicker(): List<Ticker> {
        return getTickerUseCase.execute()
                .ticker
                .tickerList
    }

    private suspend fun getHomeComponentData(item: Visitable<*>): List<Visitable<*>> {
        layoutList = when (item) {
            is TokoMartHomeLayoutUiModel -> getDataForTokoMartHomeComponent(item)
            is HomeComponentVisitable -> getDataForGlobalHomeComponent(item)
            else -> layoutList
        }
        return layoutList
    }

    private suspend fun getDataForTokoMartHomeComponent(item: TokoMartHomeLayoutUiModel): List<Visitable<*>> {
        return when (item) {
            is HomeCategoryGridUiModel -> {
                val response = getCategoryListUseCase.execute(WAREHOUSE_ID, CATEGORY_LEVEL_DEPTH)
                layoutList.mapHomeCategoryGridData(item, response)
            }
            else -> layoutList
        }
    }

    private suspend fun getDataForGlobalHomeComponent(item: HomeComponentVisitable): List<Visitable<*>> {
        val channelId = item.visitableId().orEmpty()
        val response = getHomeLayoutDataUseCase.execute(channelId)
        return layoutList.mapGlobalHomeLayoutData(item, response)
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when(this) {
            is TokoMartHomeLayoutUiModel -> visitableId
            is HomeComponentVisitable -> visitableId()
            else -> null
        }
    }

    private fun Visitable<*>.isNotStaticLayout(): Boolean {
        return this.getVisitableId() !in STATIC_LAYOUT_ID
    }
}
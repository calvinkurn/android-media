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
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapGlobalHomeLayoutData
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryGridData
import com.tokopedia.tokomart.home.domain.model.SearchPlaceholder
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokomart.home.domain.usecase.GetKeywordSearchUseCase
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
    private val getNotificationUseCase: GetNotificationUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        /**
         * List of layout IDs that doesn't need to call GQL query from Toko Now Home
         * to fetch the data. For example: Choose Address Widget. The GQL call for
         * Choose Address Widget data is done internally, so Toko Now Home doesn't
         * need to call query to fetch data for it.
         */
        private val STATIC_LAYOUT_ID = listOf(
            HomeStaticLayoutId.CHOOSE_ADDRESS_WIDGET_ID
        )

        // Temp hardcoded wh_id
        private const val WAREHOUSE_ID = "1"
        private const val CATEGORY_LEVEL_DEPTH = 1
    }

    val homeLayoutList: LiveData<Result<HomeLayoutListUiModel>>
        get() = _homeLayoutList
    val searchHint: LiveData<SearchPlaceholder>
        get() = _searchHint
    val notificationCounter: LiveData<TopNavNotificationModel>
        get() = _notificationCounter

    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()
    private val _searchHint = MutableLiveData<SearchPlaceholder>()
    private val _notificationCounter = MutableLiveData<TopNavNotificationModel>()

    private var layoutList = listOf<Visitable<*>>()

    fun getHomeLayout() {
        launchCatchError(block = {
            val response = getHomeLayoutListUseCase.execute()
            layoutList = HomeLayoutMapper.mapHomeLayoutList(response)
            val data = HomeLayoutListUiModel(layoutList, isInitialLoad = true)
            _homeLayoutList.postValue(Success(data))
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

    fun getSearchHint(isFirstInstall: Boolean, deviceId: String, userId: String) {
        launchCatchError(coroutineContext, block = {
            getKeywordSearchUseCase.params = getKeywordSearchUseCase.createParams(isFirstInstall, deviceId, userId)
            val data = getKeywordSearchUseCase.executeOnBackground()
            _searchHint.postValue(data.searchData)
        }) {}
    }

    // only used to count the old toolbar's notif
    fun getNotification() {
        launchCatchError(coroutineContext, block = {
            val topNavNotificationModel = getNotificationUseCase.executeOnBackground()
            _notificationCounter.postValue(topNavNotificationModel)
        }) {}
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
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
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapGlobalHomeLayoutData
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryGridData
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.TokoMartHomeLayoutUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class TokoMartHomeViewModel @Inject constructor(
    private val getHomeLayoutListUseCase: GetHomeLayoutListUseCase,
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        // Temp hardcoded wh_id
        private const val WAREHOUSE_ID = "1"
        private const val CATEGORY_LEVEL_DEPTH = 1
    }

    val homeLayoutList: LiveData<Result<HomeLayoutListUiModel>>
        get() = _homeLayoutList

    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()

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

            val getDataForEachLayout = layoutItems.map {
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
}
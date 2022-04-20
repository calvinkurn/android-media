package com.tokopedia.tokofood.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutItemState
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutState
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.addLoadingIntoList
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.getVisitableId
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.mapHomeLayoutList
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.mapUSPData
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.removeItem
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.setStateToLoading
import com.tokopedia.tokofood.home.domain.usecase.TokoFoodHomeDynamicChannelUseCase
import com.tokopedia.tokofood.home.domain.usecase.TokoFoodHomeUSPUseCase
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeItemUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeLayoutUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeListUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class TokoFoodHomeViewModel @Inject constructor(
    private val tokoFoodDynamicChanelUseCase: TokoFoodHomeDynamicChannelUseCase,
    private val tokoFoodHomeUSPUseCase: TokoFoodHomeUSPUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val homeLayoutList: LiveData<Result<TokoFoodHomeListUiModel>>
        get() = _homeLayoutList

    private val _homeLayoutList = MutableLiveData<Result<TokoFoodHomeListUiModel>>()

    private val homeLayoutItemList = mutableListOf<TokoFoodHomeItemUiModel>()

    fun getLoadingState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addLoadingIntoList()
        val data = TokoFoodHomeListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodHomeLayoutState.LOADING
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun getHomeLayout() {
        launchCatchError(block = {

            homeLayoutItemList.clear()

            val homeLayoutResponse = tokoFoodDynamicChanelUseCase.execute()

            homeLayoutItemList.mapHomeLayoutList(homeLayoutResponse.response.data)

            val data = TokoFoodHomeListUiModel(
                items = getHomeVisitableList(),
                state = TokoFoodHomeLayoutState.SHOW
            )

            _homeLayoutList.postValue(Success(data))

            }){

        }
    }


    fun getLayoutComponentData(){
        launchCatchError(block = {
            homeLayoutItemList.filter { it.state == TokoFoodHomeLayoutItemState.NOT_LOADED }.forEach {
                homeLayoutItemList.setStateToLoading(it)

                when (val item = it.layout){
                    is TokoFoodHomeLayoutUiModel -> getTokoFoodHomeComponent(item)
                    else -> {}
                }

                val data = TokoFoodHomeListUiModel(
                    items = getHomeVisitableList(),
                    state = TokoFoodHomeLayoutState.UPDATE
                )

                _homeLayoutList.postValue(Success(data))
            }
        }){

        }
    }

    private suspend fun getTokoFoodHomeComponent(item: TokoFoodHomeLayoutUiModel) {
        when (item) {
            is TokoFoodHomeUSPUiModel -> getUSPDataAsync(item).await()
            is TokoFoodHomeIconsUiModel -> {}
            else -> removeUnsupportedLayout(item)
        }
    }

    private suspend fun getUSPDataAsync(item: TokoFoodHomeUSPUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val uspData = tokoFoodHomeUSPUseCase.execute()
            homeLayoutItemList.mapUSPData(item, uspData)
        }){
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private fun getHomeVisitableList(): List<Visitable<*>> {
        return homeLayoutItemList.mapNotNull { it.layout }
    }

    private fun removeUnsupportedLayout(item: Visitable<*>?) {
        homeLayoutItemList.removeItem(item?.getVisitableId())
    }
}
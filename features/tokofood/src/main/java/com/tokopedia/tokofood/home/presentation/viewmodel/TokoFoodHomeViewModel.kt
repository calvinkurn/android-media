package com.tokopedia.tokofood.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutState
import com.tokopedia.tokofood.home.domain.data.GetTokoFoodHomeLayoutResponse
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.addLoadingIntoList
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.mapHomeLayoutList
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeItemUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeListUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoFoodHomeViewModel @Inject constructor(dispatchers: CoroutineDispatchers):
    BaseViewModel(dispatchers.main) {

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

            val homeLayoutMockResponse = Gson().fromJson(DummyData.dummyHomeData, GetTokoFoodHomeLayoutResponse::class.java)
            homeLayoutItemList.mapHomeLayoutList(homeLayoutMockResponse.response.data)

            val data = TokoFoodHomeListUiModel(
                items = getHomeVisitableList(),
                state = TokoFoodHomeLayoutState.SHOW
            )

            _homeLayoutList.postValue(Success(data))

            }){

        }
    }

    private fun getHomeVisitableList(): List<Visitable<*>> {
        return homeLayoutItemList.mapNotNull { it.layout }
    }
}
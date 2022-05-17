package com.tokopedia.tokofood.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutItemState
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutState
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.addLoadingIntoList
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.addNoAddressState
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.addNoPinPointState
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.getVisitableId
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.mapDynamicIcons
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.mapHomeLayoutList
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.mapUSPData
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.removeItem
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.setStateToLoading
import com.tokopedia.tokofood.home.domain.usecase.TokoFoodHomeDynamicChannelUseCase
import com.tokopedia.tokofood.home.domain.usecase.TokoFoodHomeDynamicIconsUseCase
import com.tokopedia.tokofood.home.domain.usecase.TokoFoodHomeUSPUseCase
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeItemUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeLayoutUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeListUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.tokofood.purchase.purchasepage.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoFoodHomeViewModel @Inject constructor(
    private val tokoFoodDynamicChanelUseCase: TokoFoodHomeDynamicChannelUseCase,
    private val tokoFoodHomeUSPUseCase: TokoFoodHomeUSPUseCase,
    private val tokoFoodHomeDynamicIconsUseCase: TokoFoodHomeDynamicIconsUseCase,
    private val keroEditAddressUseCase: KeroEditAddressUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val homeLayoutList: LiveData<Result<TokoFoodHomeListUiModel>>
        get() = _homeLayoutList
    val updatePinPointState: LiveData<Boolean>
        get() = _updatePinPointState
    val errorMessage:LiveData<String>
        get() = _errorMessage
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress

    private val _homeLayoutList = MutableLiveData<Result<TokoFoodHomeListUiModel>>()
    private val _updatePinPointState = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()

    private val homeLayoutItemList = mutableListOf<TokoFoodHomeItemUiModel>()

    fun updatePinPoin(addressId: String, latitude: String, longitude: String) {
        launchCatchError(block = {
            val isSuccess = withContext(dispatchers.io) {
                keroEditAddressUseCase.execute(addressId, latitude, longitude)
            }
            _updatePinPointState.value = isSuccess
        }){
            _errorMessage.value = it.message
        }
    }

    fun getChooseAddress(source: String){
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress( {
            _chooseAddress.postValue(Success(it))
        },{
            _chooseAddress.postValue(Fail(it))
        }, source)
    }

    fun getLoadingState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addLoadingIntoList()
        val data = TokoFoodHomeListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodHomeLayoutState.LOADING
        )
        _homeLayoutList.value = Success(data)
    }

    fun getNoPinPoinState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addNoPinPointState()
        val data = TokoFoodHomeListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodHomeLayoutState.HIDE
        )
        _homeLayoutList.value = Success(data)
    }

    fun getNoAddressState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addNoAddressState()
        val data = TokoFoodHomeListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodHomeLayoutState.HIDE
        )
        _homeLayoutList.value = Success(data)
    }

    fun getHomeLayout(localCacheModel: LocalCacheModel) {
        launchCatchError(block = {

            homeLayoutItemList.clear()

            val homeLayoutResponse = withContext(dispatchers.io) {
                tokoFoodDynamicChanelUseCase.execute(localCacheModel)
            }

            homeLayoutItemList.mapHomeLayoutList(homeLayoutResponse.response.data)

            val data = TokoFoodHomeListUiModel(
                items = getHomeVisitableList(),
                state = TokoFoodHomeLayoutState.SHOW
            )

            _homeLayoutList.value = Success(data)

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

                _homeLayoutList.value = Success(data)
            }
        }){

        }
    }

    private suspend fun getTokoFoodHomeComponent(item: TokoFoodHomeLayoutUiModel) {
        when (item) {
            is TokoFoodHomeUSPUiModel -> getUSPDataAsync(item).await()
            is TokoFoodHomeIconsUiModel -> getIconListDataAsync(item).await()
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

    private suspend fun getIconListDataAsync(item: TokoFoodHomeIconsUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val dynamicIcons = tokoFoodHomeDynamicIconsUseCase.execute(item.widgetParam)
            homeLayoutItemList.mapDynamicIcons(item, dynamicIcons)
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
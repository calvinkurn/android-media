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
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutItemState
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutState
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
import com.tokopedia.tokofood.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodItemUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeLayoutUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodListUiModel
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
    private val tokoFoodMerchantListUseCase: TokoFoodMerchantListUseCase,
    private val keroEditAddressUseCase: KeroEditAddressUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val eligibleForAddressUseCase: EligibleForAddressUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val layoutList: LiveData<Result<TokoFoodListUiModel>>
        get() = _homeLayoutList
    val updatePinPointState: LiveData<Boolean>
        get() = _updatePinPointState
    val errorMessage:LiveData<String>
        get() = _errorMessage
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress
    val eligibleForAnaRevamp: LiveData<Result<EligibleForAddressFeature>>
        get() = _eligibleForAnaRevamp

    private val _homeLayoutList = MutableLiveData<Result<TokoFoodListUiModel>>()
    private val _updatePinPointState = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    private val _eligibleForAnaRevamp = MutableLiveData<Result<EligibleForAddressFeature>>()

    private val homeLayoutItemList = mutableListOf<TokoFoodItemUiModel>()

    fun updatePinPoin(addressId: String, latitude: String, longitude: String) {
        launchCatchError(block = {
            val isSuccess = withContext(dispatchers.io) {
                keroEditAddressUseCase.execute(addressId, latitude, longitude)
            }
            _updatePinPointState.postValue(isSuccess)
        }){
            _errorMessage.postValue(it.message)
        }
    }

    fun checkUserEligibilityForAnaRevamp() {
        eligibleForAddressUseCase.eligibleForAddressFeature(
            {
                _eligibleForAnaRevamp.postValue(Success(it.eligibleForRevampAna))
            },
            {
                _eligibleForAnaRevamp.postValue(Fail(it))
            },
            AddressConstant.ANA_REVAMP_FEATURE_ID
        )
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
        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.LOADING
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun getNoPinPoinState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addNoPinPointState()
        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.HIDE
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun getNoAddressState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addNoAddressState()
        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.HIDE
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun getHomeLayout(localCacheModel: LocalCacheModel) {
        launchCatchError(block = {

            homeLayoutItemList.clear()

            val homeLayoutResponse = withContext(dispatchers.io) {
                tokoFoodDynamicChanelUseCase.execute(localCacheModel)
            }

            homeLayoutItemList.mapHomeLayoutList(homeLayoutResponse.response.data)

            val data = TokoFoodListUiModel(
                items = getHomeVisitableList(),
                state = TokoFoodLayoutState.SHOW
            )

            _homeLayoutList.postValue(Success(data))

            }){

        }
    }

    fun getLayoutComponentData(){
        launchCatchError(block = {
            homeLayoutItemList.filter { it.state == TokoFoodLayoutItemState.NOT_LOADED }.forEach {
                homeLayoutItemList.setStateToLoading(it)

                when (val item = it.layout){
                    is TokoFoodHomeLayoutUiModel -> getTokoFoodHomeComponent(item)
                    else -> {}
                }

                val data = TokoFoodListUiModel(
                    items = getHomeVisitableList(),
                    state = TokoFoodLayoutState.UPDATE
                )

                _homeLayoutList.postValue(Success(data))
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
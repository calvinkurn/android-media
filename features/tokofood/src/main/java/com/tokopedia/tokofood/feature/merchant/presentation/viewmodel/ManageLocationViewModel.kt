package com.tokopedia.tokofood.feature.merchant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ManageLocationViewModel @Inject constructor(
        private val keroEditAddressUseCase: KeroEditAddressUseCase,
        private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
        private val eligibleForAddressUseCase: EligibleForAddressUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    var merchantId: String = ""

    private val _updatePinPointState = MutableLiveData<Boolean>()
    val updatePinPointState: LiveData<Boolean> get() = _updatePinPointState
    private val _eligibleForAnaRevamp = MutableLiveData<Result<EligibleForAddressFeature>>()
    val eligibleForAnaRevamp: LiveData<Result<EligibleForAddressFeature>> get() = _eligibleForAnaRevamp
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>> get() = _chooseAddress
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun updatePinPoint(addressId: String, latitude: String, longitude: String) {
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
}
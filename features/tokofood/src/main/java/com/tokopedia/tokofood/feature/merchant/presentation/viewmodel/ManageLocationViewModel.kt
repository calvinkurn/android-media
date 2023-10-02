package com.tokopedia.tokofood.feature.merchant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.feature.merchant.domain.model.response.GetMerchantDataResponse
import com.tokopedia.tokofood.feature.merchant.domain.usecase.CheckDeliveryCoverageUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ManageLocationViewModel @Inject constructor(
    private val keroEditAddressUseCase: KeroEditAddressUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val checkDeliveryCoverageUseCase: CheckDeliveryCoverageUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    var merchantId: String = ""

    private val _updatePinPointState = MutableLiveData<Boolean>()
    val updatePinPointState: LiveData<Boolean> get() = _updatePinPointState

    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>> get() = _chooseAddress
    private val _checkDeliveryCoverageResult = MutableLiveData<Result<GetMerchantDataResponse>>()
    val checkDeliveryCoverageResult: LiveData<Result<GetMerchantDataResponse>> get() = _checkDeliveryCoverageResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun updatePinPoint(addressId: String, latitude: String, longitude: String) {
        launchCatchError(block = {
            val isSuccess = withContext(dispatchers.io) {
                keroEditAddressUseCase.execute(addressId, latitude, longitude)
            }
            _updatePinPointState.postValue(isSuccess)
        }) {
            _errorMessage.postValue(it.message)
        }
    }

    fun getChooseAddress(source: String) {
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress({
            _chooseAddress.postValue(Success(it))
        }, {
            _chooseAddress.postValue(Fail(it))
        }, source)
    }

    fun checkDeliveryCoverage(merchantId: String, latlong: String, timezone: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = CheckDeliveryCoverageUseCase.createRequestParams(
                    merchantId = merchantId,
                    latlong = latlong,
                    timezone = timezone
                )
                checkDeliveryCoverageUseCase.setRequestParams(params = params.parameters)
                checkDeliveryCoverageUseCase.executeOnBackground()
            }
            _checkDeliveryCoverageResult.value = Success(result)
        }, onError = {
                _checkDeliveryCoverageResult.value = Fail(it)
            })
    }
}

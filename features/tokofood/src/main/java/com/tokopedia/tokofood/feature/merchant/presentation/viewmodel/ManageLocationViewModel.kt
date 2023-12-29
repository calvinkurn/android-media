package com.tokopedia.tokofood.feature.merchant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.domain.param.KeroEditAddressParam
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointWithAddressIdUseCase
import com.tokopedia.tokofood.feature.merchant.domain.model.response.GetMerchantDataResponse
import com.tokopedia.tokofood.feature.merchant.domain.usecase.CheckDeliveryCoverageUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ManageLocationViewModel @Inject constructor(
    private val keroEditAddressUseCase: UpdatePinpointWithAddressIdUseCase,
    private val checkDeliveryCoverageUseCase: CheckDeliveryCoverageUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    var merchantId: String = ""

    private val _updatePinPointState =
        MutableLiveData<KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse>()
    val updatePinPointState: LiveData<KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse> get() = _updatePinPointState

    private val _checkDeliveryCoverageResult = MutableLiveData<Result<GetMerchantDataResponse>>()
    val checkDeliveryCoverageResult: LiveData<Result<GetMerchantDataResponse>> get() = _checkDeliveryCoverageResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun updatePinPoint(addressId: String, latitude: String, longitude: String) {
        launchCatchError(block = {
            val isSuccess = withContext(dispatchers.io) {
                keroEditAddressUseCase(
                    KeroEditAddressParam(
                        addressId,
                        latitude,
                        longitude,
                        ManageAddressSource.TOKOFOOD
                    )
                )
            }
            _updatePinPointState.postValue(isSuccess)
        }) {
            _errorMessage.postValue(it.message)
        }
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

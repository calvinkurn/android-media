package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.model.AddressResult
import com.tokopedia.tradein.model.DeviceDataResponse
import com.tokopedia.tradein.usecase.DiagnosticDataUseCase
import com.tokopedia.tradein.usecase.GetAddressUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class FinalPriceViewModel@Inject constructor(
        private var getAddressUseCase: GetAddressUseCase,
        private var diagnosticDataUseCase : DiagnosticDataUseCase
)  : BaseTradeInViewModel(), LifecycleObserver, CoroutineScope {
    val deviceDiagData: MutableLiveData<DeviceDataResponse> = MutableLiveData()
    val addressLiveData = MutableLiveData<AddressResult>()
    var tradeInParams: TradeInParams? = null
    var tradeInType: Int = 1

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    internal fun getDiagnosticData() {
        launchCatchError(block = {
            deviceDiagData.value = diagnosticDataUseCase.getDiagnosticData(getResource(),tradeInParams, tradeInType)
        }, onError = {
            it.printStackTrace()
            warningMessage.value = getResource()?.getString(com.tokopedia.abstraction.R.string.default_request_error_timeout)
        })
    }

    fun getAddress() {
        progBarVisibility.value = true
        launchCatchError(block = {
            progBarVisibility.value = false
            addressLiveData.value = getAddressUseCase.getAddress(getResource())
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it.localizedMessage
        })
    }

}

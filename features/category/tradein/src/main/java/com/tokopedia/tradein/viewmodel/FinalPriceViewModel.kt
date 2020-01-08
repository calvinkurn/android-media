package com.tokopedia.tradein.viewmodel

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.model.AddressResult
import com.tokopedia.tradein.model.DeviceDataResponse
import com.tokopedia.tradein.usecase.DiagnosticDataUseCase
import com.tokopedia.tradein.usecase.GetAddressUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FinalPriceViewModel@Inject constructor(
        @ApplicationContext val context: Context,
        private var getAddressUseCase: GetAddressUseCase,
        private var diagnosticDataUseCase : DiagnosticDataUseCase
)  : BaseTradeInViewModel(), LifecycleObserver, CoroutineScope {
    val deviceDiagData: MutableLiveData<DeviceDataResponse> = MutableLiveData()
    val addressLiveData = MutableLiveData<AddressResult>()
    val STATUS_NO_ADDRESS: Int = 12324
    var tradeInParams: TradeInParams? = null
    var tradeInType: Int = 1

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    internal fun getDiagnosticData() {
        launchCatchError(block = {
            deviceDiagData.value = diagnosticDataUseCase.getDiagnosticData(tradeInParams, tradeInType)
        }, onError = {
            it.printStackTrace()
            warningMessage.value = context.getString(com.tokopedia.abstraction.R.string.default_request_error_timeout)
        })
    }

    fun getAddress() {
        progBarVisibility.value = true
        launchCatchError(block = {
            progBarVisibility.value = false
            addressLiveData.value = getAddressUseCase.getAddress()
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it.localizedMessage
        })
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

}

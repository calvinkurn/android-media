package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.model.TradeInValidateImeiModel
import com.tokopedia.tradein.usecase.TradeInValidateImeiUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInImeiBSViewModel@Inject constructor(val tradeInImeiUseCase: TradeInValidateImeiUseCase) : BaseTradeInViewModel(),
    CoroutineScope {
    val tradeInImeiLiveData = MutableLiveData<TradeInValidateImeiModel>()

    fun validateImei(laku6DeviceModel: Laku6DeviceModel, imei: String) {
        progBarVisibility.value = true
        launchCatchError(block = {
            tradeInImeiLiveData.value = tradeInImeiUseCase.validateImei(laku6DeviceModel, imei)
            progBarVisibility.value = false
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it
        })
    }

}
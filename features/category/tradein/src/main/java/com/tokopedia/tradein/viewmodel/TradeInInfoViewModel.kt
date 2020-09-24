package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.model.TnCInfoModel
import com.tokopedia.tradein.usecase.TNCInfoUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInInfoViewModel @Inject constructor(
        private var tNCInfoUseCase: TNCInfoUseCase
) : BaseTradeInViewModel(), CoroutineScope {
    val tncInfoLiveData = MutableLiveData<TnCInfoModel>()

    fun getTNC(type: Int) {
        launchCatchError(block = {
            val tncInfo = tNCInfoUseCase.getTNCInfo(type)
            tncInfo.fetchTickerAndTnC.type = type
            tncInfoLiveData.value = tncInfo
            progBarVisibility.value = false
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it.localizedMessage
        })
    }
}
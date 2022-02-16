package com.tokopedia.tradein.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.common_tradein.model.TradeInPDPData
import com.tokopedia.common_tradein.utils.TradeInPDPHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.model.TradeInDetailModel
import com.tokopedia.tradein.usecase.TradeInDetailUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInHomePageFragmentVM @Inject constructor(val tradeInDetailUseCase: TradeInDetailUseCase) : BaseTradeInViewModel(),
    CoroutineScope {
    var data: TradeInPDPData? = null
    val tradeInDetailLiveData = MutableLiveData<TradeInDetailModel>()

    fun getPDPData(context: Context, id : String) : TradeInPDPData? {
        data = TradeInPDPHelper.getDataFromPDP(context, id) ?: data
        return data
    }

    fun startProgressBar() {
        progBarVisibility.value = true
    }

    fun getTradeInDetail(laku6DeviceModel: Laku6DeviceModel) {
        launchCatchError(block = {
            val tradeInDetail = tradeInDetailUseCase.getTradeInDetail(laku6DeviceModel)
            tradeInDetailLiveData.value = tradeInDetail
            progBarVisibility.value = false
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it
        })
    }

}
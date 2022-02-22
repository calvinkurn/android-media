package com.tokopedia.tradein.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.common_tradein.model.TradeInPDPData
import com.tokopedia.common_tradein.utils.TradeInPDPHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.model.TradeInDetailModel
import com.tokopedia.tradein.model.TradeInDetailModel.GetTradeInDetail.LogisticOption
import com.tokopedia.tradein.usecase.TradeInDetailUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInHomePageFragmentVM @Inject constructor(val tradeInDetailUseCase: TradeInDetailUseCase) : BaseTradeInViewModel(),
    CoroutineScope {
    var data: TradeInPDPData? = null
    var logisticData: ArrayList<LogisticOption> = arrayListOf()
    val tradeInDetailLiveData = MutableLiveData<TradeInDetailModel>()

    fun getPDPData(context: Context, id : String) : TradeInPDPData? {
        data = TradeInPDPHelper.getDataFromPDP(context, id) ?: data
        return data
    }

    fun getTradeInPDPData() = data

    fun startProgressBar() {
        progBarVisibility.value = true
    }

    fun getTradeInDetail(
        laku6DeviceModel: Laku6DeviceModel,
        productPrice: Int,
        userAddressData: LocalCacheModel
    ) {
        launchCatchError(block = {
            val tradeInDetail = tradeInDetailUseCase.getTradeInDetail(laku6DeviceModel, productPrice, userAddressData)
            logisticData = tradeInDetail.getTradeInDetail.logisticOptions
            if(tradeInDetail.getTradeInDetail.errMessage.isNotEmpty())
                warningMessage.value = tradeInDetail.getTradeInDetail.errMessage
            tradeInDetailLiveData.value = tradeInDetail
            progBarVisibility.value = false
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it
        })
    }

}
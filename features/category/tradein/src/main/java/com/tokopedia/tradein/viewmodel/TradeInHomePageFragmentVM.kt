package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.MutableLiveData
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
    var logisticData: ArrayList<LogisticOption> = arrayListOf()
    val tradeInDetailLiveData = MutableLiveData<TradeInDetailModel>()

    fun startProgressBar() {
        progBarVisibility.value = true
    }

    fun getTradeInDetail(
        laku6DeviceModel: Laku6DeviceModel,
        productPrice: Double,
        userAddressData: LocalCacheModel,
        tradeInUniqueCode : String
    ) {
        launchCatchError(block = {
            val tradeInDetail = tradeInDetailUseCase.getTradeInDetail(laku6DeviceModel, productPrice, userAddressData, tradeInUniqueCode)
            logisticData = tradeInDetail.getTradeInDetail.logisticOptions
            tradeInDetailLiveData.value = tradeInDetail
            progBarVisibility.value = false
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it
        })
    }

}
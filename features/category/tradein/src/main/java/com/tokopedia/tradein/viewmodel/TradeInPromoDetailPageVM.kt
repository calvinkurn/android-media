package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.model.PromoTradeInModel
import com.tokopedia.tradein.usecase.PromoUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInPromoDetailPageVM @Inject constructor(
    private var promoUsecase: PromoUseCase
) : BaseTradeInViewModel(), CoroutineScope {

    val promoTradeInLiveData = MutableLiveData<PromoTradeInModel>()

    fun getPromo(code: String) {
        launchCatchError(block = {
            val promoData = promoUsecase.getPromo(code)
            promoTradeInLiveData.value = promoData
            progBarVisibility.value = false
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it
        })
    }
}
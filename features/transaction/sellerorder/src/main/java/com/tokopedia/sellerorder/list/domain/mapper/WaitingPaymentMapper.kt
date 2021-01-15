package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.sellerorder.list.domain.model.SomListWaitingPaymentResponse
import com.tokopedia.sellerorder.list.presentation.models.WaitingPaymentCounter
import javax.inject.Inject

class WaitingPaymentMapper @Inject constructor() {
    fun mapResponseToUiModel(waitingPaymentCounter: SomListWaitingPaymentResponse.Data.OrderFilterSom.WaitingPaymentCounter): WaitingPaymentCounter {
        return WaitingPaymentCounter(waitingPaymentCounter.amount, waitingPaymentCounter.text)
    }
}
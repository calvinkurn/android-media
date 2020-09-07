package com.tokopedia.sellerorder.waitingpaymentorder.presentation.model

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel

data class WaitingPaymentOrderErrorNetworkUiModel(
        val errorType: Int
) : ErrorNetworkModel()
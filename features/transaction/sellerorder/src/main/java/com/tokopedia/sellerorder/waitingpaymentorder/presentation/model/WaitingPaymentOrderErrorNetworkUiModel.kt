package com.tokopedia.sellerorder.waitingpaymentorder.presentation.model

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

data class WaitingPaymentOrderErrorNetworkUiModel(
        val errorType: Int
) : ErrorNetworkModel()
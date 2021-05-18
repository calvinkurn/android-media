package com.tokopedia.pms.paymentlist.presentation.listeners

import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel

interface PaymentListActionListener {
    fun cancelSingleTransaction(transactionId: String, merchantCode: String)
    fun cancelCombinedTransaction(model: BasePaymentModel)

}
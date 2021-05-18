package com.tokopedia.pms.paymentlist.presentation.listeners

import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel

interface PaymentListActionListener {
    fun cancelSingleTransaction(transactionId: String, merchantCode: String, productName: String?)
    fun cancelCombinedTransaction(model: BasePaymentModel)
    fun changeAccountDetail(model: BasePaymentModel)
    fun uploadPaymentProof(model: BasePaymentModel)
    fun changeBcaUserId(model: BasePaymentModel)

}
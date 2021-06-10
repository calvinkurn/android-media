package com.tokopedia.pms.paymentlist.presentation.listener

import com.tokopedia.pms.analytics.PmsEvents
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel

interface PaymentListActionListener {
    fun showInvoiceDetail(invoiceUrl: String?)
    fun cancelSingleTransaction(transactionId: String, merchantCode: String, productName: String?, event: PmsEvents)
    fun cancelCombinedTransaction(model: BasePaymentModel)
    fun changeAccountDetail(model: BasePaymentModel)
    fun uploadPaymentProof(model: BasePaymentModel)
    fun changeBcaUserId(model: BasePaymentModel)

}
package com.tokopedia.chatbot.chatbot2.attachinvoice.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chatbot.chatbot2.attachinvoice.view.adapter.TransactionInvoiceListAdapterTypeFactory

data class TransactionInvoiceUiModel(
    val invoiceId: String = "",
    val title: String = "",
    val description: String = "",
    val createdTime: String = "",
    val statusId: Long = 0,
    val status: String = "",
    val amount: String = "",
    val invoiceUrl: String = "",
    val imageUrl: String = "",
    val userId: String = "",
    val userName: String = "",
    val invoiceNumber: String = "",
    var invoiceType: Long = 0,
    var invoiceTypeStr: String,
    var color: String
) : Visitable<TransactionInvoiceListAdapterTypeFactory> {
    override fun type(typeFactory: TransactionInvoiceListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

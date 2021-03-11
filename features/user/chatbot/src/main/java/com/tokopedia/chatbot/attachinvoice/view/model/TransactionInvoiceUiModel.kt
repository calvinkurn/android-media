package com.tokopedia.chatbot.attachinvoice.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chatbot.attachinvoice.view.adapter.TransactionInvoiceListAdapterTypeFactory

data class TransactionInvoiceUiModel(
        val invoiceId: String = "", val title: String = "", val description: String = "",
        val createdTime: String = "", val statusId: Int = 0, val status: String = "",
        val amount: String = "", val invoiceUrl: String = "", val imageUrl: String = "",
        val userId: String = "", val userName: String = "",
) : Visitable<TransactionInvoiceListAdapterTypeFactory> {
    override fun type(typeFactory: TransactionInvoiceListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
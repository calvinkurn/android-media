package com.tokopedia.chatbot.chatbot2.attachinvoice.view.adapter

import com.tokopedia.chatbot.chatbot2.attachinvoice.view.model.EmptyTransactionInvoiceUiModel
import com.tokopedia.chatbot.chatbot2.attachinvoice.view.model.TransactionInvoiceUiModel

interface TransactionInvoiceListAdapterTypeFactory {
    fun type(transactionInvoiceUiModel: TransactionInvoiceUiModel): Int
    fun type(emptyTransactionInvoiceUiModel: EmptyTransactionInvoiceUiModel): Int
}

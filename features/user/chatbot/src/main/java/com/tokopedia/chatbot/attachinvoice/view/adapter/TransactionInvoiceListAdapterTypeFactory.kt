package com.tokopedia.chatbot.attachinvoice.view.adapter

import com.tokopedia.chatbot.attachinvoice.view.model.EmptyTransactionInvoiceUiModel
import com.tokopedia.chatbot.attachinvoice.view.model.TransactionInvoiceUiModel

interface TransactionInvoiceListAdapterTypeFactory {
    fun type(transactionInvoiceUiModel: TransactionInvoiceUiModel): Int
    fun type(emptyTransactionInvoiceUiModel: EmptyTransactionInvoiceUiModel): Int
}
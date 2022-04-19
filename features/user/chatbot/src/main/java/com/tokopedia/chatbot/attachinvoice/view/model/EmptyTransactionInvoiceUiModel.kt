package com.tokopedia.chatbot.attachinvoice.view.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chatbot.attachinvoice.view.adapter.TransactionInvoiceListAdapterTypeFactory

data class EmptyTransactionInvoiceUiModel(@StringRes val title: Int,
                                          @StringRes val description: Int,
                                          val remoteUrl: String, val showButton: Boolean = false)
    : Visitable<TransactionInvoiceListAdapterTypeFactory> {

    override fun type(typeFactory: TransactionInvoiceListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
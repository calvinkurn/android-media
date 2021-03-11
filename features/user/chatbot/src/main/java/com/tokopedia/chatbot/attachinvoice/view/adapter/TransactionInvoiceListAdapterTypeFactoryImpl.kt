package com.tokopedia.chatbot.attachinvoice.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chatbot.attachinvoice.view.model.EmptyTransactionInvoiceUiModel
import com.tokopedia.chatbot.attachinvoice.view.model.TransactionInvoiceUiModel
import com.tokopedia.chatbot.attachinvoice.view.viewholder.TransactionInvoiceEmptyViewHolder
import com.tokopedia.chatbot.attachinvoice.view.viewholder.TransactionInvoiceViewHolder

class TransactionInvoiceListAdapterTypeFactoryImpl(private val emptyViewHolderListener: TransactionInvoiceEmptyViewHolder.EmptyViewHolderListener)
    : BaseAdapterTypeFactory(), TransactionInvoiceListAdapterTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TransactionInvoiceViewHolder.LAYOUT -> {
                TransactionInvoiceViewHolder(parent)
            }
            TransactionInvoiceEmptyViewHolder.LAYOUT -> {
                TransactionInvoiceEmptyViewHolder(parent, emptyViewHolderListener)
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }

    override fun type(transactionInvoiceUiModel: TransactionInvoiceUiModel): Int {
        return TransactionInvoiceViewHolder.LAYOUT
    }

    override fun type(emptyTransactionInvoiceUiModel: EmptyTransactionInvoiceUiModel): Int {
        return TransactionInvoiceEmptyViewHolder.LAYOUT
    }

}
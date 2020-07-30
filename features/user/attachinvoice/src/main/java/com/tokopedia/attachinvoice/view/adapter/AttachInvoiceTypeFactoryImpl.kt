package com.tokopedia.attachinvoice.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.view.adapter.viewholder.AttachInvoiceViewHolder
import com.tokopedia.attachinvoice.view.adapter.viewholder.EmptyAttachInvoiceViewHolder

class AttachInvoiceTypeFactoryImpl(
        private val emptyListener: EmptyAttachInvoiceViewHolder.Listener
) : BaseAdapterTypeFactory(), AttachInvoiceTypeFactory {

    override fun type(viewModel: EmptyModel?): Int {
        return EmptyAttachInvoiceViewHolder.LAYOUT
    }

    override fun type(invoice: Invoice): Int {
        return AttachInvoiceViewHolder.LAYOUT
    }

    override fun createViewHolder(
            parent: View?,
            type: Int,
            invoiceViewHolder: AttachInvoiceViewHolder.Listener
    ): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            AttachInvoiceViewHolder.LAYOUT -> AttachInvoiceViewHolder(parent, invoiceViewHolder)
            EmptyAttachInvoiceViewHolder.LAYOUT -> EmptyAttachInvoiceViewHolder(parent, emptyListener)
            else -> createViewHolder(parent, type)
        }
    }

}
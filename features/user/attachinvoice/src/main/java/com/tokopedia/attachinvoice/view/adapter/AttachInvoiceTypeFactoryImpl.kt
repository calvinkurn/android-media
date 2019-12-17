package com.tokopedia.attachinvoice.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.view.adapter.viewholder.AttachInvoiceViewHolder

class AttachInvoiceTypeFactoryImpl : BaseAdapterTypeFactory(), AttachInvoiceTypeFactory {

    override fun type(invoice: Invoice): Int {
        return AttachInvoiceViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            AttachInvoiceViewHolder.LAYOUT -> AttachInvoiceViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
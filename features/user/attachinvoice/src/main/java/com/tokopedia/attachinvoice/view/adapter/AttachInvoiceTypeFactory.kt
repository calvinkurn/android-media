package com.tokopedia.attachinvoice.view.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.view.adapter.viewholder.AttachInvoiceViewHolder

interface AttachInvoiceTypeFactory : AdapterTypeFactory {
    fun type(invoice: Invoice): Int
    fun createViewHolder(
            view: View,
            type: Int,
            invoiceViewHolder: AttachInvoiceViewHolder.Listener
    ): AbstractViewHolder<out Visitable<*>>
}
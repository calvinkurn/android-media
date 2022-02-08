package com.tokopedia.attachinvoice.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.databinding.ItemAttachinvoiceBinding
import com.tokopedia.attachinvoice.databinding.ItemAttachinvoiceEmptyViewBinding
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
            view: View,
            type: Int,
            invoiceViewHolder: AttachInvoiceViewHolder.Listener
    ): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            AttachInvoiceViewHolder.LAYOUT -> {
                val binding = ItemAttachinvoiceBinding.bind(view)
                AttachInvoiceViewHolder(binding, invoiceViewHolder)
            }
            EmptyAttachInvoiceViewHolder.LAYOUT -> {
                val binding = ItemAttachinvoiceEmptyViewBinding.bind(view)
                EmptyAttachInvoiceViewHolder(
                    binding,
                    emptyListener
                )
            }
            else -> createViewHolder(view, type)
        }
    }
}
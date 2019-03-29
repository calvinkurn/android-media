package com.tokopedia.chatbot.attachinvoice.view.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel
import com.tokopedia.chatbot.attachinvoice.view.viewholder.AttachInvoiceEmptyResultViewHolder
import com.tokopedia.chatbot.attachinvoice.view.viewholder.InvoiceViewHolder

/**
 * Created by Hendri on 22/03/18.
 */

class AttachInvoiceListAdapterTypeFactory : BaseAdapterTypeFactory() {


    fun type(viewModel: InvoiceViewModel): Int {
        return InvoiceViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == InvoiceViewHolder.LAYOUT) {
            InvoiceViewHolder(parent)
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            AttachInvoiceEmptyResultViewHolder(parent)
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            ErrorNetworkViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}

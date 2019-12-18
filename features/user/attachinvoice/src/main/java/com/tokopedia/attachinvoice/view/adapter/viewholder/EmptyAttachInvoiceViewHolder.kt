package com.tokopedia.attachinvoice.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachinvoice.R

class EmptyAttachInvoiceViewHolder(itemView: View?, private val listener: Listener) : AbstractViewHolder<EmptyModel>(itemView) {

    interface Listener {
        fun hideAttachButton()
    }

    override fun bind(element: EmptyModel?) {
        listener.hideAttachButton()
    }

    companion object {
        val LAYOUT = R.layout.item_attachinvoice_empty_view
    }
}
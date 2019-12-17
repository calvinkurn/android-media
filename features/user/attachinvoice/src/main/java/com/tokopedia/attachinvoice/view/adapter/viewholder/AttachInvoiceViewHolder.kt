package com.tokopedia.attachinvoice.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachinvoice.R
import com.tokopedia.attachinvoice.data.Invoice
import kotlinx.android.synthetic.main.item_attachinvoice.view.*

class AttachInvoiceViewHolder(itemView: View?) : AbstractViewHolder<Invoice>(itemView) {

    override fun bind(element: Invoice?) {
        itemView.tvInvoice?.text = element?.attributes?.code
    }

    companion object {
        val LAYOUT = R.layout.item_attachinvoice
    }
}
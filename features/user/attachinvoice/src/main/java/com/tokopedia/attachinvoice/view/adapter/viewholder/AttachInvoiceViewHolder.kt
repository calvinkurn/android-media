package com.tokopedia.attachinvoice.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.attachinvoice.R
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.data.OrderStatusCode
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.item_attachinvoice.view.*

class AttachInvoiceViewHolder(itemView: View?) : AbstractViewHolder<Invoice>(itemView) {

    override fun bind(element: Invoice?) {
        if (element == null) return
        bindThumbnail(element)
        bindLabelInvoiceStatus(element)
        bindTimeStamp(element)
        bindInvoiceCode(element)
        bindProductName(element)
        bindProductPrice(element)
    }

    private fun bindThumbnail(element: Invoice) {
        ImageHandler.loadImageRounded2(
                itemView.context,
                itemView.ivThumbnail,
                element.thumbnailUrl,
                6.toPx().toFloat()
        )
    }

    private fun bindLabelInvoiceStatus(element: Invoice) {
        val labelType = getLabelType(element.statusId)
        itemView.labelInfo?.text = element.status
        itemView.labelInfo?.setLabelType(labelType)
    }

    private fun bindTimeStamp(element: Invoice) {
        itemView.tpTime?.text = element.timeStamp
    }

    private fun bindInvoiceCode(element: Invoice) {
        itemView.tpCode?.text = element.invoiceCode
    }

    private fun bindProductName(element: Invoice) {
        itemView.tpName?.text = element.productName
    }

    private fun bindProductPrice(element: Invoice) {
        itemView.tpPrice?.text = element.productPrice
    }

    private fun getLabelType(statusId: Int?): Int {
        if (statusId == null) return Label.GENERAL_DARK_GREY
        return when (OrderStatusCode.MAP[statusId]) {
            OrderStatusCode.COLOR_RED -> Label.GENERAL_LIGHT_RED
            OrderStatusCode.COLOR_GREEN -> Label.GENERAL_LIGHT_GREEN
            else -> Label.GENERAL_DARK_GREY
        }
    }

    companion object {
        val LAYOUT = R.layout.item_attachinvoice
    }
}
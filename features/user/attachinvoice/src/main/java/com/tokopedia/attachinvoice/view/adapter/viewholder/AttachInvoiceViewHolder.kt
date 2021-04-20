package com.tokopedia.attachinvoice.view.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.attachinvoice.R
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.data.OrderStatusCode
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.item_attachinvoice.view.*

class AttachInvoiceViewHolder(itemView: View?, val listener: Listener) : AbstractViewHolder<Invoice>(itemView) {

    interface Listener {
        fun checkCurrentItem(element: Invoice, position: Int)
        fun uncheckPreviousItem()
        fun isChecked(element: Invoice): Boolean
    }

    override fun bind(element: Invoice?) {
        if (element == null) return
        bindState(element)
        bindThumbnail(element)
        bindLabelInvoiceStatus(element)
        bindTimeStamp(element)
        bindInvoiceCode(element)
        bindProductName(element)
        bindProductPrice(element)
        bindClick(element)
    }

    private fun bindState(element: Invoice) {
        if (listener.isChecked(element)) {
            stateChecked()
        } else {
            stateUnchecked()
        }
    }

    override fun bind(element: Invoice?, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val payload = payloads[0]
            if (payload == PAYLOAD_UNCHECK) stateUnchecked()
        } else {
            super.bind(element, payloads)
        }
    }

    private fun bindThumbnail(element: Invoice) {
        val radius = 6.toPx().toFloat()
        ImageHandler.loadImageRounded2(
                itemView.context,
                itemView.ivThumbnail,
                element.thumbnailUrl,
                radius
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
        itemView.tpCode?.text = element.code
    }

    private fun bindProductName(element: Invoice) {
        itemView.tpName?.text = element.productName
    }

    private fun bindProductPrice(element: Invoice) {
        itemView.tpPrice?.text = element.productPrice
    }

    private fun bindClick(element: Invoice) {
        itemView.clContainer?.setOnClickListener {
            toggle(element)
        }
    }

    private fun toggle(element: Invoice) {
        itemView.rbSelect?.apply {
            val checkState = !isChecked
            isChecked = checkState
            if (isChecked) {
                listener.uncheckPreviousItem()
                listener.checkCurrentItem(element, adapterPosition)
                stateChecked()
            } else {
                listener.uncheckPreviousItem()
            }
        }
    }

    private fun stateChecked() {
        val color = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G100)
        itemView.clContainer?.setBackgroundColor(color)
        itemView.rbSelect?.isChecked = true
    }

    private fun stateUnchecked() {
        itemView.clContainer?.setBackgroundColor(Color.TRANSPARENT)
        itemView.rbSelect?.isChecked = false
    }

    private fun getLabelType(statusId: Int?): Int {
        if (statusId == null) return Label.GENERAL_DARK_GREY
        return when (OrderStatusCode.MAP[statusId]) {
            OrderStatusCode.COLOR_RED -> Label.GENERAL_LIGHT_RED
            OrderStatusCode.COLOR_GREEN -> Label.GENERAL_LIGHT_GREEN
            else -> Label.GENERAL_DARK_GREY
        }
    }

    override fun onViewRecycled() {
        stateUnchecked()
        super.onViewRecycled()
    }

    companion object {
        val LAYOUT = R.layout.item_attachinvoice

        const val PAYLOAD_UNCHECK = "uncheck"
    }
}
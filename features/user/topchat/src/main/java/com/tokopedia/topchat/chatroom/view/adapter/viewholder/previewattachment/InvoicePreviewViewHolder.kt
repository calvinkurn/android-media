package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.OrderStatusCode
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewViewModel
import com.tokopedia.unifycomponents.Label

class InvoicePreviewViewHolder(
        itemView: View,
        attachmentItemPreviewListener: AttachmentItemPreviewListener
) : AttachmentPreviewViewHolder<InvoicePreviewViewModel>(itemView, attachmentItemPreviewListener) {

    private val thumbnail: ImageView? = itemView.findViewById(R.id.iv_thumbnail)
    private val status: Label? = itemView.findViewById(R.id.tv_status)
    private val invoiceId: TextView? = itemView.findViewById(R.id.tv_invoice_id)
    private val price: TextView? = itemView.findViewById(R.id.tv_price)

    override fun getButtonView(itemView: View): ImageView? {
        return itemView.findViewById(R.id.iv_close)
    }

    override fun bind(model: InvoicePreviewViewModel, position: Int) {
        super.bind(model, position)
        bindViewWithModel(model)
    }

    private fun bindViewWithModel(model: InvoicePreviewViewModel) {
        val labelType = getLabelType(model.statusId.toInt())

        ImageHandler.loadImageRounded2(itemView.context, thumbnail, model.imageUrl)
        status?.text = model.status
        status?.setLabelType(labelType)
        invoiceId?.text = model.invoiceCode
        price?.text = model.totalPriceAmount
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
        val LAYOUT = R.layout.item_invoice_preview
    }
}
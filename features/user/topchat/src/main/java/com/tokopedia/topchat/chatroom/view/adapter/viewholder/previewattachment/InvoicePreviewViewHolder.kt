package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.OrderStatusCode
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.Label

class InvoicePreviewViewHolder(
        itemView: View,
        attachmentItemPreviewListener: AttachmentItemPreviewListener
) : AttachmentPreviewViewHolder<InvoicePreviewUiModel>(itemView, attachmentItemPreviewListener) {

    private val container: ConstraintLayout? = itemView.findViewById(R.id.cl_chat_bubble)
    private val thumbnail: ImageView? = itemView.findViewById(R.id.iv_thumbnail)
    private val status: Label? = itemView.findViewById(R.id.tv_status)
    private val invoiceId: TextView? = itemView.findViewById(R.id.tv_invoice_id)
    private val bg = ViewUtil.generateBackgroundWithShadow(
            container,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_topchat_16,
            R.dimen.dp_topchat_16,
            R.dimen.dp_topchat_16,
            R.dimen.dp_topchat_16,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_6,
            R.dimen.dp_topchat_6,
            Gravity.CENTER
    )

    override fun getButtonView(itemView: View): ImageView? {
        return itemView.findViewById(R.id.iv_close)
    }

    override fun bind(model: InvoicePreviewUiModel) {
        bindViewWithModel(model)
        bindBackground(model)
        super.bind(model)
    }

    private fun bindBackground(model: InvoicePreviewUiModel) {
        container?.background = bg
    }

    private fun bindViewWithModel(model: InvoicePreviewUiModel) {
        val labelType = getLabelType(model.statusId)

        ImageHandler.loadImageRounded2(itemView.context, thumbnail, model.imageUrl, 8f.toPx())
        status?.text = model.status
        status?.setLabelType(labelType)
        invoiceId?.text = model.invoiceCode
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
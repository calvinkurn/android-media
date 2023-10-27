package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.invoice

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.util.helper.InvoiceStatusLabelHelper
import com.tokopedia.chatbot.chatbot2.view.util.view.ViewUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.clearImage
import com.tokopedia.unifycomponents.Label

/**
 * Created by Hendri on 27/03/18.
 */

class AttachedInvoiceSentViewHolder(itemView: View) : BaseChatViewHolder<com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel>(itemView) {

    private val container: RelativeLayout? = itemView.findViewById(R.id.rl_container)
    private val clContainer: ConstraintLayout? = itemView.findViewById(R.id.cl_chat_bubble)
    private val thumbnail: ImageView? = itemView.findViewById(R.id.iv_thumbnail)
    private val status: Label? = itemView.findViewById(R.id.tv_status)
    private val invoiceName: TextView? = itemView.findViewById(R.id.tv_invoice_name)
    private val invoiceDesc: TextView? = itemView.findViewById(R.id.tv_invoice_desc)
    private val invoiceDate: TextView? = itemView.findViewById(R.id.tv_invoice_date)
    private val price: TextView? = itemView.findViewById(R.id.tv_price)
    private val pricePrefix: TextView? = itemView.findViewById(R.id.tv_price_prefix)
    private val radiusInvoice: Float = itemView.context.resources.getDimension(R.dimen.dp_chatbot_6)

    private val bgSender = ViewUtil.generateBackgroundWithShadow(
        clContainer,
        R.color.chatbot_dms_left_message_bg,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER,
        R.color.chatbot_dms_stroke,
        R.dimen.dp_chatbot_3
    )

    override fun bind(element: com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel) {
        alignLayout(element)
        bindViewWithModel(element)
        bindBackground()
    }

    private fun bindViewWithModel(invoice: com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel) {
        thumbnail?.loadImageRounded(invoice.imageUrl, radiusInvoice)
        setStatus(invoice)
        invoiceName?.text = invoice.message
        invoiceDesc?.text = invoice.description
        setPrice(invoice.totalAmount)
        invoiceDate?.text = invoice.createTime
    }

    private fun setStatus(invoice: com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel) {
        if (invoice.status.isNotEmpty()) {
            val labelType: Int = if (invoice.color.isEmpty()) {
                InvoiceStatusLabelHelper.getLabelTypeWithStatusId(invoice.statusId)
            } else {
                InvoiceStatusLabelHelper.getLabelType(invoice.color)
            }
            status?.text = invoice.status
            status?.setLabelType(labelType)
        } else {
            status?.invisible()
        }
    }

    private fun setPrice(totalAmount: String?) {
        if (totalAmount.isNullOrEmpty()) {
            pricePrefix?.hide()
            price?.hide()
        } else {
            pricePrefix?.show()
            price?.text = totalAmount
            price?.show()
        }
    }

    private fun bindBackground() {
        clContainer?.background = bgSender
    }

    private fun alignLayout(uiModel: com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel) {
        alignBubble(Gravity.END)
    }

    private fun alignBubble(gravity: Int) {
        container?.gravity = gravity
    }

    override fun onViewRecycled() {
        if (thumbnail != null) {
            thumbnail.clearImage()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_attached_invoice_sent
    }
}

package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.util.InvoiceStatusLabelHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Label

/**
 * Created by Hendri on 27/03/18.
 */

class AttachedInvoiceSentViewHolder(itemView: View) : BaseChatViewHolder<AttachInvoiceSentUiModel>(itemView) {

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
        com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER,
        R.color.chatbot_dms_stroke,
        getStrokeWidthSenderDimenRes()
    )

    override fun bind(element: AttachInvoiceSentUiModel) {
        alignLayout(element)
        bindViewWithModel(element)
        bindBackground()
    }

    private fun bindViewWithModel(invoice: AttachInvoiceSentUiModel) {
        ImageHandler.loadImageRounded2(itemView.context, thumbnail, invoice.imageUrl, radiusInvoice)
        setStatus(invoice)
        invoiceName?.text = invoice.message
        invoiceDesc?.text = invoice.description
        setPrice(invoice.totalAmount)
        invoiceDate?.text = invoice.createTime
    }

    private fun setStatus(invoice: AttachInvoiceSentUiModel) {
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

    private fun alignLayout(uiModel: AttachInvoiceSentUiModel) {
        alignBubble(Gravity.END)
    }

    private fun alignBubble(gravity: Int) {
        container?.gravity = gravity
    }

    override fun onViewRecycled() {
        if (thumbnail != null) {
            ImageHandler.clearImage(thumbnail)
        }
    }

    @DimenRes
    fun getStrokeWidthSenderDimenRes(): Int {
        return R.dimen.dp_chatbot_3
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_attached_invoice_sent
    }
}

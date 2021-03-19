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
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chat_common.data.OrderStatusCode
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Label

/**
 * Created by Hendri on 27/03/18.
 */

class AttachedInvoiceSentViewHolder(itemView: View) : BaseChatViewHolder<AttachInvoiceSentViewModel>(itemView) {

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
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            getStrokeWidthSenderDimenRes()
    )


    override fun bind(element: AttachInvoiceSentViewModel) {
        alignLayout(element)
        bindViewWithModel(element)
        bindBackground()
    }

    private fun bindViewWithModel(invoice: AttachInvoiceSentViewModel) {
        ImageHandler.loadImageRounded2(itemView.context, thumbnail, invoice.imageUrl, radiusInvoice)
        setStatus(invoice)
        invoiceName?.text = invoice.message
        invoiceDesc?.text = invoice.description
        setPrice(invoice.totalAmount)
        invoiceDate?.text = invoice.createTime

    }

    private fun setStatus(invoice: AttachInvoiceSentViewModel) {
        if (invoice.status?.isNotEmpty() == true) {
            val labelType = getLabelType(invoice.statusId)
            status?.text = invoice.status
            status?.setLabelType(labelType)
        }else{
            status?.invisible()
        }
    }

    private fun setPrice(totalAmount: String?) {
        if (totalAmount.isNullOrEmpty()) {
            pricePrefix?.hide()
            price?.hide()
        }else{
            pricePrefix?.show()
            price?.text = totalAmount
            price?.show()
        }
    }

    private fun getLabelType(statusId: Int?): Int {
        if (statusId == null) return Label.GENERAL_DARK_GREY
        return when (OrderStatusCode.MAP[statusId]) {
            OrderStatusCode.COLOR_RED -> Label.GENERAL_LIGHT_RED
            OrderStatusCode.COLOR_GREEN -> Label.GENERAL_LIGHT_GREEN
            else -> Label.GENERAL_DARK_GREY
        }
    }

    private fun bindBackground() {
            clContainer?.background = bgSender
    }

    private fun alignLayout(viewModel: AttachInvoiceSentViewModel) {
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
        val LAYOUT = R.layout.attached_invoice_sent_chat_item
    }
}

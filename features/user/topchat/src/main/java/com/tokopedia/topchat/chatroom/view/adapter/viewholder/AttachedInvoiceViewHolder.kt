package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chat_common.data.OrderStatusCode
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.unifycomponents.Label

class AttachedInvoiceViewHolder(itemView: View, private val invoiceThumbnailListener: InvoiceThumbnailListener) : BaseChatViewHolder<AttachInvoiceSentViewModel>(itemView) {

    private val container: RelativeLayout? = itemView.findViewById(R.id.rl_container)
    private val chatBubble: ConstraintLayout? = itemView.findViewById(R.id.cl_chat_bubble)
    private val thumbnail: ImageView? = itemView.findViewById(R.id.iv_thumbnail)
    private val status: Label? = itemView.findViewById(R.id.tv_status)
    private val invoiceId: TextView? = itemView.findViewById(R.id.tv_invoice_id)
    private val price: TextView? = itemView.findViewById(R.id.tv_price)

    interface InvoiceThumbnailListener {
        fun onClickInvoiceThumbnail(url: String, id: String)
    }

    override fun bind(viewModel: AttachInvoiceSentViewModel?) {
        if (viewModel == null) return
        super.bind(viewModel)
        alignLayout(viewModel)
        bindViewWithModel(viewModel)
        assignInteraction(viewModel)
    }

    private fun alignLayout(viewModel: AttachInvoiceSentViewModel) {
        if (viewModel.isSender) {
            alignBubbleRight()
        } else {
            alignBubbleLeft()
        }
    }

    private fun alignBubbleRight() {
        alignBubble(Gravity.END, R.drawable.attach_product_right_bubble)
    }

    private fun alignBubbleLeft() {
        alignBubble(Gravity.START, R.drawable.attach_product_left_bubble)
    }

    private fun alignBubble(gravity: Int, @DrawableRes background: Int) {
        val bubbleBackground = ContextCompat.getDrawable(itemView.context, background)
        chatBubble?.background = bubbleBackground
        container?.gravity = gravity
    }

    private fun bindViewWithModel(viewModel: AttachInvoiceSentViewModel) {
        val labelType = getLabelType(viewModel.statusId)

        ImageHandler.loadImageRounded2(itemView.context, thumbnail, viewModel.imageUrl)
        status?.text = viewModel.status
        status?.setLabelType(labelType)
        invoiceId?.text = viewModel.invoiceId
        price?.text = viewModel.totalAmount
    }

    private fun getLabelType(statusId: Int?): Int {
        if (statusId == null) return 1
        return when (OrderStatusCode.MAP[statusId]) {
            OrderStatusCode.COLOR_RED -> Label.GENERAL_LIGHT_RED
            OrderStatusCode.COLOR_GREEN -> Label.GENERAL_LIGHT_GREEN
            else -> Label.GENERAL_DARK_GREY
        }
    }

    private fun assignInteraction(viewModel: AttachInvoiceSentViewModel) {
        itemView.setOnClickListener {
            viewModel.invoiceUrl?.let {
                invoiceThumbnailListener.onClickInvoiceThumbnail(it, it)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_attach_invoice
    }
}
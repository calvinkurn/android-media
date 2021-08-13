package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.OrderStatusCode
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getStrokeWidthSenderDimenRes
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LoaderUnify

class AttachedInvoiceViewHolder(
        itemView: View,
        private val invoiceThumbnailListener: InvoiceThumbnailListener,
        private val deferredAttachment: DeferredViewHolderAttachment
) : BaseChatViewHolder<AttachInvoiceSentViewModel>(itemView) {

    private val container: RelativeLayout? = itemView.findViewById(R.id.rl_container)
    private val clContainer: ConstraintLayout? = itemView.findViewById(R.id.cl_chat_bubble)
    private val thumbnail: ImageView? = itemView.findViewById(R.id.iv_thumbnail)
    private val status: Label? = itemView.findViewById(R.id.tv_status)
    private val invoiceId: TextView? = itemView.findViewById(R.id.tv_invoice_id)
    private val price: TextView? = itemView.findViewById(R.id.tv_price)
    private var loadView: LoaderUnify? = itemView.findViewById(R.id.loader_invoice)
    private val radiusInvoice: Float = itemView.context.resources.getDimension(R.dimen.dp_topchat_6)

    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
            clContainer,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER
    )
    private val bgSender = ViewUtil.generateBackgroundWithShadow(
            clContainer,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            getStrokeWidthSenderDimenRes()
    )

    interface InvoiceThumbnailListener {
        fun onClickInvoiceThumbnail(url: String, id: String)
        fun trackClickInvoice(viewModel: AttachInvoiceSentViewModel)
    }

    override fun alwaysShowTime(): Boolean = true

    override fun bind(invoice: AttachInvoiceSentViewModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads[0]) {
            DeferredAttachment.PAYLOAD_DEFERRED -> bindDeferredAttachment(invoice)
        }
    }

    private fun bindDeferredAttachment(invoice: AttachInvoiceSentViewModel) {
        bind(invoice)
    }

    override fun bind(invoice: AttachInvoiceSentViewModel) {
        super.bind(invoice)
        bindSyncInvoice(invoice)
        alignLayout(invoice)
        if (invoice.isLoading && !invoice.isError) {
            bindIsLoading(invoice)
        } else {
            bindIsLoading(invoice)
            bindViewWithModel(invoice)
            assignInteraction(invoice)
            bindBackground(invoice)
        }
    }

    private fun bindBackground(invoice: AttachInvoiceSentViewModel) {
        if (invoice.isSender) {
            clContainer?.background = bgSender
        } else {
            clContainer?.background = bgOpposite
        }
    }

    private fun bindIsLoading(invoice: AttachInvoiceSentViewModel) {
        if (invoice.isLoading) {
            loadView?.show()
        } else {
            loadView?.hide()
        }
    }

    private fun bindSyncInvoice(invoice: AttachInvoiceSentViewModel) {
        if (!invoice.isLoading) return
        val chatAttachments = deferredAttachment.getLoadedChatAttachments()
        val attachment = chatAttachments[invoice.attachmentId] ?: return
        if (attachment is ErrorAttachment) {
            invoice.syncError()
        } else {
            invoice.updateData(attachment.parsedAttributes)
        }
    }

    private fun alignLayout(viewModel: AttachInvoiceSentViewModel) {
        if (viewModel.isSender) {
            alignBubbleRight(viewModel)
            bindChatReadStatus(viewModel)
        } else {
            alignBubbleLeft(viewModel)
        }
    }

    private fun alignBubbleRight(viewModel: AttachInvoiceSentViewModel) {
        alignBubble(Gravity.END)
    }

    private fun alignBubbleLeft(viewModel: AttachInvoiceSentViewModel) {
        alignBubble(Gravity.START)
    }

    private fun alignBubble(gravity: Int) {
        container?.gravity = gravity
    }

    private fun bindViewWithModel(viewModel: AttachInvoiceSentViewModel) {
        val labelType = getLabelType(viewModel.statusId)

        ImageHandler.loadImageRounded2(itemView.context, thumbnail, viewModel.imageUrl, radiusInvoice)
        status?.text = viewModel.status
        status?.setLabelType(labelType)
        invoiceId?.text = viewModel.invoiceId
        price?.text = viewModel.totalAmount
    }

    private fun getLabelType(statusId: Int?): Int {
        if (statusId == null) return Label.GENERAL_DARK_GREY
        return when (OrderStatusCode.MAP[statusId]) {
            OrderStatusCode.COLOR_RED -> Label.GENERAL_LIGHT_RED
            OrderStatusCode.COLOR_GREEN -> Label.GENERAL_LIGHT_GREEN
            else -> Label.GENERAL_DARK_GREY
        }
    }

    private fun assignInteraction(viewModel: AttachInvoiceSentViewModel) {
        itemView.setOnClickListener {
            invoiceThumbnailListener.trackClickInvoice(viewModel)
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
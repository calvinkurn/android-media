package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chat_common.data.AttachInvoiceSentUiModel
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.OrderStatusCode
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.Payload
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getStrokeWidthSenderDimenRes
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LoaderUnify

class AttachedInvoiceViewHolder(
        itemView: View,
        private val invoiceThumbnailListener: InvoiceThumbnailListener,
        private val deferredAttachment: DeferredViewHolderAttachment
) : BaseChatViewHolder<AttachInvoiceSentUiModel>(itemView) {

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
            com.tokopedia.unifyprinciples.R.color.Unify_Background,
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
            com.tokopedia.unifyprinciples.R.color.Unify_Background,
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
        fun trackClickInvoice(uiModel: AttachInvoiceSentUiModel)
    }

    override fun alwaysShowTime(): Boolean = true

    override fun bind(invoice: AttachInvoiceSentUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads[0]) {
            Payload.REBIND -> bind(invoice)
            DeferredAttachment.PAYLOAD_DEFERRED -> bindDeferredAttachment(invoice)
        }
    }

    private fun bindDeferredAttachment(invoice: AttachInvoiceSentUiModel) {
        bind(invoice)
    }

    override fun bind(invoice: AttachInvoiceSentUiModel) {
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

    private fun bindBackground(invoice: AttachInvoiceSentUiModel) {
        if (invoice.isSender) {
            clContainer?.background = bgSender
        } else {
            clContainer?.background = bgOpposite
        }
    }

    private fun bindIsLoading(invoice: AttachInvoiceSentUiModel) {
        if (invoice.isLoading) {
            loadView?.show()
        } else {
            loadView?.hide()
        }
    }

    private fun bindSyncInvoice(invoice: AttachInvoiceSentUiModel) {
        if (!invoice.isLoading) return
        val chatAttachments = deferredAttachment.getLoadedChatAttachments()
        val attachment = chatAttachments[invoice.attachmentId] ?: return
        if (attachment is ErrorAttachment) {
            invoice.syncError()
        } else {
            invoice.updateData(attachment.parsedAttributes)
        }
    }

    private fun alignLayout(uiModel: AttachInvoiceSentUiModel) {
        if (uiModel.isSender) {
            alignBubbleRight(uiModel)
            bindChatReadStatus(uiModel)
        } else {
            alignBubbleLeft(uiModel)
        }
    }

    private fun alignBubbleRight(uiModel: AttachInvoiceSentUiModel) {
        alignBubble(Gravity.END)
    }

    private fun alignBubbleLeft(uiModel: AttachInvoiceSentUiModel) {
        alignBubble(Gravity.START)
    }

    private fun alignBubble(gravity: Int) {
        container?.gravity = gravity
    }

    private fun bindViewWithModel(uiModel: AttachInvoiceSentUiModel) {
        val labelType = getLabelType(uiModel.statusId)

        thumbnail?.loadImageRounded(uiModel.imageUrl, radiusInvoice)
        status?.text = uiModel.status
        status?.setLabelType(labelType)
        invoiceId?.text = uiModel.invoiceId
        price?.text = uiModel.totalAmount
    }

    private fun getLabelType(statusId: Int?): Int {
        if (statusId == null) return Label.GENERAL_DARK_GREY
        return when (OrderStatusCode.MAP[statusId]) {
            OrderStatusCode.COLOR_RED -> Label.GENERAL_LIGHT_RED
            OrderStatusCode.COLOR_GREEN -> Label.GENERAL_LIGHT_GREEN
            else -> Label.GENERAL_DARK_GREY
        }
    }

    private fun assignInteraction(uiModel: AttachInvoiceSentUiModel) {
        itemView.setOnClickListener {
            invoiceThumbnailListener.trackClickInvoice(uiModel)
            uiModel.invoiceUrl?.let {
                invoiceThumbnailListener.onClickInvoiceThumbnail(it, it)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_attach_invoice
    }
}
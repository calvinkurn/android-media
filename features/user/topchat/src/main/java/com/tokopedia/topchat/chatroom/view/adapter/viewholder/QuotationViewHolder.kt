package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationUiModel
import kotlinx.android.synthetic.main.item_chat_quotation.view.*
import kotlinx.android.synthetic.main.topchat_quotation_attachment.view.*

class QuotationViewHolder(
        itemView: View?,
        private val chatLinkHandlerListener: ChatLinkHandlerListener,
        private val listener: QuotationListener

) : BaseChatViewHolder<QuotationUiModel>(itemView) {

    interface QuotationListener {
        fun trackClickQuotation(msg: QuotationUiModel)
    }

    private var chatStatus: ImageView? = null

    override fun bind(message: QuotationUiModel?) {
        if (message == null) return
        super.bind(message)
        bindViewId()
        bindBubbleBackground()
        bindBubbleAlignment(message)
        bindProductImage(message)
        bindProductTitle(message)
        bindProductPrice(message)
        bindClick(message)
    }

    override fun alwaysShowTime(): Boolean {
        return true
    }

    private fun bindViewId() {
        with(itemView) {
            chatStatus = findViewById(R.id.chat_status)
        }
    }

    private fun bindBubbleBackground() {
        itemView.quotationAttachmentContainer?.background = getQuotationMessageBackground()
    }

    private fun bindBubbleAlignment(message: QuotationUiModel) {
        if (message.isSender) {
            setChatRight()
            bindChatReadStatus(message)
        } else {
            setChatLeft()
            bindChatStatusImage()
        }
    }

    private fun bindProductImage(message: QuotationUiModel) {
        ImageHandler.loadImageRounded2(
                itemView.context,
                itemView.ivThumbnail,
                message.thumbnailUrl,
                8f.toPx()
        )
    }

    private fun bindProductTitle(message: QuotationUiModel) {
        itemView.tvQuotationName?.text = message.title
    }

    private fun bindProductPrice(message: QuotationUiModel) {
        itemView.tvQuotationPrice?.text = message.price
    }

    private fun bindClick(message: QuotationUiModel) {
        itemView.quotationAttachmentContainer?.setOnClickListener {
            listener.trackClickQuotation(message)
            chatLinkHandlerListener.onGoToWebView(message.url, message.url)
        }
    }

    private fun bindChatStatusImage() {
        chatStatus?.hide()
    }

    private fun setChatLeft() {
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, itemView.quotationAttachmentContainer)
    }

    private fun setChatRight() {
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, itemView.quotationAttachmentContainer)
    }

    private fun getQuotationMessageBackground(): Drawable {
        return MethodChecker.getDrawable(itemView.context, com.tokopedia.chat_common.R.drawable.bg_shadow_attach_product)
    }

    private fun setAlignParent(alignment: Int, view: View) {
        val params = view.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
        params.addRule(alignment)
        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT
        view.layoutParams = params
    }

    companion object {
        val LAYOUT = R.layout.item_chat_quotation
    }
}
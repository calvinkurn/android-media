package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationUiModel
import com.tokopedia.topchat.common.util.ViewUtil
import kotlinx.android.synthetic.main.item_chat_quotation.view.*
import kotlinx.android.synthetic.main.topchat_quotation_attachment.view.*

class QuotationViewHolder(
        itemView: View?,
        private val chatLinkHandlerListener: ChatLinkHandlerListener,
        private val listener: QuotationListener

) : BaseChatViewHolder<QuotationUiModel>(itemView) {

    private val container: RelativeLayout? = itemView?.findViewById(R.id.quotationAttachmentContainer)

    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
            itemView,
            com.tokopedia.unifyprinciples.R.color.Neutral_N0,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            R.color.topchat_message_shadow,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER
    )
    private val bgSender = ViewUtil.generateBackgroundWithShadow(
            itemView,
            com.tokopedia.unifyprinciples.R.color.Neutral_N0,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            R.color.topchat_message_shadow,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            R.color.bg_topchat_right_message,
            R.dimen.dp_topchat_1point5
    )

    interface QuotationListener {
        fun trackClickQuotation(msg: QuotationUiModel)
    }

    private var chatStatus: ImageView? = null

    override fun bind(message: QuotationUiModel) {
        super.bind(message)
        bindViewId()
        bindBubbleBackground(message)
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

    private fun bindBubbleBackground(message: QuotationUiModel) {
        if (message.isSender) {
            container?.background = bgSender
        } else {
            container?.background = bgOpposite
        }
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
package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.view.Gravity
import android.view.View
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class RightChatMessageUnifyViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : ChatbotMessageUnifyViewHolder(itemView, listener, replyBubbleListener) {

     private val bg = ViewUtil.generateBackgroundWithShadow(
            customChatLayout?.fxChat,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_0,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER
    )

    private val backgroundChatWithReplyBubble = ViewUtil.generateBackgroundWithShadow(
        customChatLayout?.fxChat,
        com.tokopedia.unifyprinciples.R.color.Unify_GN50,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_0,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER
    )

    override fun bind(message: MessageUiModel) {
        super.bind(message)
        ChatbotMessageViewHolderBinder.bindChatReadStatus(message, customChatLayout)
        bindBackground()
        if (message.parentReply != null) {
            val senderName = mapSenderName(message.parentReply!!)
            customChatLayout?.fxChat?.background = backgroundChatWithReplyBubble
            customChatLayout?.fxChat?.bringToFront()
            setupReplyBubble(senderName, message)
        } else {
            customChatLayout?.replyBubbleContainer?.hide()
        }
    }

    private fun setupReplyBubble(senderName: String, message: MessageUiModel) {
        customChatLayout?.replyBubbleContainer?.apply {
            composeMsg(senderName, message.parentReply?.mainText, message.parentReply)
            updateReplyButtonState(true)
            updateBackground(ReplyBubbleAreaMessage.RIGHT_ORIENTATION)
            updateCloseButtonState(false)
            show()
        }
    }

    private fun bindBackground() {
        customChatLayout?.background = bg
    }

    override fun getChatStatusId(): Int {
        return R.id.ivCheckMark
    }

    override fun alwaysShowTime(): Boolean {
        return true
    }

    companion object {
        val LAYOUT = R.layout.item_chatbot_chat_right
    }
}
package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.view.Gravity
import android.view.View
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder2
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDp

class RightChatMessageUnifyViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : ChatbotMessageUnifyViewHolder(itemView, listener, replyBubbleListener) {

     val bg = ViewUtil.generateBackgroundWithShadow(
            customChatLayout,
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

    val bgForChatReplyBubble = ViewUtil.generateBackgroundWithShadow(
        customChatLayout,
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
        ChatbotMessageViewHolderBinder2.bindChatReadStatus(message, customChatLayout)
   //     bindBackground(message)

        customChatLayout?.background = bgForChatReplyBubble

        if (message.parentReply != null) {
            customChatLayout?.replyBubbleContainer?.composeMsg(message.parentReply?.name, message.parentReply?.mainText)
            customChatLayout?.replyBubbleContainer?.updateReplyButtonState(true)
            customChatLayout?.replyBubbleContainer?.updateBackground(false)
            customChatLayout?.replyBubbleContainer?.updateCloseButtonState(false)
            customChatLayout?.replyBubbleContainer?.show()
        } else {
            customChatLayout?.replyBubbleContainer?.hide()
        }
    }

    private fun bindBackground(message: MessageUiModel) {
    //    customChatLayout?.background = bg
    //    replyBubbleArea?.updateBackground(false)
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
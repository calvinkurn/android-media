package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.chatbubble

import android.view.Gravity
import android.view.View
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.chatbot2.view.util.view.ViewUtil
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface

class RightChatMessageUnifyViewHolder(
    itemView: View?,
    listener: ChatLinkHandlerListener,
    replyBubbleListener: ReplyBubbleAreaMessage.Listener,
    private val userSession: UserSessionInterface
) : ChatbotMessageUnifyViewHolder(itemView, listener, replyBubbleListener, userSession) {

    private val backgroundChatWithoutReplyBubble = ViewUtil.generateBackgroundWithShadow(
        customChatLayout?.fxChat,
        R.color.chatbot_dms_right_chat_message_bg,
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
            val senderName = mapSenderName(message)
            customChatLayout?.fxChat?.background = backgroundChatWithReplyBubble
            customChatLayout?.fxChat?.bringToFront()
            customChatLayout?.apply {
                setPadding(paddingLeft, paddingTop, rightMargin, paddingBottom)
            }
            customChatLayout?.background = null
            setupReplyBubble(senderName, message)
        } else {
            customChatLayout?.fxChat?.background = backgroundChatWithoutReplyBubble
            customChatLayout?.replyBubbleContainer?.hide()
        }
    }

    private fun mapSenderName(message: MessageUiModel): String {
        return message.parentReply?.name ?: ""
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
        customChatLayout?.background = backgroundChatWithoutReplyBubble
    }

    override val chatStatusId: Int
        get() = R.id.ivCheckMark

    override fun alwaysShowTime(): Boolean {
        return true
    }

    companion object {
        val LAYOUT = R.layout.item_chatbot_chat_right
        const val rightMargin = -4
    }
}

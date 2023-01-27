package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.chatbubble

import android.view.Gravity
import android.view.View
import com.google.gson.GsonBuilder
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.data.senderinfo.SenderInfoData
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.chatbot2.view.util.view.ViewUtil
import com.tokopedia.chatbot.chatbot2.view.util.view.isInDarkMode
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface

class LeftChatMessageUnifyViewHolder(
    itemView: View?,
    listener: ChatLinkHandlerListener,
    private val chatbotAdapterListener: ChatbotAdapterListener,
    replyBubbleListener: ReplyBubbleAreaMessage.Listener,
    private val userSession: UserSessionInterface
) : ChatbotMessageUnifyViewHolder(itemView, listener, replyBubbleListener, userSession) {

    private val senderAvatar = itemView?.findViewById<ImageUnify>(R.id.senderAvatar)
    private val senderName = itemView?.findViewById<Typography>(R.id.senderName)

    private val backgroundForChat = ViewUtil.generateBackgroundWithShadow(
        customChatLayout?.fxChat,
        R.color.chatbot_dms_left_chat_message_bg,
        R.dimen.dp_chatbot_0,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER
    )

    override fun bind(message: MessageUiModel) {
        super.bind(message)
        hideSenderInfo()
        val senderInfoData = convertToSenderInfo(message.source)

        bindBackground()
        if (chatbotAdapterListener.isPreviousItemSender(adapterPosition)) {
            senderInfoData?.let { bindSenderInfo(it) }
        }

        if (message.parentReply != null) {
            val senderName = mapSenderName(message)
            customChatLayout?.fxChat?.background = backgroundForChat
            customChatLayout?.fxChat?.bringToFront()
            customChatLayout?.background = null
            setupReplyBubble(senderName, message)
        } else {
            bindBackground()
            customChatLayout?.replyBubbleContainer?.hide()
        }
    }

    private fun mapSenderName(messageUiModel: MessageUiModel): String {
        if (userSession.userId == messageUiModel.parentReply?.senderId) {
            return userSession.name
        }
        val senderInfoData = convertToSenderInfo(messageUiModel.source)
        if (senderInfoData != null) {
            return senderInfoData.name ?: ""
        }
        return ""
    }

    private fun setupReplyBubble(senderName: String, message: MessageUiModel) {
        customChatLayout?.replyBubbleContainer?.apply {
            composeMsg(senderName, message.parentReply?.mainText, message.parentReply)
            updateReplyButtonState(true)
            updateBackground(ReplyBubbleAreaMessage.LEFT_ORIENTATION)
            updateCloseButtonState(false)
            show()
        }
    }

    private fun hideSenderInfo() {
        senderAvatar?.hide()
        senderName?.hide()
    }

    private fun bindSenderInfo(senderInfoData: SenderInfoData) {
        senderInfoData.iconUrl?.let {
            senderAvatar?.setImageUrl(it)
        }
        if (itemView.isInDarkMode()) {
            senderInfoData.iconDarkUrl?.let {
                senderAvatar?.setImageUrl(it)
            }
        }

        senderAvatar?.show()
        senderName?.show()
        senderName?.text = senderInfoData.name
    }

    private fun convertToSenderInfo(source: String): SenderInfoData? {
        val senderInfoPrefix = itemView.context.getString(R.string.chatbot_sender_info_prefix)
        if (source.isNotEmpty() && source.startsWith(senderInfoPrefix)) {
            val s = source.substring(senderInfoPrefix.length, source.length)
            return GsonBuilder().create()
                .fromJson<SenderInfoData>(
                    s,
                    SenderInfoData::class.java
                )
        } else {
            return null
        }
    }

    private fun bindBackground() {
        customChatLayout?.background = backgroundForChat
    }

    private fun bindMessageInfo(message: MessageUiModel) {
        if (!message.isSender) {
            customChatLayout?.fxChat?.showReadMoreView()
        } else {
            customChatLayout?.fxChat?.hideReadMoreView()
        }
    }

    override fun getDateContainerId(): Int = R.id.dateContainer

    companion object {
        val LAYOUT = R.layout.item_chatbot_chat_left
    }
}

package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.view.Gravity
import android.view.View
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.domain.pojo.senderinfo.SenderInfoData
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class LeftChatMessageUnifyViewHolder(
    itemView: View?,
    listener: ChatLinkHandlerListener,
    private val chatbotAdapterListener: ChatbotAdapterListener,
    replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : ChatbotMessageUnifyViewHolder(itemView, listener, replyBubbleListener) {

    private val senderAvatar = itemView?.findViewById<ImageUnify>(R.id.senderAvatar)
    private val senderName = itemView?.findViewById<Typography>(R.id.senderName)

    private val backgroundForChat = ViewUtil.generateBackgroundWithShadow(
        customChatLayout?.fxChat,
        com.tokopedia.unifyprinciples.R.color.Unify_N0,
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

        if (chatbotAdapterListener.isPreviousItemSender(adapterPosition)) {
            senderInfoData?.let { bindSenderInfo(it) }
        }

        if (message.parentReply != null) {
            val senderName = mapSenderName(message.parentReply!!)
            customChatLayout?.fxChat?.background = backgroundForChat
            customChatLayout?.fxChat?.bringToFront()
            customChatLayout?.background = null
            setupReplyBubble(senderName,message)
        } else {
            bindBackground()
            customChatLayout?.replyBubbleContainer?.hide()
        }

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

    override fun mapSenderName(parentReply: ParentReply): String {
        if (parentReply.name == ChatbotConstant.TANYA)
            return ChatbotConstant.TOKOPEDIA_CARE
        return parentReply.name
    }

    private fun hideSenderInfo() {
        senderAvatar?.hide()
        senderName?.hide()
    }

    private fun bindSenderInfo(senderInfoData: SenderInfoData) {
        senderAvatar?.show()
        senderName?.show()
        ImageHandler.loadImageCircle2(itemView.context, senderAvatar, senderInfoData.iconUrl)
        senderName?.text = senderInfoData.name
    }

    private fun convertToSenderInfo(source: String): SenderInfoData? {
        val senderInfoPrefix = itemView.context.getString(R.string.chatbot_sender_info_prefix)
        if (source.isNotEmpty() && source.startsWith(senderInfoPrefix)) {
            val s = source.substring(senderInfoPrefix.length, source.length)
            return GsonBuilder().create()
                .fromJson<SenderInfoData>(s,
                    SenderInfoData::class.java)
        } else return null
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
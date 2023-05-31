package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder

import android.view.View
import com.tokopedia.chat_common.data.FallbackAttachmentUiModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder

class ChatbotFallbackAttachmentViewHolder(
    itemView: View,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
    chatbotAdapterListener: ChatbotAdapterListener
) :
    BaseChatBotViewHolder<FallbackAttachmentUiModel>(itemView, chatbotAdapterListener) {

    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    override fun bind(uiModel: FallbackAttachmentUiModel) {
        super.bind(uiModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(uiModel.message, customChatLayout, movementMethod)
    }

    override fun getCustomChatLayoutId(): Int = R.id.customChatLayout
    override fun getSenderAvatarId(): Int = R.id.senderAvatar
    override fun getSenderNameId(): Int = R.id.senderName
    override fun getDateContainerId(): Int = R.id.dateContainer

    companion object {
        val LAYOUT = R.layout.item_chatbot_fallback_attachment
    }
}

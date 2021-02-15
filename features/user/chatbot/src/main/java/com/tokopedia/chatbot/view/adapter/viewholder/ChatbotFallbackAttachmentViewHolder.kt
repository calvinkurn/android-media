package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import com.tokopedia.chat_common.data.FallbackAttachmentViewModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.customview.CustomChatbotChatLayout

class ChatbotFallbackAttachmentViewHolder(itemView: View,
                                          private val chatLinkHandlerListener: ChatLinkHandlerListener)
    : BaseChatBotViewHolder<FallbackAttachmentViewModel>(itemView) {

    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    override fun bind(viewModel: FallbackAttachmentViewModel) {
        super.bind(viewModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(viewModel.message, customChatLayout, movementMethod)
    }

    override fun getCustomChatLayoutId(): Int =  com.tokopedia.chatbot.R.id.customChatLayout

    companion object {
        val LAYOUT = R.layout.chatbot_fallback_attachment_layout
    }
}
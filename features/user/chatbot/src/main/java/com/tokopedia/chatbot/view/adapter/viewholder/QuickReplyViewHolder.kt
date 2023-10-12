package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener

/**
 * @author by nisie on 5/8/18.
 */
class QuickReplyViewHolder(
    itemView: View,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
    chatbotAdapterListener: ChatbotAdapterListener
) :
    BaseChatBotViewHolder<QuickReplyListUiModel>(itemView, chatbotAdapterListener) {

    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    override fun bind(viewModel: QuickReplyListUiModel) {
        super.bind(viewModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(viewModel.message, customChatLayout, movementMethod)
    }

    override fun getCustomChatLayoutId(): Int = R.id.customChatLayout
    override fun getSenderAvatarId(): Int = R.id.senderAvatar
    override fun getSenderNameId(): Int = R.id.senderName
    override fun getDateContainerId(): Int = R.id.dateContainer

    companion object {
        val LAYOUT = R.layout.item_chatbot_quick_reply
    }
}

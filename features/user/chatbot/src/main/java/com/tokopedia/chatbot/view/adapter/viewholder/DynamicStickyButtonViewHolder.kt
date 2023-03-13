package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.dynamicattachment.dynamicstickybutton.DynamicStickyButtonUiModel
import com.tokopedia.chatbot.databinding.ItemChatbotDynamicContentCode105Binding
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.utils.view.binding.viewBinding

class DynamicStickyButtonViewHolder(itemView: View,
                                    private val chatLinkHandlerListener: ChatLinkHandlerListener,): BaseChatBotViewHolder<DynamicStickyButtonUiModel>(itemView) {

    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    private var binding: ItemChatbotDynamicContentCode105Binding? by viewBinding()

    override fun bind(uiModel: DynamicStickyButtonUiModel) {
        super.bind(uiModel)
        verifyReplyTime(uiModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(uiModel.contentText, customChatLayout, movementMethod)
        ChatbotMessageViewHolderBinder.bindHour(uiModel.replyTime, customChatLayout)

        binding?.apply {
            chatbotTvMessageContent.message?.text = uiModel.contentText ?: ""
            actionButton.text = uiModel.actionBubble.text
        }
    }


    override fun getCustomChatLayoutId(): Int = R.id.chatbot_tv_message_content

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_dynamic_content_code_105
    }
}

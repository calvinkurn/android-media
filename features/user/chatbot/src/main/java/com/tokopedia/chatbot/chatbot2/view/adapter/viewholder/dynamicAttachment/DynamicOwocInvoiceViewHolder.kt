package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.BaseChatBotViewHolder
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicOwocInvoiceUiModel
import com.tokopedia.chatbot.chatbot2.view.util.ChatBackground
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.databinding.ItemChatbotDynamicContentCode106Binding
import com.tokopedia.chatbot.databinding.ItemChatbotDynamicOwocBinding
import com.tokopedia.chatbot.databinding.ItemChatbotOwocInvoiceBinding
import com.tokopedia.utils.view.binding.viewBinding

class DynamicOwocInvoiceViewHolder(
    itemView: View,
    private val chatLinkHandlerListener: ChatLinkHandlerListener
): BaseChatBotViewHolder<DynamicOwocInvoiceUiModel>(itemView){

    private var binding: ItemChatbotDynamicOwocBinding? by viewBinding()
    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    override fun bind(uiModel: DynamicOwocInvoiceUiModel) {
        super.bind(uiModel)

        verifyReplyTime(uiModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(
            uiModel.message,
            customChatLayout,
            movementMethod
        )
        ChatbotMessageViewHolderBinder.bindHour(uiModel.replyTime, customChatLayout)

        binding?.apply {
            customChatLayout.message?.text = uiModel.message

        }


    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_dynamic_owoc
    }
}

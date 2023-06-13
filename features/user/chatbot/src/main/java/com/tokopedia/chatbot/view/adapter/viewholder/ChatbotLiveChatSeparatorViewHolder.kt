package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.databinding.ItemChatbotLiveChatSeparatorBinding
import com.tokopedia.chatbot.util.ChatBotTimeConverter

class ChatbotLiveChatSeparatorViewHolder(itemView: View) : AbstractViewHolder<ChatSepratorUiModel>(itemView) {
    private val view = ItemChatbotLiveChatSeparatorBinding.bind(itemView)
    private val liveChatSeprator: TextView = view.chatbotLivechatSeprator

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_live_chat_separator
    }

    override fun bind(element: ChatSepratorUiModel) {
        liveChatSeprator.text = String.format("%s. %s", element.sepratorMessage, ChatBotTimeConverter.getHourTime(element.dividerTiemstamp))
    }
}

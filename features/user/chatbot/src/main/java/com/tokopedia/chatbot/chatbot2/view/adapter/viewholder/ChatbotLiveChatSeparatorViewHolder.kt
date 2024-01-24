package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.uimodel.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatBotTimeConverter
import com.tokopedia.chatbot.databinding.ItemChatbotLiveChatSeparatorBinding
import java.util.*

class ChatbotLiveChatSeparatorViewHolder(itemView: View) : AbstractViewHolder<ChatSepratorUiModel>(
    itemView
) {
    private val view = ItemChatbotLiveChatSeparatorBinding.bind(itemView)
    private val liveChatSeprator: TextView = view.chatbotLivechatSeprator

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_live_chat_separator
    }

    override fun bind(element: ChatSepratorUiModel) {
        val text = String.format(
            Locale.getDefault(),
            "%s. %s",
            element.sepratorMessage,
            ChatBotTimeConverter.getHourTime(element.dividerTiemstamp)
        )
        liveChatSeprator.text = MethodChecker.fromHtml(text)
    }
}

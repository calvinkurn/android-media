package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.util.ChatBotTimeConverter

class ChatbotLiveChatSeparatorViewHolder(itemView: View) : AbstractViewHolder<ChatSepratorViewModel>(itemView) {

    private val liveChatSeprator: TextView = itemView.findViewById(R.id.chatbot_livechat_seprator)

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.chatbot_live_chat_seprator_layout
    }

    override fun bind(element: ChatSepratorViewModel) {
        liveChatSeprator.text = String.format("%s. %s", element.sepratorMessage, ChatBotTimeConverter.getHourTime(element.dividerTiemstamp))
    }
}
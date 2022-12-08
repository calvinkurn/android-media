package com.tokopedia.chatbot.view.customview.csat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.databinding.ChatbotLayoutCsatOptionItemBinding
import com.tokopedia.csat_rating.quickfilter.ItemFilterViewHolder
import com.tokopedia.csat_rating.quickfilter.QuickSingleFilterAdapter
import com.tokopedia.csat_rating.quickfilter.QuickSingleFilterListener

class ChatBotQuickOptionViewAdapter(actionListener: QuickSingleFilterListener?) : QuickSingleFilterAdapter(actionListener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilterViewHolder {
        val view = ChatbotLayoutCsatOptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatBotOptionItemViewHolder(view.root, actionListener)
    }
}

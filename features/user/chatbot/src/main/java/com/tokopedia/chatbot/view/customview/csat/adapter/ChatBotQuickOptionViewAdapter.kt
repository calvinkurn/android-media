package com.tokopedia.chatbot.view.customview.csat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.chatbot.R
import com.tokopedia.design.quickfilter.ItemFilterViewHolder
import com.tokopedia.design.quickfilter.QuickSingleFilterAdapter
import com.tokopedia.design.quickfilter.QuickSingleFilterListener

class ChatBotQuickOptionViewAdapter(actionListener: QuickSingleFilterListener?) : QuickSingleFilterAdapter(actionListener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilterViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chatbot_layout_csat_option_item, parent, false)
        return ChatBotOptionItemViewHolder(view, actionListener)
    }
}

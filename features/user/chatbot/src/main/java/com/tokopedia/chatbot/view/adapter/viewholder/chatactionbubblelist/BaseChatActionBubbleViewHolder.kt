package com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.unifyprinciples.Typography

open class BaseChatActionBubbleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind(element: ChatActionBubbleViewModel, onSelect: (Int) -> Unit) {}
}

package com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel

open class BaseChatActionBubbleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind(element: ChatActionBubbleUiModel, onSelect: (Int) -> Unit) {}
}

package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.chatactionbubblelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel

open class BaseChatActionBubbleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind(element: ChatActionBubbleUiModel, onSelect: (Int) -> Unit) {}
}

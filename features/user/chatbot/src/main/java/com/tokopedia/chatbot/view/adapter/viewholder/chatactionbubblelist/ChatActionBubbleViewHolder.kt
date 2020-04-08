package com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionBubbleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val chatActionMessage: TextView

    init {
        chatActionMessage = itemView.findViewById(R.id.chat_action_message)
    }

    fun bind(element: ChatActionBubbleViewModel) {
        chatActionMessage.text = element.text
    }
}

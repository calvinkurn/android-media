package com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListViewModel
import com.tokopedia.unifyprinciples.Typography

class ChatActionBubbleViewHolder(itemView: View) : BaseChatActionBubbleViewHolder(itemView) {
    private val chatActionMessage: Typography = itemView.findViewById(R.id.helpfull_question_option)

    override fun bind(element: ChatActionBubbleViewModel, onSelect: (Int) -> Unit) {
        chatActionMessage.text = element.text
        itemView.setOnClickListener { onSelect.invoke(element.bubbleType)  }
    }
}

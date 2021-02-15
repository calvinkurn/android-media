package com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist

import android.view.View
import android.widget.ImageView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.unifyprinciples.Typography

class ChatActionBubbleReadMoreViewHolder(itemView: View) : BaseChatActionBubbleViewHolder(itemView) {
    private val chatActionMessage: Typography = itemView.findViewById(R.id.read_more_options)
    private val arrowUpDown: ImageView = itemView.findViewById(R.id.arrow_up_down)

    override fun bind(element: ChatActionBubbleViewModel, onSelect: (Int) -> Unit) {
        if (element.text ==  "Selngkapnya"){
            arrowUpDown.setImageResource(R.drawable.ic_chevron_down)
        }else{
            arrowUpDown.setImageResource(R.drawable.ic_chevron_up)
        }
        chatActionMessage.text = element.text
        itemView.setOnClickListener { onSelect.invoke(element.bubbleType)  }
    }
}

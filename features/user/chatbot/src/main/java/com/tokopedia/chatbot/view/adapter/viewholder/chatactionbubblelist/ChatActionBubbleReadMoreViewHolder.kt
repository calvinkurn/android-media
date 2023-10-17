package com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist

import android.view.View
import android.widget.ImageView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.Typography

class ChatActionBubbleReadMoreViewHolder(itemView: View) : BaseChatActionBubbleViewHolder(itemView) {
    private val chatActionMessage: Typography = itemView.findViewById(R.id.read_more_options)
    private val arrowUpDown: IconUnify = itemView.findViewById(R.id.arrow_up_down)

    override fun bind(element: ChatActionBubbleUiModel, onSelect: (Int) -> Unit) {
        if (element.text ==  MORE_DETAILS_TEXT){
            arrowUpDown.setImageResource(com.tokopedia.iconunify.R.drawable.iconunify_chevron_down)
        }else{
            arrowUpDown.setImageResource(com.tokopedia.iconunify.R.drawable.iconunify_chevron_up)
        }
        chatActionMessage.text = element.text
        itemView.setOnClickListener { onSelect.invoke(element.bubbleType)  }
    }
}

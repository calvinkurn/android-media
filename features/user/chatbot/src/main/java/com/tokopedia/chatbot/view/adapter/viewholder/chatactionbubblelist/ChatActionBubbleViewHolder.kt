package com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class ChatActionBubbleViewHolder(itemView: View) : BaseChatActionBubbleViewHolder(itemView) {
    private val chatActionMessage: Typography = itemView.findViewById(R.id.helpfull_question_option)
    private val customerCareImage: ImageView = itemView.findViewById(R.id.chat_rating)

    override fun bind(element: ChatActionBubbleUiModel, onSelect: (Int) -> Unit) {
        chatActionMessage.text = element.text
        chatActionMessage.setTextColor(MethodChecker.getColor(itemView.context , (com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)))
        customerCareImage.hide()
        if (element.iconUrl.isNotEmpty()) {
            setLiveChatButtonAction(element)
        }
        itemView.setOnClickListener { onSelect.invoke(element.bubbleType) }
    }

    private fun setLiveChatButtonAction(element: ChatActionBubbleUiModel) {
        chatActionMessage.setTextColor(Color.parseColor(String.format(itemView.context.getString(R.string.chatbot_action_bubble_text_color_prefix), element.hexColor)))
        customerCareImage.show()
        ImageHandler.LoadImage(customerCareImage, element.iconUrl)
    }
}

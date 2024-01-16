package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.chatactionbubblelist

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ChatActionBubbleViewHolder(itemView: View) : BaseChatActionBubbleViewHolder(itemView) {
    private val chatActionMessage: Typography = itemView.findViewById(R.id.helpfull_question_option)
    private val chatActionImage: ImageView = itemView.findViewById(R.id.chat_rating)

    override fun bind(element: ChatActionBubbleUiModel, onSelect: (Int) -> Unit) {
        renderMessage(element)
        renderIcon(element)
        itemView.setOnClickListener { onSelect.invoke(element.bubbleType) }
    }

    private fun renderIcon(element: ChatActionBubbleUiModel) {
        if (element.iconUrl.isNotEmpty()) {
            chatActionImage.loadImage(element.iconUrl)
            chatActionImage.show()
        } else {
            chatActionImage.hide()
        }
    }

    private fun renderMessage(element: ChatActionBubbleUiModel) {
        chatActionMessage.text = element.text
        if (element.hexColor.isNotEmpty()) {
            chatActionMessage.setTextColor(
                Color.parseColor(
                    String.format(
                        itemView.context.getString(R.string.chatbot_action_bubble_text_color_prefix),
                        element.hexColor
                    )
                )
            )
        } else {
            chatActionMessage.setTextColor(
                MethodChecker.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN950_96
                )
            )
        }
    }
}

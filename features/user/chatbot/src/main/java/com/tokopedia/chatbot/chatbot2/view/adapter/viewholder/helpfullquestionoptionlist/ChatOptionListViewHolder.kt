package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.helpfullquestionoptionlist

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.ChatOptionListUiModel
import com.tokopedia.chatbot.databinding.ItemChatbotHelpfulBinding
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

const val OPTION_TYPE_CSAT = "csat"
class ChatOptionListViewHolder(itemView: ItemChatbotHelpfulBinding, private val onOptionListSelected: (ChatOptionListUiModel) -> Unit) : RecyclerView.ViewHolder(itemView.root) {
    private val chatActionMessage: Typography
    private val chatRaing: ImageView

    init {
        chatActionMessage = itemView.helpfullQuestionOption
        chatRaing = itemView.chatRating
    }

    fun bind(element: ChatOptionListUiModel) {
        chatActionMessage.text = element.text
        if (element.type == OPTION_TYPE_CSAT) {
            setChatRating(element.value)
        }
        itemView.setOnClickListener { onOptionListSelected.invoke(element) }
    }

    private fun setChatRating(value: Long) {
        chatRaing.show()
        when (value) {
            1L -> chatRaing.setImageDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.csat_rating.R.drawable.emoji_active_1))
            2L -> chatRaing.setImageDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.csat_rating.R.drawable.emoji_active_2))
            3L -> chatRaing.setImageDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.csat_rating.R.drawable.emoji_active_3))
            4L -> chatRaing.setImageDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.csat_rating.R.drawable.emoji_active_4))
            5L -> chatRaing.setImageDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.csat_rating.R.drawable.emoji_active_5))
        }
    }
}

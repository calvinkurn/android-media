package com.tokopedia.chatbot.view.adapter.viewholder.helpfullquestionoptionlist

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListViewModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

const val OPTION_TYPE_CSAT = "csat"
class ChatOptionListViewHolder(itemView: View, private val onOptionListSelected: (ChatOptionListViewModel) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val chatActionMessage: Typography
    private val chatRaing: ImageView

    init {
        chatActionMessage = itemView.findViewById(R.id.helpfull_question_option)
        chatRaing = itemView.findViewById(R.id.chat_rating)
    }

    fun bind(element: ChatOptionListViewModel) {
        chatActionMessage.text = element.text
        if (element.type == OPTION_TYPE_CSAT){
            setChatRating(element.value)
        }
        itemView.setOnClickListener { onOptionListSelected.invoke(element) }
    }

    private fun setChatRating(value: Int) {
        chatRaing.show()
        when (value) {
            1 -> chatRaing.setImageDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.csat_rating.R.drawable.rating_active_1))
            2 -> chatRaing.setImageDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.csat_rating.R.drawable.rating_active_2))
            3 -> chatRaing.setImageDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.csat_rating.R.drawable.rating_active_3))
            4 -> chatRaing.setImageDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.csat_rating.R.drawable.rating_active_4))
            5 -> chatRaing.setImageDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.csat_rating.R.drawable.rating_active_5))
        }
    }
}

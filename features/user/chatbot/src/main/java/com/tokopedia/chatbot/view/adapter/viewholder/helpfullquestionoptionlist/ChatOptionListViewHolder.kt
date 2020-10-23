package com.tokopedia.chatbot.view.adapter.viewholder.helpfullquestionoptionlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.unifyprinciples.Typography


class ChatOptionListViewHolder(itemView: View, private val onOptionListSelected: (HelpFullQuestionPojo.HelpfulQuestion.HelpfulQuestions) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val chatActionMessage: Typography

    init {
        chatActionMessage = itemView.findViewById(R.id.helpfull_question_option)
    }

    fun bind(element: HelpFullQuestionPojo.HelpfulQuestion.HelpfulQuestions?) {
        chatActionMessage.text = element?.text
        itemView.setOnClickListener { element?.let { selected -> onOptionListSelected.invoke(selected) } }
    }
}

package com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.unifyprinciples.Typography

class SrwQuestionViewHolder constructor(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<QuestionUiModel>(itemView) {

    interface Listener {
        fun onClickSrwQuestion(question: QuestionUiModel)
        fun trackClickSrwQuestion(element: QuestionUiModel)
    }

    private val title: Typography? = itemView?.findViewById(R.id.tp_srw_title)

    override fun bind(element: QuestionUiModel) {
        bindTitle(element)
        bindClick(element)
    }

    private fun bindTitle(element: QuestionUiModel) {
        title?.text = element.content
    }

    private fun bindClick(element: QuestionUiModel) {
        itemView.setOnClickListener {
            listener?.trackClickSrwQuestion(element)
            listener?.onClickSrwQuestion(element)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_srw_question
    }
}
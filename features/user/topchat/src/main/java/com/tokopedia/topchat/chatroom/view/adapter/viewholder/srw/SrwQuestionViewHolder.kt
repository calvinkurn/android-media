package com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.unifyprinciples.Typography

class SrwQuestionViewHolder(
        itemView: View?
) : AbstractViewHolder<QuestionUiModel>(itemView) {

    private val title: Typography? = itemView?.findViewById(R.id.tp_srw_title)

    override fun bind(element: QuestionUiModel) {
        bindTitle(element)
    }

    private fun bindTitle(element: QuestionUiModel) {
        title?.text = element.content
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_srw_question
    }
}
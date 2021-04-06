package com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel

class SrwQuestionViewHolder(
        itemView: View?
) : AbstractViewHolder<QuestionUiModel>(itemView) {
    override fun bind(element: QuestionUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_topchat_srw_question
    }
}
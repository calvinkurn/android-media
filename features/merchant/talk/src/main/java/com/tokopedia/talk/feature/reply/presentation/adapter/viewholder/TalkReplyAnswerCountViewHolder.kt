package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyAnswerCountModel
import com.tokopedia.talk.R
import kotlinx.android.synthetic.main.item_talk_reply_answer_count.view.*

class TalkReplyAnswerCountViewHolder(view: View) : AbstractViewHolder<TalkReplyAnswerCountModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_answer_count
    }
    override fun bind(element: TalkReplyAnswerCountModel) {
        itemView.talkReplyTotalAnswers.text = String.format(getString(R.string.reply_total_answer), element.totalAnswers)
    }


}
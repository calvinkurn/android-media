package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.R
import com.tokopedia.talk.databinding.ItemTalkReplyAnswerCountBinding
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyAnswerCountModel

class TalkReplyAnswerCountViewHolder(
    view: View
) : AbstractViewHolder<TalkReplyAnswerCountModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_answer_count
    }

    private val binding = ItemTalkReplyAnswerCountBinding.bind(view)

    override fun bind(element: TalkReplyAnswerCountModel) {
        binding.talkReplyTotalAnswers.text =
            String.format(getString(R.string.reply_total_answer), element.totalAnswers)
    }


}
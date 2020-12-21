package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyEmptyModel
import com.tokopedia.talk.R
import kotlinx.android.synthetic.main.item_talk_reply_empty_state.view.*

class TalkReplyEmptyViewHolder(view: View) : AbstractViewHolder<TalkReplyEmptyModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_empty_state
        const val TALK_REPLY_EMPTY_IMAGE_OWN_QUESTION_URL = "https://ecs7.tokopedia.net/android/others/talk_reply_own_question_empty_state.png"
        const val TALK_REPLY_EMPTY_IMAGE_DEFAULT_URL = "https://ecs7.tokopedia.net/android/others/talk_reply_empty_state.png"
    }

    override fun bind(element: TalkReplyEmptyModel) {
        if(element.isMyQuestion) {
            showOwnQuestionEmptyState()
            return
        }
        itemView.talkReplyEmptyImage.loadImage(TALK_REPLY_EMPTY_IMAGE_DEFAULT_URL)
    }

    private fun showOwnQuestionEmptyState() {
        with(itemView) {
            talkReplyEmptyContainer.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            talkReplyEmptyImage.loadImage(TALK_REPLY_EMPTY_IMAGE_OWN_QUESTION_URL)
            talkReplyEmptyTitle.text = getString(R.string.reply_empty_title_own_question)
            talkReplyEmptySubtitle.text = getString(R.string.reply_empty_subtitle_own_question)
        }
    }


}
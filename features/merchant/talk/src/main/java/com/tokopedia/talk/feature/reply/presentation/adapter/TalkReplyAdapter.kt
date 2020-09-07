package com.tokopedia.talk.feature.reply.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.talk.feature.reply.data.model.discussion.Answer
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAdapterTypeFactory
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.*

class TalkReplyAdapter(talkReplyAdapterTypeFactory: TalkReplyAdapterTypeFactory) : BaseAdapter<TalkReplyAdapterTypeFactory>(talkReplyAdapterTypeFactory) {

    fun displayAnswers(answerCount: TalkReplyAnswerCountModel, answers: List<TalkReplyUiModel>) {
        visitables.add(answerCount)
        visitables.addAll(answers)
        notifyDataSetChanged()
    }

    fun showEmpty(talkReplyEmptyModel: TalkReplyEmptyModel) {
        visitables.add(talkReplyEmptyModel)
        notifyDataSetChanged()
    }

    fun showProductHeader(talkReplyProductHeaderModel: TalkReplyProductHeaderModel) {
        visitables.add(talkReplyProductHeaderModel)
    }

    fun showHeader(talkReplyHeaderModel: TalkReplyHeaderModel) {
        visitables.add(talkReplyHeaderModel)
    }

    fun setIsFollowingButton(isFollowing: Boolean) {
        visitables.forEachIndexed { index, visitable ->
            if (visitable is TalkReplyHeaderModel) {
                visitables[index] = visitable.copy(isFollowed = isFollowing)
                notifyItemChanged(index)
                return
            }
        }
    }

    fun unmaskQuestion() {
        visitables.forEachIndexed { index, visitable ->
            if (visitable is TalkReplyHeaderModel) {
                visitables[index] = visitable.copy(maskedContent = "", isMasked = false, allowUnmask = false)
                notifyItemChanged(index)
                return
            }
        }
    }

    fun unmaskComment() {
        visitables.forEachIndexed { index, visitable ->
            if (visitable is TalkReplyUiModel) {
                val state = visitable.answer.state.copy(isMasked = false, allowUnmask = false)
                val answer = visitable.answer.copy(maskedContent = "", state = state)
                visitables[index] = visitable.copy(answer = answer)
                notifyItemChanged(index)
                return
            }
        }
    }
}
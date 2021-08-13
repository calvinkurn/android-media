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
        notifyDataSetChanged()
    }

    fun showHeader(talkReplyHeaderModel: TalkReplyHeaderModel) {
        visitables.add(talkReplyHeaderModel)
        notifyDataSetChanged()
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

    fun hasProductHeader(): Boolean {
        return visitables.filterIsInstance(TalkReplyProductHeaderModel::class.java).isNotEmpty()
    }
}
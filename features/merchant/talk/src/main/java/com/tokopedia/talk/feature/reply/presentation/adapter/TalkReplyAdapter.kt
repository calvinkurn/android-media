package com.tokopedia.talk.feature.reply.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
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
                with(visitable) {
                    visitables[index] = TalkReplyHeaderModel(date, question, isFollowing, allowFollow, allowReport, allowDelete, isMasked, maskedContent, userThumbnail , userName, userId, isMyQuestion)
                    notifyItemChanged(index)
                    return
                }
            }
        }
    }
}
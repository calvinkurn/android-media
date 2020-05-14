package com.tokopedia.talk.feature.reply.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAdapterTypeFactory
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.*

class TalkReplyAdapter(talkReplyAdapterTypeFactory: TalkReplyAdapterTypeFactory) : BaseAdapter<TalkReplyAdapterTypeFactory>(talkReplyAdapterTypeFactory) {

    fun displayAnswers(answerCount: TalkReplyAnswerCountModel, answers: List<TalkReplyUiModel>) {
        visitables.add(answerCount)
        visitables.addAll(answers)
    }

    fun showEmpty(talkReplyEmptyModel: TalkReplyEmptyModel) {
        visitables.clear()
        visitables.add(talkReplyEmptyModel)
    }

    fun showProductHeader(talkReplyProductHeaderModel: TalkReplyProductHeaderModel) {
        visitables.add(talkReplyProductHeaderModel)
    }

    fun showHeader(talkReplyHeaderModel: TalkReplyHeaderModel) {
        visitables.add(talkReplyHeaderModel)
    }

    fun setIsFollowingButton(isFollowing: Boolean) {
        visitables.forEachIndexed { index, visitable ->

        }
    }
}
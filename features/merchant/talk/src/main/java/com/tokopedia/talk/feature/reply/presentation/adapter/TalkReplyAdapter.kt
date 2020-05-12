package com.tokopedia.talk.feature.reply.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAdapterTypeFactory
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyEmptyModel
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel

class TalkReplyAdapter(talkReplyAdapterTypeFactory: TalkReplyAdapterTypeFactory) : BaseAdapter<TalkReplyAdapterTypeFactory>(talkReplyAdapterTypeFactory) {

    fun displayAnswers(answers: List<TalkReplyUiModel>) {
        visitables.clear()
        visitables.addAll(answers)
    }

    fun showEmpty(talkReplyEmptyModel: TalkReplyEmptyModel) {
        visitables.clear()
        visitables.add(talkReplyEmptyModel)
    }
}
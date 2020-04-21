package com.tokopedia.talk.feature.reply.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.feature.reply.data.model.discussion.Answer
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAdapterTypeFactory

class TalkReplyUiModel(
        val answer: Answer = Answer()
) : Visitable<TalkReplyAdapterTypeFactory> {

    override fun type(typeFactory: TalkReplyAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
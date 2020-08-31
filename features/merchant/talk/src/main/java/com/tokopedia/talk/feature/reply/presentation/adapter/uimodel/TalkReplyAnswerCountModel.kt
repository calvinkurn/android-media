package com.tokopedia.talk.feature.reply.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyTypeFactory

class TalkReplyAnswerCountModel(
        val totalAnswers: Int
) : Visitable<TalkReplyTypeFactory> {

    override fun type(typeFactory: TalkReplyTypeFactory): Int {
        return typeFactory.type(this)
    }
}
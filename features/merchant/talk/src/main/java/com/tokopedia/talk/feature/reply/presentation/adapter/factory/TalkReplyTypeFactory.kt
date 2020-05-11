package com.tokopedia.talk.feature.reply.presentation.adapter.factory

import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyEmptyModel
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel

interface TalkReplyTypeFactory {
    fun type(talkReplyUiModel: TalkReplyUiModel): Int
    fun type(talkReplyEmptyModel: TalkReplyEmptyModel): Int
}
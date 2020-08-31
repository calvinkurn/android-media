package com.tokopedia.talk.feature.reply.presentation.adapter.factory

import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.*

interface TalkReplyTypeFactory {
    fun type(talkReplyUiModel: TalkReplyUiModel): Int
    fun type(talkReplyEmptyModel: TalkReplyEmptyModel): Int
    fun type(talkReplyAnswerCountModel: TalkReplyAnswerCountModel): Int
    fun type(talkReplyHeaderModel: TalkReplyHeaderModel): Int
    fun type(talkReplyProductHeaderModel: TalkReplyProductHeaderModel): Int
}
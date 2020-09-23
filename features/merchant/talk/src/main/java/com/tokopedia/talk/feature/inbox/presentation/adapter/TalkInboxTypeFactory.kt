package com.tokopedia.talk.feature.inbox.presentation.adapter

import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel

interface TalkInboxTypeFactory {
    fun type(talkInboxUiModel: TalkInboxUiModel): Int
}
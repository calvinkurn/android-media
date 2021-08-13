package com.tokopedia.talk.feature.inbox.presentation.adapter

import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.BaseTalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxOldUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel

interface TalkInboxTypeFactory {
    fun type(baseTalkInboxUiModel: BaseTalkInboxUiModel): Int
}
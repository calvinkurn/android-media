package com.tokopedia.talk.feature.inbox.presentation.listener

import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxOldUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel

interface TalkInboxViewHolderListener {
    fun onInboxItemImpressed(talkId: String, position: Int, isUnread: Boolean)
    fun onInboxItemClicked(talkInboxUiModel: TalkInboxUiModel?, talkInboxOldUiModel: TalkInboxOldUiModel?, position: Int)
}
package com.tokopedia.talk.feature.inbox.presentation.listener

interface TalkInboxViewHolderListener {
    fun onInboxItemImpressed(talkId: String, position: Int, isUnread: Boolean)
}
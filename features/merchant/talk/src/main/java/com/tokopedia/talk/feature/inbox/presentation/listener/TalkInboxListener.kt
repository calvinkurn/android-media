package com.tokopedia.talk.feature.inbox.presentation.listener

interface TalkInboxListener {
    fun updateUnreadCounter(sellerUnread: Long, buyerUnread: Long)
}
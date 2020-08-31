package com.tokopedia.talk.feature.inbox.data

sealed class TalkInboxFilter(open val filterParam: String) {
    companion object {
        const val READ_FILTER = "read"
        const val UNREAD_FILTER = "unread"
    }
    data class TalkInboxReadFilter(override val filterParam: String = READ_FILTER): TalkInboxFilter(filterParam)
    data class TalkInboxUnreadFilter(override val filterParam: String = UNREAD_FILTER): TalkInboxFilter(filterParam)
}
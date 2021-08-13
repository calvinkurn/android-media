package com.tokopedia.talk.feature.inbox.data

sealed class TalkInboxFilter(open val filterParam: String) {
    companion object {
        const val READ_FILTER = "read"
        const val UNREAD_FILTER = "unread"
        const val UNRESPONDED_FILTER = "unresponded"
        const val PROBLEM_FILTER = "problem"
        const val AUTOREPLIED_FILTER = "autoreplied"
    }
    data class TalkInboxReadFilter(override val filterParam: String = READ_FILTER): TalkInboxFilter(filterParam)
    data class TalkInboxUnreadFilter(override val filterParam: String = UNREAD_FILTER): TalkInboxFilter(filterParam)
    data class TalkInboxNoFilter(override val filterParam: String = ""): TalkInboxFilter(filterParam)
    data class TalkInboxUnrespondedFilter(override val filterParam: String = UNRESPONDED_FILTER): TalkInboxFilter(filterParam)
    data class TalkInboxProblemFilter(override val filterParam: String = PROBLEM_FILTER): TalkInboxFilter(filterParam)
    data class TalkInboxAutorepliedFilter(override val filterParam: String = AUTOREPLIED_FILTER): TalkInboxFilter(filterParam)
}
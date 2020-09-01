package com.tokopedia.talk.feature.inbox.data

sealed class TalkInboxViewState<out T : Any> {
    data class Success<out T : Any>(val data: T, val page: Int = 0, val filter: TalkInboxFilter, val hasNext: Boolean): TalkInboxViewState<T>()
    data class Fail<out T : Any>(val throwable: Throwable, val page: Int = 0): TalkInboxViewState<T>()
}
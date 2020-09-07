package com.tokopedia.talk.feature.inbox.data

sealed class TalkInboxViewState<out T : Any> {
    data class Success<out T : Any>(val data: T, val page: Int, val filter: TalkInboxFilter): TalkInboxViewState<T>()
    data class Fail<out T : Any>(val throwable: Throwable, val page: Int): TalkInboxViewState<T>()
    data class Loading<out T: Any>(val page: Int): TalkInboxViewState<T>()
}
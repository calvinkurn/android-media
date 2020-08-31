package com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel

sealed class TalkInboxViewState<out T : Any> {
    data class Success<out T : Any>(val data: T, val page: Int = 0): TalkInboxViewState<T>()
    data class Fail<out T : Any>(val throwable: Throwable, val page: Int = 0): TalkInboxViewState<T>()
}
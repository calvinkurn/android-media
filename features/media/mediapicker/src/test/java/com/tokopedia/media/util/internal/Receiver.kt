package com.tokopedia.media.util.internal

import com.tokopedia.media.util.event.Event

interface Receiver<T> {
    suspend fun cancel()

    fun expectNoEvents()

    fun expectMostRecentItem(): T

    suspend fun awaitEvent(): Event<T>
    suspend fun awaitItem(): T
    suspend fun awaitComplete()
    suspend fun awaitError(): Throwable

    fun ensureAllEventsConsumed()
}

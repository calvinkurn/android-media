package com.tokopedia.media.util.event

sealed class Event<out T> {
    object Complete : Event<Nothing>()
    data class Error(val throwable: Throwable) : Event<Nothing>()
    data class Item<T>(val value: T) : Event<T>()

    val isTerminal: Boolean
        get() = this is Complete || this is Error
}

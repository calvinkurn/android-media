package com.tokopedia.media.util.internal

interface TestChannelDefault<T> : Receiver<T> {
    fun add(item: T)
    fun close(cause: Throwable? = null)
}

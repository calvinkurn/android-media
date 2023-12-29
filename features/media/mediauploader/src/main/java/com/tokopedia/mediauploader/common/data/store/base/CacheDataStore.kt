package com.tokopedia.mediauploader.common.data.store.base

interface CacheDataStore<T> {
    fun read(string: String): T
    fun write(data: T): String

    fun default(cache: T.() -> Unit): T

    suspend fun get(key: String): T?
    suspend fun set(key: String, cache: T.() -> Unit)
}

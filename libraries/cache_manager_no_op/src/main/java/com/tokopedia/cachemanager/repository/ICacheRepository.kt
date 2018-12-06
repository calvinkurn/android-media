package com.tokopedia.cachemanager.repository

interface ICacheRepository {

    fun put(key: String, value: String, cacheDurationInMillis: Long)

    fun get(key: String): String?
}
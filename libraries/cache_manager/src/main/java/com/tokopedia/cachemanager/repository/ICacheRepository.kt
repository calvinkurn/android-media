package com.tokopedia.cachemanager.repository

interface ICacheRepository {

    fun deleteExpiredRecords()

    fun put(key: String, value: String, cacheDurationInMillis: Long)

    fun get(key: String): String?

    fun delete(key: String)

    fun delete()
}

package com.tokopedia.cachemanager.repository

import kotlinx.coroutines.flow.Flow

interface ICacheRepository {

    fun deleteExpiredRecords()

    fun put(key: String, value: String, cacheDurationInMillis: Long)

    fun get(key: String): String?
    fun getFlow(key: String): Flow<String?>

    fun delete(key: String)

    fun delete()
}

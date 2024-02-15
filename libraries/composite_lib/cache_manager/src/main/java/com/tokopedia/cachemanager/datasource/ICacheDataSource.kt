package com.tokopedia.cachemanager.datasource

import kotlinx.coroutines.flow.Flow

interface ICacheDataSource<U : Any> {

    fun put(key: String, value: String, cacheDurationInMillis: Long)

    fun get(key: String): U?
    fun getFlow(key: String): Flow<U?>

    fun deleteExpired()

    fun delete(key: String)

    fun delete()

}

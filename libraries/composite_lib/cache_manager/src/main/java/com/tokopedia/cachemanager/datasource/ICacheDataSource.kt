package com.tokopedia.cachemanager.datasource

interface ICacheDataSource<U : Any> {

    fun put(key: String, value: String, cacheDurationInMillis: Long)

    fun get(key: String): U?

    fun deleteExpired()

    fun delete(key: String)

    fun delete()

}

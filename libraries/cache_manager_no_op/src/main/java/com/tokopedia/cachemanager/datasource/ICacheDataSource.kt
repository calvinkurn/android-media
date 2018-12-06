package com.tokopedia.cachemanager.datasource

interface ICacheDataSource {

    fun put(key: String, value: String, cacheDurationInMillis: Long)

    fun get(key: String): String?

}
package com.tokopedia.cachemanager.datasource

import android.content.Context

abstract class CacheDataSource(val context: Context)
    :ICacheDataSource {

    abstract fun createMap(): HashMap<String, String?>

    val cacheMap: HashMap<String, String?> by lazy {
        createMap()
    }
}
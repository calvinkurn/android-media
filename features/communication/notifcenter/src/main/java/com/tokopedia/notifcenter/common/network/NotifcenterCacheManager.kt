package com.tokopedia.notifcenter.common.network

import java.lang.reflect.Type

interface NotifcenterCacheManager {
    fun saveCache(key: String, obj: Any)
    fun <T> loadCache(key: String, type: Type): T?
}
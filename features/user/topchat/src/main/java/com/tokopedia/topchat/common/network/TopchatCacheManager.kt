package com.tokopedia.topchat.common.network

import java.lang.reflect.Type

interface TopchatCacheManager {
    fun saveCache(key: String, obj: Any)
    fun <T> loadCache(key: String, type: Type): T
}
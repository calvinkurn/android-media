package com.tokopedia.notifcenter.stub.common

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManager
import java.lang.reflect.Type

class NotificationCacheManagerStub : NotifCenterCacheManager {

    private var cacheBooleanResult = false
    private var cacheIntResult = Int.ZERO
    private var cacheObjResult: Any = Any()

    override fun saveCache(key: String, obj: Any) {
        cacheObjResult = obj
    }

    override fun <T> loadCache(key: String, type: Type): T {
        return cacheObjResult as T
    }

    override fun saveCacheInt(key: String, int: Int) {
        cacheIntResult = int
    }

    override fun loadCacheInt(key: String): Int {
        return cacheIntResult
    }

    override fun saveCacheBoolean(key: String, bool: Boolean) {
        cacheBooleanResult = bool
    }

    override fun loadCacheBoolean(key: String): Boolean {
        return cacheBooleanResult
    }
}

package com.tokopedia.notifcenter.stub.common

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManager
import java.lang.reflect.Type

object NotificationCacheManagerStub : NotifCenterCacheManager {

    private var cacheBooleanResult = false
    private var cacheIntResult = Int.ONE
    private var cacheObjMapResult: HashMap<String, Any> = hashMapOf()

    override fun saveCache(key: String, obj: Any) {
        cacheObjMapResult[key] = obj
    }

    override fun <T> loadCache(key: String, type: Type): T {
        return cacheObjMapResult[key] as T
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

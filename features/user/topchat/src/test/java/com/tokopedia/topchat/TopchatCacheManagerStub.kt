package com.tokopedia.topchat

import com.tokopedia.topchat.common.network.TopchatCacheManager
import java.lang.reflect.Type

class TopchatCacheManagerStub(
        var cache: Any?
) : TopchatCacheManager {

    var throwError: Boolean = false
    var isPreviousLoadSuccess: Boolean = true

    override fun saveCache(key: String, obj: Any) {

    }

    override fun <T> loadCache(key: String, type: Type): T {
        if (throwError) {
            throw Exception()
        }
        return cache as T
    }

    override fun saveState(stateCacheKey: String, isSuccess: Boolean) {

    }

    override fun getPreviousState(stateCacheKey: String): Boolean {
        return isPreviousLoadSuccess
    }
}
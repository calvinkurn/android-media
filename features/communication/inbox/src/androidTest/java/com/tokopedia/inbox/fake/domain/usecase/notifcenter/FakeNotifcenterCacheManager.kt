package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import java.lang.reflect.Type
import javax.inject.Inject

class FakeNotifcenterCacheManager @Inject constructor() : NotifcenterCacheManager {

    val cache: MutableMap<String, Any> = mutableMapOf()

    override fun saveCache(key: String, obj: Any) {
        cache[key] = obj
    }

    override fun <T> loadCache(key: String, type: Type): T? {
        if (cache.contains(key)) {
            return cache[key] as T
        }
        return null
    }

    fun clearCache() {
        cache.clear()
    }
}
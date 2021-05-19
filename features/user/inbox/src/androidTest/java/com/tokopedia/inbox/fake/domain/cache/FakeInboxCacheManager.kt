package com.tokopedia.inbox.fake.domain.cache

import com.tokopedia.inbox.domain.cache.InboxCacheManager
import com.tokopedia.inbox.viewmodel.InboxViewModel
import java.lang.reflect.Type

class FakeInboxCacheManager: InboxCacheManager {

    val booleanMap = mutableMapOf(
            InboxViewModel.KEY_ONBOARDING_SELLER to true,
            InboxViewModel.KEY_ONBOARDING_BUYER to true,
    )

    override fun saveCache(key: String, obj: Any) {

    }

    override fun <T> loadCache(key: String, type: Type): T? {
        return null
    }

    override fun saveCacheInt(key: String, int: Int) {

    }

    override fun loadCacheInt(key: String): Int? {
        return null
    }

    override fun saveCacheBoolean(key: String, bool: Boolean) {

    }

    override fun loadCacheBoolean(key: String): Boolean {
        return booleanMap.getOrDefault(key, false)
    }
}
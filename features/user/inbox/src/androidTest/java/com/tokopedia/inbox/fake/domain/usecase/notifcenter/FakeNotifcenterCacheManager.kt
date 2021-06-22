package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import java.lang.reflect.Type

class FakeNotifcenterCacheManager : NotifcenterCacheManager {
    override fun saveCache(key: String, obj: Any) {

    }

    override fun <T> loadCache(key: String, type: Type): T? {
        return null
    }
}
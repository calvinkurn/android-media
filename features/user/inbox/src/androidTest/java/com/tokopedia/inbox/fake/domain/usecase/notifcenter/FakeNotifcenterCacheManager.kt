package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import java.lang.reflect.Type
import javax.inject.Inject

class FakeNotifcenterCacheManager @Inject constructor() : NotifcenterCacheManager {
    override fun saveCache(key: String, obj: Any) {

    }

    override fun <T> loadCache(key: String, type: Type): T? {
        return null
    }
}
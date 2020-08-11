package com.tokopedia.seller.search.common.util

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class GlobalSearchConfig @Inject constructor(
        private val remoteConfig: FirebaseRemoteConfigImpl
) {
    fun isGlobalSearchEnabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_GLOBAL_SEARCH_SELLER, false)
    }
}
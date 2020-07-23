package com.tokopedia.sellerhome.config

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class SellerHomeRemoteConfig @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfigImpl
) {

    fun isImprovementDisabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.SELLER_HOME_IMPROVEMENT_DISABLED, false)
    }

    fun isGlobalSearchEnabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_GLOBAL_SEARCH_SELLER, false)
    }
}
package com.tokopedia.sellerhome.common.config

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.SELLER_HOME_ENABLE_WATCH_APP_CHECKING
import javax.inject.Inject

class SellerHomeRemoteConfig @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfigImpl
) {

    fun isWatchAppCheckingEnabled(): Boolean {
        return remoteConfig.getBoolean(SELLER_HOME_ENABLE_WATCH_APP_CHECKING, false)
    }
}

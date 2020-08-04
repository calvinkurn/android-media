package com.tokopedia.sellerhome.config

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class SellerHomeRemoteConfig @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfigImpl
) {

    fun isGlobalSearchEnabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_GLOBAL_SEARCH_SELLER, false)
    }

    fun isNewSellerHomeDisabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.NEW_SELLER_HOME_DISABLED, false)
    }

    fun isNotificationTrayClear(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.NOTIFICATION_TRAY_CLEAR, false)
    }

}
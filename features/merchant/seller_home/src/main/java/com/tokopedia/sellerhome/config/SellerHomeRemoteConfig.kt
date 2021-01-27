package com.tokopedia.sellerhome.config

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class SellerHomeRemoteConfig @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfigImpl
) {

    fun isNotificationTrayClear(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.NOTIFICATION_TRAY_CLEAR, false)
    }

    fun isSellerHomeDashboardCachingEnabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.SELLER_HOME_DASHBOARD_ENABLE_CACHE, false)
    }
}
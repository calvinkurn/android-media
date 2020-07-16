package com.tokopedia.sellerhome.config

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class SellerHomeRemoteConfig @Inject constructor(
    private val remoteConfig: RemoteConfig
) {

    fun isImprovementDisabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.SELLER_HOME_IMPROVEMENT_DISABLED, false)
    }

    fun isGlobalSearchEnabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_GLOBAL_SEARCH_SELLER, false)
    }

    fun isNotificationTrayClear(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.NOTIFICATION_TRAY_CLEAR, false)
    }

}
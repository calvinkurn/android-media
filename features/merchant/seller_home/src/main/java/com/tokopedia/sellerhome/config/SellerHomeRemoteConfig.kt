package com.tokopedia.sellerhome.config

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class SellerHomeRemoteConfig @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfigImpl
) {

    companion object {
        private const val SELLER_HOME_DASHBOARD_ENABLE_CACHE = "seller_home_dashboard_enable_cache"
        private const val SELLER_HOME_DASHBOARD_ENABLE_NEW_CACHE = "seller_home_dashboard_enable_new_cache"
        private const val SELLER_HOME_DASHBOARD_NEW_LAZY_LOAD = "seller_home_dashboard_new_lazy_load"
        private const val SELLERAPP_OTHER_NEW_ERROR_FLOW = "sellerapp_other_new_error_flow"
    }

    fun isNotificationTrayClear(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.NOTIFICATION_TRAY_CLEAR, false)
    }

    fun isSellerHomeDashboardCachingEnabled(): Boolean {
        return remoteConfig.getBoolean(SELLER_HOME_DASHBOARD_ENABLE_CACHE, false)
    }

    fun isSellerHomeDashboardNewCachingEnabled(): Boolean {
        return remoteConfig.getBoolean(SELLER_HOME_DASHBOARD_ENABLE_NEW_CACHE, false)
    }

    fun isSellerHomeDashboardNewLazyLoad(): Boolean {
        return remoteConfig.getBoolean(SELLER_HOME_DASHBOARD_NEW_LAZY_LOAD, false)
    }

    fun isOtherMenuNewErrorFlow(): Boolean {
        return remoteConfig.getBoolean(SELLERAPP_OTHER_NEW_ERROR_FLOW, true)
    }

}
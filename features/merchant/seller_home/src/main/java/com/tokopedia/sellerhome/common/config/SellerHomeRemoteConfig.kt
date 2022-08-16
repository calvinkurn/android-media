package com.tokopedia.sellerhome.common.config

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import javax.inject.Inject

class SellerHomeRemoteConfig @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfigImpl
) {

    companion object {
        private const val SELLER_HOME_DASHBOARD_ENABLE_CACHE = "seller_home_dashboard_enable_cache"
        private const val SELLER_HOME_DASHBOARD_NEW_LAZY_LOAD = "seller_home_dashboard_new_lazy_load"
    }

    fun isSellerHomeDashboardCachingEnabled(): Boolean {
        return false//remoteConfig.getBoolean(SELLER_HOME_DASHBOARD_ENABLE_CACHE, false)
    }

    fun isSellerHomeDashboardNewLazyLoad(): Boolean {
        return remoteConfig.getBoolean(SELLER_HOME_DASHBOARD_NEW_LAZY_LOAD, false)
    }
}
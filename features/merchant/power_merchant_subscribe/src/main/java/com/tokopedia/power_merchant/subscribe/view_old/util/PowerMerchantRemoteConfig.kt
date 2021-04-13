package com.tokopedia.power_merchant.subscribe.view_old.util

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class PowerMerchantRemoteConfig @Inject constructor(
    private val remoteConfig: RemoteConfig
) {

    fun isFreeShippingEnabled(): Boolean {
        return !remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
    }

    fun isTransitionPeriodEnabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
    }
}
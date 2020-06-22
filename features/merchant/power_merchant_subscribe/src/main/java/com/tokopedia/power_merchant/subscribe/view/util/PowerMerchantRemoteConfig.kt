package com.tokopedia.power_merchant.subscribe.view.util

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class PowerMerchantRemoteConfig @Inject constructor(
    private val remoteConfig: RemoteConfig
) {

    fun isFreeShippingEnabled(): Boolean {
        return !remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, false)
    }

    fun isTransitionPeriodEnabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
    }
}
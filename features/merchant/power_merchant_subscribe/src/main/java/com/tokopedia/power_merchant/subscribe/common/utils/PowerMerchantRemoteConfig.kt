package com.tokopedia.power_merchant.subscribe.common.utils

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class PowerMerchantRemoteConfig @Inject constructor(
    private val remoteConfig: RemoteConfig
) {

    fun isFreeShippingEnabled(): Boolean {
        return !remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
    }
}
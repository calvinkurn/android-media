package com.tokopedia.minicart.common.config

import com.tokopedia.remoteconfig.RemoteConfig
import javax.inject.Inject

class MiniCartRemoteConfig @Inject constructor(
    private val remoteConfig: RemoteConfig
) {

    companion object {
        private const val ENABLE_NEW_MINI_CART = "android_enable_new_mini_cart"
    }

    fun isNewMiniCartEnabled(): Boolean {
        return remoteConfig.getBoolean(ENABLE_NEW_MINI_CART, true)
    }
}
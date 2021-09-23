package com.tokopedia.purchase_platform.common.utils

import android.content.Context
import androidx.annotation.Keep
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object Switch {

    @Keep
    fun isBundleToggleOn(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CART_CHECKOUT_BUNDLING, true)
    }

}
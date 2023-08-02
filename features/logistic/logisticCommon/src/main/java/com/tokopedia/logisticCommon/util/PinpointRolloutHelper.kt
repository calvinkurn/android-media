package com.tokopedia.logisticCommon.util

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object PinpointRolloutHelper {
    private const val PINPOINT_V1_CONFIG_KEY = "android_disable_pinpoint_v3_from_v1"
    private const val PINPOINT_V2_CONFIG_KEY = "android_disable_pinpoint_v3_from_v2"
    fun eligibleForRevamp(context: Context, isGeolocation: Boolean): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val key = if (isGeolocation) PINPOINT_V1_CONFIG_KEY else PINPOINT_V2_CONFIG_KEY
        return !remoteConfig.getBoolean(key)
    }
}

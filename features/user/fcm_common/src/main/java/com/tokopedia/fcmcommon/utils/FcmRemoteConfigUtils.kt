package com.tokopedia.fcmcommon.utils

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class FcmRemoteConfigUtils(val context: Context) {

    private var remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)

    fun getBooleanRemoteConfig(key: String, defaultValue: Boolean): Boolean =
        remoteConfig.getBoolean(key, defaultValue)

    fun getLongRemoteConfig(key: String, defaultValue: Long): Long =
        remoteConfig.getLong(key, defaultValue)
}


package com.tokopedia.affiliate

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class AffiliateRemoteConfigUtils(val context: Context) {

    private var remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)

    fun getBooleanRemoteConfig(key: String, defaultValue: Boolean): Boolean = remoteConfig.getBoolean(key, defaultValue)
}
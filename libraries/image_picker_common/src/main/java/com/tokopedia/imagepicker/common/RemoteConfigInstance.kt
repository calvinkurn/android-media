package com.tokopedia.imagepicker.common

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

object RemoteConfigInstance {
    private var remoteConfig: RemoteConfig? = null

    fun getRemoteConfig(context: Context): RemoteConfig {
        with(remoteConfig) {
            if (this == null) {
                FirebaseRemoteConfigImpl(context.applicationContext).also {
                    remoteConfig = it
                    return it
                }
            } else {
                return this
            }
        }
    }
}
package com.tokopedia.applink

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

internal class FirebaseRemoteConfigInstance {
    companion object {
        var remoteConfig: RemoteConfig? = null
        fun get(context: Context): RemoteConfig {
            val rc = remoteConfig
            return if (rc == null) {
                val newRc = FirebaseRemoteConfigImpl(context)
                remoteConfig = newRc
                newRc
            } else {
                rc
            }
        }
    }
}
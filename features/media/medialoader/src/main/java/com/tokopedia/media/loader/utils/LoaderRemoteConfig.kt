package com.tokopedia.media.loader.utils

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

internal class LoaderRemoteConfig(context: Context) {

    private var remoteConfig: RemoteConfig

    init {
        remoteConfig = FirebaseRemoteConfigImpl(
            context.applicationContext
        )
    }

    fun isNewApiEnabled(): Boolean {
        return remoteConfig.getBoolean(KEY, true)
    }

    companion object {
        private const val KEY = "android_media_loader_new_api"
        private var remoteConfig: LoaderRemoteConfig? = null

        fun build(context: Context): LoaderRemoteConfig {
            return remoteConfig ?: LoaderRemoteConfig(context).also {
                remoteConfig = it
            }
        }
    }
}

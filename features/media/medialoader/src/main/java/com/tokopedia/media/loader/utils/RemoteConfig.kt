package com.tokopedia.media.loader.utils

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object RemoteConfig {

    private const val ENABLE_M3U8_THUMBNAIL_LOADER = "android_enable_m3u8_thumbnail_loader"

    private var glideM3U8ThumbnailLoaderEnabled: Boolean? = null

    fun glideM3U8ThumbnailLoaderEnabled(context: Context): Boolean {
        return glideM3U8ThumbnailLoaderEnabled ?: FirebaseRemoteConfigImpl(context.applicationContext)
            .getBoolean(ENABLE_M3U8_THUMBNAIL_LOADER)
            .also { glideM3U8ThumbnailLoaderEnabled = it }
    }
}

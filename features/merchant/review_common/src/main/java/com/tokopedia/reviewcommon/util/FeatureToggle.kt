package com.tokopedia.reviewcommon.util

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object FeatureToggle {

    private const val M3U8_THUMBNAIL_LOADER = "android_enable_m3u8_thumbnail_loader"

    fun glideM3U8ThumbnailLoaderEnabled(context: Context): Boolean {
        return glideM3U8ThumbnailLoaderEnabled ?: FirebaseRemoteConfigImpl(context.applicationContext)
            .getBoolean(M3U8_THUMBNAIL_LOADER, false).also {
                glideM3U8ThumbnailLoaderEnabled = it
            }
    }
}

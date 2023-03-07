package com.tokopedia.media.loader.utils

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object RemoteConfig {

    private const val AB_MEDIALOADER_KEY = "medialoader_key"
    private const val AB_HIGH_QUALITY_VALUE = "high_quality"
    private const val AB_LOW_QUALITY_VALUE = "low_quality"
    private const val ENABLE_M3U8_THUMBNAIL_LOADER = "android_enable_m3u8_thumbnail_loader"

    private var glideM3U8ThumbnailLoaderEnabled: Boolean? = null

//    fun abUrlBuilder(url: String): GlideUrl {
//        val status = RemoteConfigInstance
//                .getInstance()
//                .abTestPlatform
//                .getString(AB_MEDIALOADER_KEY, "")
//
//        return when (status) {
//            AB_HIGH_QUALITY_VALUE -> Loader.glideUrl(url, HIGH_QUALITY_SETTINGS)
//            AB_LOW_QUALITY_VALUE -> Loader.glideUrl(url, LOW_QUALITY_SETTINGS)
//            else -> Loader.glideUrl(url)
//        }
//    }

    fun glideM3U8ThumbnailLoaderEnabled(context: Context): Boolean {
        return glideM3U8ThumbnailLoaderEnabled ?: FirebaseRemoteConfigImpl(context.applicationContext)
            .getBoolean(ENABLE_M3U8_THUMBNAIL_LOADER)
            .also { glideM3U8ThumbnailLoaderEnabled = it }
    }

}

package com.tokopedia.media.loader.utils

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance

object FeatureToggle {

    private const val M3U8_THUMBNAIL_LOADER = "android_enable_m3u8_thumbnail_loader"
    private const val EXPOSE_RESPONSE_HEADER = "android_media_loader_expose_header"
    private const val WEBP_SUPPORT = "android_webp"

    private var glideM3U8ThumbnailLoaderEnabled: Boolean? = null
    private var isExposeResponseHeaderEnabled: Boolean? = null
    private var isWebpFormatDelivered: Boolean? = null

    fun glideM3U8ThumbnailLoaderEnabled(context: Context): Boolean {
        return glideM3U8ThumbnailLoaderEnabled ?: getRemoteConfigBoolean(
            context,
            M3U8_THUMBNAIL_LOADER
        ).also { glideM3U8ThumbnailLoaderEnabled = it }
    }

    fun shouldAbleToExposeResponseHeader(context: Context): Boolean {
        return isExposeResponseHeaderEnabled ?: getRemoteConfigBoolean(
            context,
            EXPOSE_RESPONSE_HEADER
        ).also { isExposeResponseHeaderEnabled = it }
    }

    fun isWebpFormatEnabled(): Boolean {
        return isWebpFormatDelivered ?: getAbTestBoolean(WEBP_SUPPORT)
            .also { isWebpFormatDelivered = it }
    }

    private fun getRemoteConfigBoolean(context: Context, key: String): Boolean {
        return FirebaseRemoteConfigImpl(context.applicationContext)
            .getBoolean(key, false)
    }

    private fun getAbTestBoolean(key: String): Boolean {
        return RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(key)
            .isNotEmpty()
    }
}

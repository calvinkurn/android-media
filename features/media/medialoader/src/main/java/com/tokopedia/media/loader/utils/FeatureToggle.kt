package com.tokopedia.media.loader.utils

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

interface FeatureToggle {

    fun glideM3U8ThumbnailLoaderEnabled(context: Context): Boolean
    fun shouldAbleToExposeResponseHeader(context: Context): Boolean
}

class FeatureToggleManager : FeatureToggle {

    private var glideM3U8ThumbnailLoaderEnabled: Boolean? = null
    private var isExposeResponseHeaderEnabled: Boolean? = null

    override fun glideM3U8ThumbnailLoaderEnabled(context: Context): Boolean {
        return glideM3U8ThumbnailLoaderEnabled ?: getRemoteConfigBoolean(
            context,
            M3U8_THUMBNAIL_LOADER
        ).also { glideM3U8ThumbnailLoaderEnabled = it }
    }

    override fun shouldAbleToExposeResponseHeader(context: Context): Boolean {
        return isExposeResponseHeaderEnabled ?: getRemoteConfigBoolean(
            context,
            EXPOSE_RESPONSE_HEADER
        ).also { isExposeResponseHeaderEnabled = it }
    }

    private fun getRemoteConfigBoolean(context: Context, key: String): Boolean {
        return FirebaseRemoteConfigImpl(context.applicationContext)
            .getBoolean(key, false)
    }

    companion object {
        private const val M3U8_THUMBNAIL_LOADER = "android_enable_m3u8_thumbnail_loader"
        private const val EXPOSE_RESPONSE_HEADER = "android_media_loader_expose_header"

        @Volatile private var toggle: FeatureToggle? = null

        fun instance(): FeatureToggle {
            return synchronized(this) {
                toggle ?: FeatureToggleManager().also {
                    toggle = it
                }
            }
        }
    }
}

package com.tokopedia.media.loader.utils

import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.media.common.Loader
import com.tokopedia.media.common.data.HIGH_QUALITY_SETTINGS
import com.tokopedia.media.common.data.LOW_QUALITY_SETTINGS
import com.tokopedia.remoteconfig.RemoteConfigInstance

object RemoteConfig {

    private const val AB_MEDIALOADER_KEY = "medialoader_key"
    private const val AB_HIGH_QUALITY_VALUE = "high_quality"
    private const val AB_LOW_QUALITY_VALUE = "low_quality"

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

}
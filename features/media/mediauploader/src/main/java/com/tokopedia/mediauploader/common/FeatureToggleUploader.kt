package com.tokopedia.mediauploader.common

import com.tokopedia.remoteconfig.RemoteConfigInstance

object FeatureToggleUploader {

    private const val VOD_COMPRESSION_KEY = "android_vodcompress"

    fun isCompressionEnabled(): Boolean {
        return RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(VOD_COMPRESSION_KEY) == VOD_COMPRESSION_KEY
    }
}

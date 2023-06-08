package com.tokopedia.mediauploader.common

import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance

object FeatureToggleUploader {

    private const val VOD_COMPRESSION_KEY = "android_vodcompress"

    fun isCompressionEnabled(): Boolean {
        if (GlobalConfig.isAllowDebuggingTools()) return true // TODO(isfa): for testing

        return RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(VOD_COMPRESSION_KEY, "")
            .isNotEmpty()
    }
}

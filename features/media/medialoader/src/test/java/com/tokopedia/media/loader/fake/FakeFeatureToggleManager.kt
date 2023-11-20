package com.tokopedia.media.loader.fake

import android.content.Context
import com.tokopedia.media.loader.utils.FeatureToggle

class FakeFeatureToggleManager : FeatureToggle {

    var isFakeWebpFormatEnabled = true

    override fun glideM3U8ThumbnailLoaderEnabled(context: Context): Boolean {
        return true
    }

    override fun shouldAbleToExposeResponseHeader(context: Context): Boolean {
        return true
    }

    override fun isWebpFormatEnabled(): Boolean {
        return isFakeWebpFormatEnabled
    }
}

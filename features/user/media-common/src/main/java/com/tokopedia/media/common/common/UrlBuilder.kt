package com.tokopedia.media.common.common

import com.tokopedia.media.common.data.*

object UrlBuilder {

    fun urlBuilder(
            networkState: String,
            qualitySettings: Int,
            url: String?
    ): String {
        val connectionType = when(qualitySettings) {
            LOW_QUALITY_SETTINGS -> LOW_QUALITY // (2g / 3g)
            HIGH_QUALITY_SETTINGS -> HIGH_QUALITY // (4g / wifi)
            else -> networkState
        }
        return "$url?$HEADER_ECT=$connectionType"
    }

}
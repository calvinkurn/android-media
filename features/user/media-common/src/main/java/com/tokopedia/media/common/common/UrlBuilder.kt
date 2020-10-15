package com.tokopedia.media.common.common

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.media.common.data.HEADER_ECT
import com.tokopedia.media.common.data.HIGH_QUALITY
import com.tokopedia.media.common.data.LOW_QUALITY
import com.tokopedia.media.common.data.MediaSettingPreferences

object UrlBuilder {

    fun urlBuilder(
            networkState: String,
            settings: MediaSettingPreferences,
            url: String?
    ): GlideUrl {
        val connectionType = when(settings.qualitySettings()) {
            1 -> LOW_QUALITY // (2g / 3g)
            2 -> HIGH_QUALITY // (4g / wifi)
            else -> networkState
        }

        return GlideUrl(url, LazyHeaders.Builder()
                .addHeader(HEADER_ECT, connectionType)
                .build()
        )
    }

}
package com.tokopedia.media.loader.common

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.media.loader.utils.HEADER_ECT
import com.tokopedia.media.loader.utils.HIGH_QUALITY
import com.tokopedia.media.loader.utils.LOW_QUALITY
import com.tokopedia.media.loader.utils.MediaSettingPreferences

object UrlBuilder {

    fun urlBuilder(networkState: String, settings: MediaSettingPreferences, url: String?): GlideUrl {
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
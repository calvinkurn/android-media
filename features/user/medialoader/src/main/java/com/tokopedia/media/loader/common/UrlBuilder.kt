package com.tokopedia.media.loader.common

import android.content.Context
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.media.loader.utils.MediaSettingPreferences
import com.tokopedia.media.loader.network.NetworkManager
import com.tokopedia.media.loader.utils.HEADER_ECT
import com.tokopedia.media.loader.utils.HIGH_QUALITY
import com.tokopedia.media.loader.utils.LOW_QUALITY

object UrlBuilder {

    fun urlBuilder(context: Context, url: String?): GlideUrl {
        // adaptive network state
        val networkState by lazy { NetworkManager.state(context) }

        // get configuration quality setting state
        val settings by lazy { MediaSettingPreferences(context) }

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
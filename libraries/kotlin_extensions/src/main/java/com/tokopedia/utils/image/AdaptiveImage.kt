package com.tokopedia.utils.image

import android.content.Context
import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.utils.network.NetworkManager

object AdaptiveImage {

    fun glideUrl(context: Context, url: String): GlideUrl {
        // adaptive network state
        val networkState = NetworkManager.state(context)

        // get configuration image quality setting
        val settings = MediaQualityCacheManager(context)

        val connectionType = when(settings.indexQuality()) {
            0 -> networkState
            1 -> "2g"
            2 -> "4g"
            else -> networkState
        }

        return GlideUrl(url) {
            mapOf(Pair("ECT", connectionType))
        }
    }

}
package com.tokopedia.media.loader.common

import android.content.Context
import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.media.loader.network.NetworkManager
import com.tokopedia.media.loader.utils.MediaSettingPreferences

object Loader {

    private lateinit var context: Context
    private lateinit var settings: MediaSettingPreferences

    @JvmStatic
    fun initialize(context: Context) {
        Loader.context = context
        settings = MediaSettingPreferences(context)
    }

    fun glideUrl(url: String?): GlideUrl {
        val networkState = NetworkManager.state(context)
        return UrlBuilder.urlBuilder(networkState, settings, url)
    }

}
package com.tokopedia.media.common

import android.content.Context
import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.media.common.common.UrlBuilder
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.media.common.util.NetworkManager

object Loader {

    private lateinit var settings: MediaSettingPreferences
    private lateinit var context: Context

    @JvmStatic
    fun initialize(context: Context) {
        settings = MediaSettingPreferences(context)
        this.context = context
    }

    fun glideUrl(url: String?): GlideUrl {
        val networkState = NetworkManager.state(context)
        return UrlBuilder.urlBuilder(networkState, settings, url)
    }

}
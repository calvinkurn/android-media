package com.tokopedia.media.common

import android.content.Context
import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.media.common.common.UrlBuilder.urlBuilder
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.media.common.util.NetworkManager.state as networkManagerState

object Loader {

    private lateinit var context: Context

    private val settings by lazy {
        MediaSettingPreferences(context)
    }

    @JvmStatic
    fun initialize(context: Context) {
        this.context = context
    }

    fun glideUrl(url: String?): GlideUrl {
        val networkState = networkManagerState(context)
        return urlBuilder(networkState, settings, url)
    }

}
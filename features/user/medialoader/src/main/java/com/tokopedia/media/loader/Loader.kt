package com.tokopedia.media.loader

import android.content.Context
import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.media.loader.common.UrlBuilder
import com.tokopedia.media.loader.network.NetworkManager
import com.tokopedia.media.loader.utils.MediaSettingPreferences

object Loader {

    private lateinit var context: Context
    private val networkState by lazy { NetworkManager.state(context) }
    private val settings by lazy { MediaSettingPreferences(context) }

    @JvmStatic
    fun initialize(context: Context) {
        this.context = context
    }

    fun glideUrl(url: String?): GlideUrl {
        return UrlBuilder.urlBuilder(networkState, settings, url)
    }

}
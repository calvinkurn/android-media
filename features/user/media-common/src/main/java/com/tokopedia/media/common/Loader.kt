package com.tokopedia.media.common

import android.content.Context
import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.media.common.common.UrlBuilder.urlBuilder
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.media.common.util.NetworkManager.state as networkManagerState

object Loader {

    private const val KEY_ADAPTIVE_IMAGE = "is_adaptive_image_status"

    private lateinit var context: Context
    private lateinit var remoteConfig: RemoteConfig

    private val settings by lazy {
        MediaSettingPreferences(context)
    }

    @JvmStatic
    fun initialize(context: Context) {
        this.context = context
        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    fun glideUrl(url: String): String {
        val networkState = networkManagerState(context)
        return if (remoteConfig.getBoolean(KEY_ADAPTIVE_IMAGE)) {
            urlBuilder(networkState, settings.qualitySettings(), url)
        } else url
    }

}
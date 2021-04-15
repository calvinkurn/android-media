package com.tokopedia.media.common

import android.app.Application
import android.content.Context
import com.tokopedia.media.common.common.UrlBuilder.urlBuilder
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.media.common.util.NetworkManager.state as networkManagerState

class Loader {

    private var context: Context? = null

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(context) }
    private val settings by lazy { MediaSettingPreferences(context) }

    fun init(application: Application) {
        this.context = application.applicationContext
    }

    fun urlBuilder(url: String): String {
        if (context == null) return url

        val networkState = networkManagerState(context)
        return if (remoteConfig.getBoolean(KEY_ADAPTIVE_IMAGE)) {
            urlBuilder(networkState, settings.qualitySettings(), url)
        } else url
    }

    companion object {
        private const val KEY_ADAPTIVE_IMAGE = "is_adaptive_image_status"
        @JvmStatic val instance = Loader()
    }

}
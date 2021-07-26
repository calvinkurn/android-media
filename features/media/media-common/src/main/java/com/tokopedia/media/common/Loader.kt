package com.tokopedia.media.common

import android.app.Application
import android.content.Context
import com.tokopedia.media.common.common.UrlBuilder.urlBuilder
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.media.common.util.NetworkManager.state as networkManagerState

object Loader {

    private var context: Context? = null

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(context) }
    private val settings by lazy { MediaSettingPreferences(context) }

    // reducing fetch time of remote config
    private var isAdaptiveImage: Boolean = false

    private const val KEY_ADAPTIVE_IMAGE = "is_adaptive_image_status"

    @JvmStatic
    fun init(application: Application) {
        this.context = application.applicationContext

        isAdaptiveImage = remoteConfig.getBoolean(KEY_ADAPTIVE_IMAGE)
    }

    fun urlBuilder(url: String): String {
        if (context == null) return url

        val networkState = networkManagerState(context)
        return if (isAdaptiveImage) {
            urlBuilder(networkState, settings.qualitySettings(), url)
        } else url
    }

}
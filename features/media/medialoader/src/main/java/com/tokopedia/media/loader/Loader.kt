package com.tokopedia.media.loader

import android.app.Application
import android.content.Context
import com.tokopedia.media.loader.data.*
import com.tokopedia.media.loader.internal.MediaSettingPreferences
import com.tokopedia.media.loader.internal.NetworkManager
import com.tokopedia.media.loader.utils.isValidUrl
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

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

    fun get(url: String): String {
        if (context == null) return url

        val networkState = NetworkManager.state(context)

        if (isAdaptiveImage && url.isValidUrl()) {
            return getOrSetEctParam(networkState, settings.qualitySettings(), url)
        }

        return url
    }

    fun getOrSetEctParam(
        networkState: String,
        qualitySettings: Int,
        url: String
    ): String {
        val connectionType = when(qualitySettings) {
            LOW_QUALITY_SETTINGS -> LOW_QUALITY // (2g / 3g)
            HIGH_QUALITY_SETTINGS -> HIGH_QUALITY // (4g / wifi)
            else -> networkState // adaptive
        }

        if (connectionType != LOW_QUALITY) return url
        return url.addEctParam(connectionType)
    }

    /**
     * addEctParam()
     * it will add the query parameter of ECT to adopt a adaptive images,
     * if the URL has query parameters, it will append a new string with &ect=connType,
     * but if the URL haven't query parameters yet, it will append with ?ect=connType
     * @param connType (connection type)
     */
    private fun String.addEctParam(connType: String): String {
        return if (hasParam(this)) "$this&$PARAM_ECT=$connType" else "$this?$PARAM_ECT=$connType"
    }

    private fun hasParam(url: String): Boolean {
        return url.contains("?")
    }

}

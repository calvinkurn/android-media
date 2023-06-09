package com.tokopedia.media.loader.v1.util

import android.content.Context
import com.tokopedia.media.loader.data.*
import com.tokopedia.media.loader.internal.MediaSettingPreferences
import com.tokopedia.media.loader.internal.NetworkManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import java.lang.ref.WeakReference

internal class UrlBuilder constructor(
    private val context: Context
) {

    private var remoteConfig: RemoteConfig? = null
    private var preferences: MediaSettingPreferences? = null

    private var isAdaptive = false

    init {
        if (remoteConfig == null) remoteConfig = FirebaseRemoteConfigImpl(context)
        if (preferences == null) preferences = MediaSettingPreferences(context)

        isAdaptive = remoteConfig?.getBoolean(KEY_ADAPTIVE_IMAGE) ?: false
    }

    fun buildUrl(url: String): String {
        val setting = preferences?.qualitySettings() ?: 0

        if (isAdaptiveImageSupported(url).not()) {
            return url
        }

        val connectionType = when (setting) {
            LOW_QUALITY_SETTINGS -> LOW_QUALITY // (2g / 3g)
            HIGH_QUALITY_SETTINGS -> HIGH_QUALITY // (4g / wifi)
            else -> NetworkManager.state(context) // adaptive
        }

        /*
        * by default, our CDN returns a high quality,
        * if the connection type is not low quality,
        * then don't add ECT param in the url and
        * return it as is.
        * */
        if (connectionType != LOW_QUALITY) return url

        return url.addEctParam(connectionType)
    }

    private fun isAdaptiveImageSupported(model: String): Boolean {
        return model.startsWith("https://images.tokopedia.net/") && isAdaptive
    }

    /**
     * addEctParam()
     * it will add the query parameter of ECT to adopt a adaptive images,
     * if the URL has query parameters, it will append a new string with &ect=connType,
     * but if the URL haven't query parameters yet, it will append with ?ect=connType
     * @param connType (connection type)
     */
    private fun String.addEctParam(connType: String): String {
        return if (hasParam(this)) {
            "$this&$PARAM_ECT=$connType"
        } else {
            "$this?$PARAM_ECT=$connType"
        }
    }

    private fun hasParam(url: String): Boolean {
        return url.contains("?")
    }

    companion object {
        private const val KEY_ADAPTIVE_IMAGE = "is_adaptive_image_status"

        private var urlBuilder: WeakReference<UrlBuilder>? = null

        fun build(context: Context): UrlBuilder {
            return urlBuilder?.get() ?: UrlBuilder(context).also {
                urlBuilder = WeakReference(it)
            }
        }
    }
}

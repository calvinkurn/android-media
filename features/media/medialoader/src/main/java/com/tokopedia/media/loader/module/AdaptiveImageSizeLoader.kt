package com.tokopedia.media.loader.module

import android.content.Context
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.tokopedia.media.loader.data.*
import com.tokopedia.media.loader.internal.MediaSettingPreferences
import com.tokopedia.media.loader.internal.NetworkManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import timber.log.Timber
import java.io.InputStream

class AdaptiveImageSizeLoader constructor(
    private val context: Context,
    loader: ModelLoader<GlideUrl, InputStream>,
    cache: ModelCache<String, GlideUrl?>
) : BaseGlideUrlLoader<String>(loader, cache) {

    private var remoteConfig: RemoteConfig? = null
    private var preferences: MediaSettingPreferences? = null

    private var isAdaptive = false

    init {
        if (remoteConfig == null) remoteConfig = FirebaseRemoteConfigImpl(context)
        if (preferences == null) preferences = MediaSettingPreferences(context)

        isAdaptive = remoteConfig?.getBoolean(KEY_ADAPTIVE_IMAGE)?: false
    }

    override fun handles(model: String): Boolean {
        Timber.d("medialoader: [Url-handles] ${model.startsWith("https://images.tokopedia.net/") && isAdaptive}")
        return model.startsWith("https://images.tokopedia.net/") && isAdaptive
    }

    override fun getUrl(model: String, width: Int, height: Int, options: Options?): String {
        val setting = preferences?.qualitySettings()?: 0

        val url = getOrSetEctParam(
            qualitySettings = setting,
            url = model
        )

        Timber.d("medialoader: [Url] $url")

        return url
    }

    companion object {
        private const val KEY_ADAPTIVE_IMAGE = "is_adaptive_image_status"
    }

    private fun getOrSetEctParam(
        qualitySettings: Int,
        url: String
    ): String {
        val connectionType = when(qualitySettings) {
            LOW_QUALITY_SETTINGS -> LOW_QUALITY // (2g / 3g)
            HIGH_QUALITY_SETTINGS -> HIGH_QUALITY // (4g / wifi)
            else -> NetworkManager.state(context) // adaptive
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

package com.tokopedia.media.loader.module.model

import android.content.Context
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.tokopedia.media.loader.data.DEBUG_TIMBER_TAG
import com.tokopedia.media.loader.data.LOW_QUALITY_SETTINGS
import com.tokopedia.media.loader.data.LOW_QUALITY
import com.tokopedia.media.loader.data.HIGH_QUALITY_SETTINGS
import com.tokopedia.media.loader.data.HIGH_QUALITY
import com.tokopedia.media.loader.data.PARAM_ECT
import com.tokopedia.media.loader.internal.MediaSettingPreferences
import com.tokopedia.media.loader.internal.NetworkManager
import timber.log.Timber
import java.io.InputStream

internal class AdaptiveImageSizeLoader constructor(
    private val context: Context,
    loader: ModelLoader<GlideUrl, InputStream>,
    cache: ModelCache<String, GlideUrl?>
) : BaseGlideUrlLoader<String>(loader, cache) {

    private var preferences: MediaSettingPreferences? = null

    init {
        if (preferences == null) {
            preferences = MediaSettingPreferences(context)
        }
    }

    override fun handles(model: String) = true

    override fun getUrl(model: String, width: Int, height: Int, options: Options?): String {
        val setting = preferences?.qualitySettings()?: 0
        val url = buildUrl(setting, model)

        return url.also {
            Timber.d("$DEBUG_TIMBER_TAG: [Url] $it")
        }
    }

    private fun isAdaptiveImageSupported(model: String): Boolean {
        return model.startsWith("https://images.tokopedia.net/")
    }

    private fun buildUrl(qualitySettings: Int, url: String): String {
        if (isAdaptiveImageSupported(url).not()) return url

        val connectionType = when(qualitySettings) {
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

    class Factory(private val context: Context) : ModelLoaderFactory<String, InputStream> {

        private val modelCache = ModelCache<String, GlideUrl?>(500)

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
            val loader = multiFactory.build(
                GlideUrl::class.java,
                InputStream::class.java
            )

            return AdaptiveImageSizeLoader(context, loader, modelCache)
        }

        override fun teardown() = Unit
    }
}

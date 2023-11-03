package com.tokopedia.media.loader.module

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.tokopedia.media.loader.module.model.AdaptiveImageSizeLoader
import com.tokopedia.media.loader.module.model.M3U8ModelLoader
import com.tokopedia.media.loader.module.model.OkHttpModelLoader
import com.tokopedia.media.loader.utils.FeatureToggleManager
import java.io.InputStream

@GlideModule
/** @suppress */
class LoaderGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(
            DiskLruCacheFactory(
                Glide.getPhotoCacheDir(context)?.absolutePath,
                (LIMIT_CACHE_SIZE_IN_MB * SIZE_IN_MB * SIZE_IN_MB).toLong()
            )
        )

        super.applyOptions(context, builder)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)

        /**
         * A overridden of OkHttp client.
         *
         * This replacement due we need to track every single network request occurred, hence
         * we are able to send it as log into newrelic.
         *
         * This method is hansel-able, simply switch the toggle of [hasNetworkClientOverridden].
         */
        if (hasNetworkClientOverridden()) {
            registry.replace(
                GlideUrl::class.java,
                InputStream::class.java,
                OkHttpModelLoader.Factory(context)
            )
        }

        // dynamic image loader (based on network connection)
        registry.append(
            String::class.java,
            InputStream::class.java,
            AdaptiveImageSizeLoader.Factory(context)
        )

        // m3u8 video preview
        if (FeatureToggleManager.instance().glideM3U8ThumbnailLoaderEnabled(context)) {
            registry.prepend(String::class.java, Bitmap::class.java, M3U8ModelLoader.Factory())
        }
    }

    override fun isManifestParsingEnabled(): Boolean {
        /*
        * improve the initial startup time of Glide and
        * avoid some potential problems with trying to parse metadata.
        * */
        return false
    }

    private fun hasNetworkClientOverridden(): Boolean {
        return true
    }

    companion object {
        private const val LIMIT_CACHE_SIZE_IN_MB = 50
        private const val SIZE_IN_MB = 1024
    }
}

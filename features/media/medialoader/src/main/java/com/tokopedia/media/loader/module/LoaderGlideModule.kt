package com.tokopedia.media.loader.module

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.module.interceptor.NetworkLogInterceptor
import com.tokopedia.media.loader.module.model.AdaptiveImageSizeLoader
import com.tokopedia.media.loader.module.model.M3U8ModelLoaderFactory
import com.tokopedia.media.loader.utils.FeatureToggle
import okhttp3.OkHttpClient
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

        // custom network interceptor
        if (hasNetworkClientOverridden()) {
            val client = OkHttpClient.Builder()

            if (FeatureToggle.shouldAbleToExposeResponseHeader(context)) {
                client.addInterceptor(NetworkLogInterceptor(context))
            }

            if (GlobalConfig.isAllowDebuggingTools()) {
                client.addInterceptor(
                    ChuckerInterceptor(
                        context = context,
                        maxContentLength = 5000
                    )
                )
            }

            val okHttpLoaderFactory = OkHttpUrlLoader.Factory(client.build())
            registry.replace(GlideUrl::class.java, InputStream::class.java, okHttpLoaderFactory)
        }

        // dynamic image loader (based on network connection)
        registry.append(
            String::class.java,
            InputStream::class.java,
            AdaptiveImageSizeLoader.Factory(context)
        )

        // m3u8 video preview
        if (FeatureToggle.glideM3U8ThumbnailLoaderEnabled(context)) {
            registry.prepend(String::class.java, Bitmap::class.java, M3U8ModelLoaderFactory())
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

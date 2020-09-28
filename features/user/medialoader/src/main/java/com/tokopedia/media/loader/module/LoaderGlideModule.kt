package com.tokopedia.media.loader.module

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.tokopedia.media.loader.BuildConfig
import com.tokopedia.media.loader.utils.GlideNetworkInterceptor.okHttpInterceptor
import java.io.InputStream

@GlideModule
class LoaderGlideModule: AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        // optimize image loader on glide
        builder.setDefaultRequestOptions(
                RequestOptions().format(DecodeFormat.PREFER_RGB_565)
        )

        // showing glide log in debug app
        if (BuildConfig.DEBUG) {
            builder.setLogLevel(Log.VERBOSE)
        }
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.replace(
                GlideUrl::class.java,
                InputStream::class.java,
                OkHttpUrlLoader.Factory(okHttpInterceptor())
        )
    }

}
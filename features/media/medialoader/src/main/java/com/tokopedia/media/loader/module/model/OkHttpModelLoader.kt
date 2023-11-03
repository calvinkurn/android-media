package com.tokopedia.media.loader.module.model

import android.content.Context
import com.bumptech.glide.Priority
import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.tokopedia.media.loader.internal.NetworkResponseManager
import com.tokopedia.media.loader.tracker.RequestLogger
import com.tokopedia.media.loader.utils.FeatureToggleManager
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.InputStream

class OkHttpModelLoader constructor(
    private val context: Context,
    private val client: Call.Factory
) : ModelLoader<GlideUrl, InputStream> {

    override fun buildLoadData(model: GlideUrl, width: Int, height: Int, options: Options) =
        ModelLoader.LoadData(model, CustomOkHttpStreamFetcher(context, client, model))

    override fun handles(model: GlideUrl): Boolean {
        return true
    }

    class Factory(private val context: Context) : ModelLoaderFactory<GlideUrl, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return OkHttpModelLoader(context, instanceClient())
        }

        override fun teardown() {
            // no-op
        }

        companion object {
            @Volatile var client: Call.Factory? = null

            fun instanceClient(): Call.Factory {
                return synchronized(Factory::class) {
                    client ?: OkHttpClient().also {
                        client = it
                    }
                }
            }
        }
    }
}

internal class CustomOkHttpStreamFetcher constructor(
    private val context: Context,
    client: Call.Factory,
    url: GlideUrl
) : OkHttpStreamFetcher(client, url) {

    private var startTime: Long = 0L

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        super.loadData(priority, callback)
        startTime = System.currentTimeMillis()
    }

    override fun onResponse(call: Call, response: Response) {
        super.onResponse(call, response)

        if (response.isSuccessful) {
            val endTime = System.currentTimeMillis()
            val mContentType = response.body?.contentType()

            // expose headers for MediaListenerBuilder.
            if (FeatureToggleManager.instance().shouldAbleToExposeResponseHeader(context)) {
                NetworkResponseManager.set(
                    response.request.url.toString(),
                    response.headers
                )
            }

            RequestLogger.request(context) {
                url = response.request.url.toString()
                requestLoadTime = (endTime - startTime).toString()
                fileSize = response.body?.contentLength()?.toString() ?: "0"
                contentType = "${mContentType?.type}/${mContentType?.subtype}"
            }
        }
    }
}

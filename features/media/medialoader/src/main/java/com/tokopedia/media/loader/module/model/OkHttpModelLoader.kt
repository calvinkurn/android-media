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
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader
import com.tokopedia.media.loader.data.DEFAULT_TIMEOUT
import com.tokopedia.media.loader.internal.NetworkResponseManager
import com.tokopedia.media.loader.tracker.RequestLogger
import com.tokopedia.media.loader.utils.FeatureToggleManager
import com.tokopedia.media.loader.utils.OkHttpClientManager
import okhttp3.Call
import okhttp3.Response
import java.io.InputStream

class OkHttpModelLoader (
    private val context: Context
) : ModelLoader<GlideUrl, InputStream> {

    override fun buildLoadData(model: GlideUrl, width: Int, height: Int, options: Options): ModelLoader.LoadData<InputStream> {
        val timeoutMs = options.get(HttpGlideUrlLoader.TIMEOUT)
        val client = OkHttpClientManager.getClient(timeoutMs ?: DEFAULT_TIMEOUT)
        return ModelLoader.LoadData(model, CustomOkHttpStreamFetcher(context, client, model))
    }

    override fun handles(model: GlideUrl): Boolean {
        return true
    }

    class Factory constructor(
        private val context: Context
    ) : ModelLoaderFactory<GlideUrl, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return OkHttpModelLoader(context)
        }

        override fun teardown() = Unit
    }
}

internal class CustomOkHttpStreamFetcher (
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
                NetworkResponseManager.instance(context).set(
                    response.request.url.toString(),
                    response.headers
                )
            }

            // logger
            RequestLogger.request(context) {
                url = response.request.url.toString()
                requestLoadTime = (endTime - startTime).toString()
                fileSize = response.body?.contentLength()?.toString() ?: "0"
                contentType = "${mContentType?.type}/${mContentType?.subtype}"
            }
        }
    }
}

package com.tokopedia.media.loader.common.factory

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.data.PARAM_BLURHASH
import com.tokopedia.media.common.data.toUri
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.common.MediaLoaderFactory
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.listener.MediaListenerBuilder.callback as callbackListener
import com.tokopedia.media.loader.transform.BlurHashDecoder.decode as blurHashDecode

class BitmapFactory : MediaLoaderFactory<Bitmap>() {

    /*
    * The blurhash (built-in) list,
    * it will be use as default if the image didn't have the hash
    * in the URL. the blurhashes will randomly rendering */
    private val blurHashes = listOf(
            "A4ADcRuO_2y?",
            "A9K{0B#R3WyY",
            "AHHUnD~V^ia~",
            "A2N+X[~qv]IU",
            "ABP?2U~X5J^~"
    )

    fun build(
            context: Context,
            properties: Properties,
            performanceMonitoring: PerformanceMonitoring? = null,
            request: GlideRequest<Bitmap>
    ) = setup(properties, request).apply {
        // startTimeRequest will use for performance tracking
        val startTimeRequest = System.currentTimeMillis()

        /*
        * because the medialoader placeholder has a different behavior,
        * a builder is needed to handle it. the blurhash only work for URL
        * */
        blurHashPlaceHolder(context, properties, this)

        with(properties) {
            if (thumbnailUrl.isNotEmpty()) {
                thumbnail(loader(context, thumbnailUrl))
            }

            listener(callbackListener(
                    context,
                    properties,
                    startTimeRequest,
                    loaderListener,
                    performanceMonitoring
            ))
        }
    }

    private fun blurHashPlaceHolder(
            context: Context,
            properties: Properties,
            request: GlideRequest<Bitmap>
    ): GlideRequest<Bitmap> {
        val placeHolder = properties.placeHolder
        val blurHash = properties.blurHash

        /*
        * get the hash of image blur (placeholder) from the URL, example:
        * https://images.tokopedia.net/samples.png?b=abc123
        * the hash of blur is abc123
        * */
        val hash = properties.urlHasQualityParam.toUri()
                ?.getQueryParameter(PARAM_BLURHASH)
                ?: blurHashes.random()

        return request.apply {
            when {
                /*
                * validate if the placeholder have default placeholder value, 0.
                * it will check if the blurHash is active, the placeholder will render
                * the blurHash image, but if placeholder is 0 and blurHash is inactive,
                * the placeholder will render the default of built-in placeholder.
                * */
                placeHolder == ZERO_PLACEHOLDER -> {
                    if (blurHash && hash.isNotEmpty()) {
                        placeholder(BitmapDrawable(context.resources, generateBlurHash(hash)))
                    } else {
                        placeholder(R.drawable.media_state_default_placeholder)
                    }
                }

                // render the custom placeholder that provided by Properties()
                placeHolder != ZERO_PLACEHOLDER && placeHolder > ZERO_PLACEHOLDER -> {
                    placeholder(placeHolder)
                }
            }
        }
    }

    private fun generateBlurHash(hash: String?): Bitmap? {
        return blurHashDecode(
                blurHash = hash,
                width = 20,
                height = 20,
                parallelTasks = 2
        )
    }

    companion object {
        private const val ZERO_PLACEHOLDER = 0
    }

}
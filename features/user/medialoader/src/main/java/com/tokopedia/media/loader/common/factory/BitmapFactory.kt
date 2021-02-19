package com.tokopedia.media.loader.common.factory

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.common.MediaLoaderFactory
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.transform.BlurHashDecoder.decode as blurHashDecode
import com.tokopedia.media.loader.listener.MediaListenerBuilder.callback as callbackListener

class BitmapFactory : MediaLoaderFactory<Bitmap>() {

    fun build(
            context: Context,
            properties: Properties,
            blurHash: String? = "",
            performanceMonitoring: PerformanceMonitoring? = null,
            request: GlideRequest<Bitmap>
    ) = setup(properties, request).apply {
        // startTimeRequest will use for performance tracking
        val startTimeRequest = System.currentTimeMillis()

        with(properties) {
            /*
            * because the medialoader placeholder has a different behavior,
            * a builder is needed to handle it. the type of placeholder following:
            * */
            blurHashPlaceHolder(context, blurHash, this, request)

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
            hash: String?,
            properties: Properties,
            request: GlideRequest<Bitmap>
    ): GlideRequest<Bitmap> {
        val placeHolder = properties.placeHolder
        val blurHash = properties.blurHash

        return request.apply {
            when {
                /*
                * validate if the placeholder have default placeholder value, 0.
                * it will check if the blurHash is active, the placeholder will render
                * the blurHash image, but if placeholder is 0 and blurHash is inactive,
                * the placeholder will render the default of built-in placeholder.
                * */
                placeHolder == ZERO_PLACEHOLDER -> {
                    if (blurHash && !hash.isNullOrEmpty()) {
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
                width = 50, //TODO
                height = 30 //TODO
        )
    }

    companion object {
        private const val ZERO_PLACEHOLDER = 0
    }

}
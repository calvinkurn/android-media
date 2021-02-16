package com.tokopedia.media.loader.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.transform.BlurHashDecoder
import com.tokopedia.media.loader.transform.CircleCrop
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy.Companion.mapToDiskCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat.Companion.mapToDecodeFormat
import com.tokopedia.media.loader.utils.MediaListenerBuilder.callback as callbackListener

class GlideBuilder {

    /*
    * The transformation mechanism carried out by medialoader is
    * that it will collect any transformations specified in the properties applied
    * and will be transformed at the same time using MultiTransform().
    * */
    private val _transform = mutableListOf<Transformation<Bitmap>>()

    fun build(
            context: Context,
            properties: Properties,
            blurHash: String? = "",
            performanceMonitoring: PerformanceMonitoring? = null,
            request: GlideRequest<Bitmap>
    ) = request.apply {
        // startTimeRequest will use for performance tracking
        val startTimeRequest = System.currentTimeMillis()

        with(properties) {
            /*
            * because the medialoader placeholder has a different behavior,
            * a builder is needed to handle it. the type of placeholder following:
            * */
            blurHashPlaceHolder(context, blurHash, this, request)

            if (!isAnimate) dontAnimate()
            if (thumbnailUrl.isNotEmpty()) thumbnail(loader(context, thumbnailUrl))
            if (roundedRadius > 0f) _transform.add(RoundedCorners(roundedRadius.toInt()))
            if (isCircular) _transform.add(CircleCrop())

            cacheStrategy?.let { diskCacheStrategy(mapToDiskCacheStrategy(it)) }
            overrideSize?.let { override(it.width, it.height) }
            decodeFormat?.let { format(mapToDecodeFormat(it)) }
            transforms?.let { _transform.addAll(it) }
            transform?.let { _transform.add(it) }
            signatureKey?.let { signature(it) }

            error(error)

            if (_transform.isNotEmpty()) {
                transform(MultiTransformation(_transform))
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
        val isCircular = properties.isCircular
        val placeHolder = properties.placeHolder
        val blurHash = properties.blurHash

        return request.apply {
            if (!isCircular) {
                when {
                    /*
                    * validate if the placeholder have default placeholder value, 0.
                    * it will check if the blurHash is active, the placeholder will render
                    * the blurHash image, but if placeholder is 0 and blurHash is inactive,
                    * the placeholder will render the default of built-in placeholder.
                    * */
                    placeHolder == ZERO_PLACEHOLDER -> {
                        if (blurHash && !hash.isNullOrEmpty()) {
                            placeholder(BitmapDrawable(context.resources, blurring(hash)))
                        } else {
                            placeholder(R.drawable.ic_media_default_placeholder)
                        }
                    }

                    // render the custom placeholder that provided by Properties()
                    placeHolder != ZERO_PLACEHOLDER && placeHolder > ZERO_PLACEHOLDER -> {
                        placeholder(placeHolder)
                    }
                }
            }
        }
    }

    private fun blurring(blurHash: String?): Bitmap? {
        return BlurHashDecoder.decode(
                blurHash = blurHash,
                width = 60, //TODO
                height = 20 //TODO
        )
    }

    private fun loader(context: Context, resource: Any?): RequestBuilder<Bitmap> {
        return GlideApp.with(context)
                .asBitmap()
                .load(resource)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    }

    companion object {
        private const val ZERO_PLACEHOLDER = 0
    }

}
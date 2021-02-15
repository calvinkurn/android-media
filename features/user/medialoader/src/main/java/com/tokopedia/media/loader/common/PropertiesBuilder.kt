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
import com.tokopedia.media.loader.utils.Listener.invoke as callbackListener

class PropertiesBuilder {

    private val _transform = mutableListOf<Transformation<Bitmap>>()

    fun build(
            context: Context,
            properties: Properties,
            blurHash: String? = "",
            performanceMonitoring: PerformanceMonitoring? = null,
            request: GlideRequest<Bitmap>
    ) = request.apply {
        val startTimeRequest = System.currentTimeMillis()

        with(properties) {
            blurHashPlaceHolder(context, blurHash, this, request)

            if (!isAnimate) dontAnimate()
            if (thumbnailUrl.isNotEmpty()) thumbnail(thumbnailLoader(context, thumbnailUrl))
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
        val isCircular = properties.isCircular
        val blurHash = properties.blurHash

        return request.apply {
            if (placeHolder != 0) {
                placeholder(placeHolder)
            } else {
                if ((!isCircular || blurHash) && !hash.isNullOrEmpty()) {
                    placeholder(BitmapDrawable(context.resources, blurring(hash)))
                } else {
                    placeholder(R.drawable.ic_media_default_placeholder)
                }
            }
        }
    }

    private fun blurring(blurHash: String?): Bitmap? {
        return BlurHashDecoder.decode(
                blurHash = blurHash,
                width = 60,
                height = 20
        )
    }

    private fun thumbnailLoader(context: Context, resource: Any?): RequestBuilder<Bitmap> {
        return GlideApp.with(context)
                .asBitmap()
                .load(resource)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    }

}
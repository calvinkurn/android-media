package com.tokopedia.media.loader.common

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.transform.CircleCrop
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

abstract class GlideBuilder<T> {

    /*
    * The transformation mechanism carried out by medialoader is
    * that it will collect any transformations specified in the properties applied
    * and will be transformed at the same time using MultiTransform().
    * */
    private val _transform = mutableListOf<Transformation<Bitmap>>()

    private fun transformation(properties: Properties) {
        with(properties) {
            if (roundedRadius > 0f) _transform.add(RoundedCorners(roundedRadius.toInt()))
            if (isCircular) _transform.add(CircleCrop())

            transforms?.let { _transform.addAll(it) }
            transform?.let { _transform.add(it) }
        }
    }

    fun setup(
            properties: Properties,
            request: GlideRequest<T>
    ) = request.apply {
        with(properties) {
            error(error)
            transformation(properties)

            if (!isAnimate) {
                dontAnimate()
            }

            cacheStrategy?.let { diskCacheStrategy(MediaCacheStrategy.mapToDiskCacheStrategy(it)) }
            overrideSize?.let { override(it.width, it.height) }
            decodeFormat?.let { format(MediaDecodeFormat.mapToDecodeFormat(it)) }
            signatureKey?.let { signature(it) }

            // bulk transforms from transformList
            if (_transform.isNotEmpty()) {
                transform(MultiTransformation(_transform))
            }
        }
    }

    protected fun loader(context: Context, resource: Any?): RequestBuilder<Bitmap> {
        return GlideApp.with(context)
                .asBitmap()
                .load(resource)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    }

}
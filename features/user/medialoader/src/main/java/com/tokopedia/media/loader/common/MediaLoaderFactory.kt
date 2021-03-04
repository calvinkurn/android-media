package com.tokopedia.media.loader.common

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

abstract class MediaLoaderFactory<T> {

    /*
    * The transformation mechanism carried out by medialoader is
    * that it will collect any transformations specified in the properties applied
    * and will be transformed at the same time using MultiTransform().
    * */
    private val _transform = mutableListOf<Transformation<Bitmap>>()

    private fun transformation(properties: Properties) {
        // clear before applying a transformations
        _transform.clear()

        with(properties) {
            // built-in RoundedCorners transformation
            if (roundedRadius > 0f) {
                _transform.add(RoundedCorners(roundedRadius.toInt()))
            }

            transforms?.let { _transform.addAll(it) }
            transform?.let { _transform.add(it) }
        }
    }

    fun setup(
            properties: Properties,
            request: GlideRequest<T>
    ) = request.apply {
        with(properties) {
            /*
            * set multiple transformation into list of transform (_transform)
            * and then bulk it the transforms from transformList with MultiTransformation
            * */
            transformation(properties)

            /*
            * regarding the circleCrop() didn't work in transformation above,
            * we need to transform manually in here
            * */
            if (properties.isCircular) {
                circleCrop()
            }

            if (_transform.isNotEmpty()) {
                request.transform(MultiTransformation(_transform))
            }

            // set custom error drawable
            error(error)

            // disable animation (default)
            if (!isAnimate) {
                dontAnimate()
            }

            // use custom signature for caching
            signatureKey?.let {
                signature(it)
            }?: signature(ObjectKey(properties.urlHasQualityParam))

            cacheStrategy?.let { diskCacheStrategy(MediaCacheStrategy.mapTo(it)) }
            decodeFormat?.let { format(MediaDecodeFormat.mapTo(it)) }
            overrideSize?.let { override(it.width, it.height) }
            signatureKey?.let { signature(it) }
        }
    }

    protected fun thumbnailFrom(context: Context, resource: Any?): RequestBuilder<Bitmap> {
        return GlideApp.with(context)
                .asBitmap()
                .load(resource)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    }

}
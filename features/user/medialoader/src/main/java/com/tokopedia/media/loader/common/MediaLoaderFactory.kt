package com.tokopedia.media.loader.common

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.*
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
    private val _transform = arrayListOf<Transformation<Bitmap>>()

    private fun transformation(
            properties: Properties,
            request: GlideRequest<T>
    ) {
        // clear before applying a transformations
        _transform.clear()

        with(properties) {
            // built-in RoundedCorners transformation
            if (roundedRadius > 0f) {
                _transform.add(RoundedCorners(roundedRadius.toInt()))
            }

            // built-in transformations
            if (properties.isCircular) _transform.add(CircleCrop())
            if (properties.centerCrop) _transform.add(CenterCrop())
            if (properties.fitCenter) _transform.add(FitCenter())
            if (properties.centerInside) _transform.add(CenterInside())

            // store-bulk transformation into MultiTransformations()
            if (transform != null) {
                _transform.add(transform!!)
            }

            if (!transforms.isNullOrEmpty()) {
                _transform.addAll(transforms!!)
            }
        }
    }

    fun setup(
            properties: Properties,
            request: GlideRequest<T>
    ) = request.also {
        with(properties) {
            /*
            * set multiple transformation into list of transform (_transform)
            * and then bulk it the transforms from transformList with MultiTransformation
            * */
            transformation(this, request)
            if (_transform.isNotEmpty()) {
                request.transform(MultiTransformation(_transform))
            }


            // set custom error drawable
            it.error(error)

            // disable animation (default)
            if (!isAnimate) {
                it.dontAnimate()
            }

            // use custom signature for caching
            signatureKey?.let { key ->
                it.signature(key)
            }?: it.signature(ObjectKey(properties.urlHasQualityParam))

            cacheStrategy?.let { cacheStrategy -> it.diskCacheStrategy(MediaCacheStrategy.mapTo(cacheStrategy)) }
            decodeFormat?.let { format -> it.format(MediaDecodeFormat.mapTo(format)) }
            overrideSize?.let { newSize -> it.override(newSize.width, newSize.height) }
            signatureKey?.let { key -> it.signature(key) }
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
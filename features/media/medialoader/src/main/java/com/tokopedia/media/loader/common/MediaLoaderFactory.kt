package com.tokopedia.media.loader.common

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
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

    private fun transformation(properties: Properties) {
        // clear before applying a transformations
        _transform.clear()

        with(properties) {
            if (properties.centerCrop) {
                _transform.add(CenterCrop())
            }

            if (properties.fitCenter) {
                _transform.add(FitCenter())
            }

            if (properties.centerInside) {
                _transform.add(CenterInside())
            }

            // store-bulk transformation into MultiTransformations()
            if (transform != null) {
                _transform.add(transform!!)
            }

            if (!transforms.isNullOrEmpty()) {
                _transform.addAll(transforms!!)
            }

            // built-in RoundedCorners transformation
            if (roundedRadius > 0f) {
                _transform.add(RoundedCorners(roundedRadius.toInt()))
            }
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
            transformation(this)

            if (isCircular) {
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
            if (isCache && signatureKey != null) {
                signature(signatureKey!!)
            } else if (isCache && signatureKey == null) {
                signature(ObjectKey(properties.urlHasQualityParam + cacheVersionNumber))
            } else {
                skipMemoryCache(true)
            }

            if (cacheStrategy != null) {
                diskCacheStrategy(MediaCacheStrategy.mapTo(cacheStrategy!!))
            }

            if (decodeFormat != null) {
                format(MediaDecodeFormat.mapTo(decodeFormat!!))
            }

            if (overrideSize != null && overrideSize?.width.isMoreThanZero()) {
                override(overrideSize?.width?: 0, overrideSize?.height?: 0)
            }
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
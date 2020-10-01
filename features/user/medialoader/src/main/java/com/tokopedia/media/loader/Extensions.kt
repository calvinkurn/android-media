package com.tokopedia.media.loader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.common.UrlBuilder
import com.tokopedia.media.loader.utils.DEFAULT_ROUNDED

fun ImageView.loadImage(drawable: Drawable) = this.setImageDrawable(drawable)

fun ImageView.loadImage(resource: Int) = this.setImageResource(resource)

fun ImageView.loadImage(url: String) = call(url, Properties())

inline fun ImageView.loadImage(url: String, properties: Properties.() -> Unit) = call(url, Properties().apply(properties))

fun ImageView.loadImageCircle(url: String) = call(url, Properties())

fun ImageView.loadImageRounded(resource: Int, rounded: Float) = this.setImageResource(resource)

inline fun ImageView.loadImageRounded(
        url: String?,
        rounded: Float = DEFAULT_ROUNDED,
        configuration: Properties.() -> Unit = {
            Properties().apply {
                roundedRadius = rounded
            }
        }) {
    call(url, Properties().apply(configuration))
}

fun ImageView?.clearImage() {
    if (this != null) {
        Glide.with(this.context).clear(this)
    }
}

@PublishedApi
internal fun ImageView.call(url: String?, properties: Properties) {
    builder(UrlBuilder.urlBuilder(context, url), properties)
}

@PublishedApi
internal fun ImageView.builder(url: GlideUrl, properties: Properties) {
    val imageView = this
    with(properties) {
        GlideBuilder.loadImage(
                imageView = imageView,
                url = url,
                thumbnailUrl = thumbnailUrl,
                radius = roundedRadius,
                signatureKey = signature,
                cacheStrategy = cacheStrategy,
                placeHolder = placeHolder,
                resOnError = error,
                isAnimate = isAnimate,
                isCircular = isCircular,
                overrideSize = overrideSize,
                decodeFormat = decodeFormat,
                transform = transform,
                listener = loaderListener
        )
    }
}
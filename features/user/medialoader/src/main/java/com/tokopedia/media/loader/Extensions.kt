package com.tokopedia.media.loader

import android.widget.ImageView
import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.common.UrlBuilder
import com.tokopedia.media.loader.utils.DEFAULT_ROUNDED

fun ImageView.loadImage(url: String) = call(url, Properties())

inline fun ImageView.loadImage(url: String, properties: Properties.() -> Unit) = call(url, Properties().apply(properties))

inline fun ImageView.loadImageRounded(
        url: String,
        rounded: Float = DEFAULT_ROUNDED,
        configuration: Properties.() -> Unit = {
            Properties().apply {
                roundedRadius = rounded
            }
        }) {
    call(url, Properties().apply(configuration))
}

@PublishedApi
internal fun ImageView.call(url: String, properties: Properties) {
    builder(UrlBuilder.urlBuilder(context, url), properties)
}

@PublishedApi
internal fun ImageView.builder(url: GlideUrl, properties: Properties) {
    val imageView = this
    with(properties) {
        GlideBuilder.loadImage(
                imageView,
                url,
                roundedRadius,
                signature,
                cacheStrategy,
                placeHolder,
                error,
                isAnimate
        )
    }
}
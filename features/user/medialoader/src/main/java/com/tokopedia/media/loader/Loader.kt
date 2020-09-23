package com.tokopedia.media.loader

import android.widget.ImageView
import com.tokopedia.media.loader.common.UrlBuilder.urlBuilder
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.utils.DEFAULT_ROUNDED

fun ImageView.loadImage(url: Any) = call(url, Properties())

inline fun ImageView.loadImage(url: Any, properties: Properties.() -> Unit) = call(url, Properties().apply(properties))

inline fun ImageView.loadImageRounded(
        url: Any,
        rounded: Float = DEFAULT_ROUNDED,
        configuration: Properties.() -> Unit = {
            Properties().apply {
                roundedRadius = rounded
            }
        }) {
    call(url, Properties().apply(configuration))
}

@PublishedApi
internal fun ImageView.call(url: Any, properties: Properties) {
    builder(if (url is String) urlBuilder(context, url) else url, properties)
}

@PublishedApi
internal fun ImageView.builder(url: Any, properties: Properties) {
    val imageView = this
    with(properties) {
        MediaGlide(imageView, url)
                .isRounded(isRounded, roundedRadius)
                .showPlaceHolder(placeHolder)
                .cacheStrategy(cacheStrategy)
                .isAnimate(isAnimate)
                .showError(error)
                .build()
    }
}
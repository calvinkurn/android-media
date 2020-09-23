package com.tokopedia.media.loader

import android.widget.ImageView
import com.tokopedia.media.loader.UrlBuilder.urlBuilder
import com.tokopedia.media.loader.data.Properties

fun ImageView.loadImage(url: Any) = call(url, Properties())

fun ImageView.loadImage(url: Any, configuration: Properties.() -> Unit) = call(url, Properties().apply(configuration))

fun ImageView.loadImageRounded(
        url: Any,
        rounded: Float,
        configuration: Properties.() -> Unit = {
            Properties().apply {
                roundedRadius = rounded
            }
        }) {
    call(url, Properties().apply(configuration))
}

private fun ImageView.call(url: Any, configuration: Properties) {
    builder(if (url is String) urlBuilder(context, url) else url, configuration)
}

private fun ImageView.builder(url: Any, configuration: Properties) {
    val imageView = this
    with(configuration) {
        MediaGlide(imageView, url)
                .isRounded(isRounded, roundedRadius)
                .showPlaceHolder(placeHolder)
                .cacheStrategy(cacheStrategy)
                .isAnimate(animate)
                .showError(error)
                .build()
    }
}
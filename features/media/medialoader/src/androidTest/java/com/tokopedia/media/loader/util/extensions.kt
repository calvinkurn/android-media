package com.tokopedia.media.loader.util

import android.widget.ImageView
import com.tokopedia.media.loader.MediaLoaderApi
import com.tokopedia.media.loader.data.Properties

// instrument test only

internal inline fun ImageView.newLoadImage(
    url: String,
    crossinline properties: Properties.() -> Unit = {}
) = Properties()
    .apply(properties)
    .also {
        MediaLoaderApi.loadImage(
            imageView = this,
            properties = it.setSource(url)
        )
    }

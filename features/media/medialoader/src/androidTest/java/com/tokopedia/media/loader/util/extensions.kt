package com.tokopedia.media.loader.util

import android.widget.ImageView
import com.tokopedia.media.loader.MediaLoaderApi
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.v1.LegacyMediaLoaderApi

// instrument test only

internal inline fun ImageView.legacyLoadImage(
    url: String,
    crossinline properties: Properties.() -> Unit = {}
) = Properties()
    .apply(properties)
    .also {
        LegacyMediaLoaderApi.loadImage(
            imageView = this,
            isSecure = it.isSecure,
            properties = it.setSource(url)
        )
    }

internal inline fun ImageView.v2LoadImage(
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

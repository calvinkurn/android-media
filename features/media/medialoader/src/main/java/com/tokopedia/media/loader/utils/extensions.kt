package com.tokopedia.media.loader.utils

import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.module.GlideRequest

private val handler by lazy(LazyThreadSafetyMode.NONE) {
    Looper.myLooper()?.let {
        Handler(it)
    }
}

internal fun <T> GlideRequest<T>.mediaLoad(properties: Properties): GlideRequest<T> {
    return if (properties.data is String) {
        load(properties.generateUrl())
    } else {
        load(properties.data)
    }
}

internal fun <T> GlideRequest<T>.delayInto(imageView: ImageView, properties: Properties) {
    // render image
    if (properties.renderDelay <= 0L) {
        into(imageView)
    } else {
        handler?.postDelayed({
            into(imageView)
        }, properties.renderDelay)
    }
}

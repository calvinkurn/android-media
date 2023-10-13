package com.tokopedia.media.loader.utils

import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.data.HEADER_FMT
import com.tokopedia.media.loader.data.HEADER_KEY_AUTH
import com.tokopedia.media.loader.data.HEADER_USER_ID
import com.tokopedia.media.loader.data.HEADER_X_DEVICE
import com.tokopedia.media.loader.data.PREFIX_BEARER
import com.tokopedia.media.loader.data.WEBP_SUPPORT
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

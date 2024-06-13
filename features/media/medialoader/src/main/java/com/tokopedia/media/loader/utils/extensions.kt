package com.tokopedia.media.loader.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.tokopedia.media.loader.data.DEFAULT_TIMEOUT
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.module.GlideRequest
import java.io.File
import java.util.Locale

private val handler by lazy(LazyThreadSafetyMode.NONE) {
    Looper.myLooper()?.let {
        Handler(it)
    }
}

internal fun <T> GlideRequest<T>.mediaLoad(properties: Properties, isDownload: Boolean = false): GlideRequest<T> {
    return if (properties.data is String && !isDownload) {
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

// load local gif asset using URI.
// When resource int ref is on different module or using DF, loader cannot load it due to auth between module
internal fun <T> GlideRequest<T>.loadLookup(context: Context, resId: Int): GlideRequest<T> {
    val resources = context.resources
    val uri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context.packageName) // Look up the resources in the application with its splits loaded
        .appendPath(resources.getResourceTypeName(resId))
        .appendPath(
            String.format(
                Locale.US,
                "%s:%s",
                resources.getResourcePackageName(resId),
                resources.getResourceEntryName(resId)
            )
        ) // Look up the dynamic resource in the split namespace.
        .build()
    return load(uri)
}

internal fun GlideRequest<Bitmap>.transition(properties: Properties): GlideRequest<Bitmap> {
    properties.transition?.let {
        this.transition(it)
    }
    return this
}

@SuppressLint("CheckResult")
internal fun <T> GlideRequest<T>.timeout(properties: Properties): GlideRequest<T> {
    timeout(properties.timeoutMS ?: DEFAULT_TIMEOUT)
    return this
}

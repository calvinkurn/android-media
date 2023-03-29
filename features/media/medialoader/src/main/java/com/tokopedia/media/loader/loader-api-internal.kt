package com.tokopedia.media.loader

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.module.GlideApp

@PublishedApi
internal fun ImageView.call(source: Any?, properties: Properties, isSecure: Boolean = false) {
    if (context.isValid()) {
        try {
            MediaLoaderApi.loadImage(
                imageView = this,
                isSecure = isSecure,
                properties = properties.setSource(source),
            )
        } catch (e: Exception) {
            e.printStackTrace()

            /*
            * don't let the imageView haven't image to show,
            * render with error drawable unify instead.
            * */
            setImageResource(ERROR_RES_UNIFY)
        }
    }
}

fun ImageView?.clearImage() {
    if (this != null && context.isValid()) {
        GlideApp.with(this.context).clear(this)
    }
}

fun Context?.isValid(): Boolean {
    return when {
        this == null -> false
        this is Activity -> !(this.isDestroyed || this.isFinishing)
        else -> true
    }
}
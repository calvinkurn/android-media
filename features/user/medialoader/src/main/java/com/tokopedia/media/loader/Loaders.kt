package com.tokopedia.media.loader

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.tokopedia.media.loader.GlideBuilder.loadGifImage
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.utils.DEFAULT_ROUNDED

fun ImageView.loadImage(bitmap: Bitmap?) = call(bitmap, Properties())

fun ImageView.loadImage(drawable: Drawable?) = this.setImageDrawable(drawable)

fun ImageView.loadImage(resource: Int) = this.setImageResource(resource)

fun ImageView.loadImage(url: String?) = call(url, Properties().apply {
    useBlurHash(true)
})

fun ImageView.loadImage(uri: Uri) = this.setImageURI(uri)

fun ImageView.loadAsGif(url: String) = loadGifImage(this, url)

inline fun ImageView.loadImage(
        url: String?,
        crossinline properties: Properties.() -> Unit
) = call(url, Properties().apply(properties))

fun ImageView.loadImageCircle(url: String) {
    call(url, Properties().apply {
        isCircular(true)
    })
}

fun ImageView.loadImageRounded(resource: Int, rounded: Float) = this.setImageResource(resource)

inline fun ImageView.loadImageRounded(
        url: String?,
        rounded: Float = DEFAULT_ROUNDED,
        crossinline configuration: Properties.() -> Unit = {
            Properties().apply {
                setRoundedRadius(rounded)
            }
        }) {
    call(url, Properties().apply(configuration))
}

fun ImageView.loadIcon(url: String?) = call(
        url,
        Properties().apply {
            isIcon = true
        }
)

fun ImageView?.clearImage() {
    if (this != null && context.isValidContext()) {
        GlideApp.with(this.context).clear(this)
    }
}

@PublishedApi
internal fun ImageView.call(source: Any?, properties: Properties) {
    if (context.isValidContext()) {
        try {
            GlideBuilder.loadImage(
                    data = source,
                    imageView = this,
                    properties = properties
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private fun Context?.isValidContext(): Boolean {
    return when {
        this == null -> false
        this is Activity -> !(this.isDestroyed || this.isFinishing)
        else -> true
    }
}
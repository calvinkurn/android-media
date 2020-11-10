package com.tokopedia.media.loader

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.load.Key
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.media.common.Loader
import com.tokopedia.media.loader.GlideBuilder.loadGifImage
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.utils.DEFAULT_ROUNDED
import com.tokopedia.media.loader.utils.mediaSignature
import com.tokopedia.media.loader.GlideBuilder.loadImage as glideLoadImage

fun ImageView.loadImage(bitmap: Bitmap?) = call(bitmap, Properties())

fun ImageView.loadImage(drawable: Drawable) = this.setImageDrawable(drawable)

fun ImageView.loadImage(resource: Int) = this.setImageResource(resource)

fun ImageView.loadImage(url: String) = call(url, Properties())

fun ImageView.loadImage(uri: Uri) = this.setImageURI(uri)

fun ImageView.loadAsGif(url: String) = loadGifImage(this, url)

inline fun ImageView.loadImage(url: String, properties: Properties.() -> Unit) = call(url, Properties().apply(properties))

fun ImageView.loadImageCircle(url: String) {
    call(url, Properties().apply {
        isCircular = true
    })
}

fun ImageView.loadImageRounded(resource: Int, rounded: Float) = this.setImageResource(resource)

inline fun ImageView.loadImageRounded(
        url: String?,
        rounded: Float = DEFAULT_ROUNDED,
        crossinline configuration: Properties.() -> Unit = {
            Properties().apply {
                roundedRadius = rounded
            }
        }) {
    call(url, Properties().apply(configuration))
}

fun ImageView?.clearImage() {
    if (this != null) {
        GlideApp.with(this.context).clear(this)
    }
}

@PublishedApi
internal fun ImageView.call(url: Any?, properties: Properties) {
    val imageView = this
    glideLoadImage(imageView, properties.apply { data = url })
}
package com.tokopedia.media.loader

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.tokopedia.media.loader.LoaderApi.loadGifImage
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.transform.FitCenter
import com.tokopedia.media.loader.utils.DEFAULT_ROUNDED

fun ImageView.loadAsGif(url: String) = loadGifImage(this, url, Properties())

fun ImageView.loadAsGif(
        url: String,
        properties: Properties.() -> Unit
) = loadGifImage(this, url, Properties().apply(properties))

fun ImageView.loadImage(bitmap: Bitmap?) = call(bitmap, Properties())

fun ImageView.loadImage(drawable: Drawable?) = this.setImageDrawable(drawable)

fun ImageView.loadImage(resource: Int) = this.setImageResource(resource)

fun ImageView.loadImage(uri: Uri) = this.setImageURI(uri)

fun ImageView.loadImage(url: String?) = this.loadImage(url) {}

fun ImageView.loadImageFitCenter(url: String?) = this.loadImage(url) { transform(FitCenter()) }

fun ImageView.loadImageWithoutPlaceholder(url: String?) = this.loadImage(url) { setPlaceHolder(-1) }

fun ImageView.loadImageWithoutPlaceholder(
        url: String?,
        properties: Properties.() -> Unit
) = call(url, Properties().apply(properties).also {
    it.setPlaceHolder(-1)
})

inline fun ImageView.loadImage(
        url: String?,
        crossinline properties: Properties.() -> Unit
) = call(url, Properties().apply(properties))

fun ImageView.loadImageCircle(url: String) {
    call(url, Properties().apply {
        isCircular(true)

        /*
        * loadImageCircle() extension must be haven't placeholder,
        * the loader effect should be handled by team by
        * using own shimmering.
        * */
        setPlaceHolder(-1)
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

inline fun ImageView.loadIcon(
        url: String?,
        crossinline properties: Properties.() -> Unit
) = call(url, Properties().apply(properties).also {
    it.useBlurHash(false)

     /*
     * loadIcon() extension must be haven't placeholder,
     * the loader effect should be handled by team by
     * using own shimmering.
     * */
    it.setPlaceHolder(-1)
})

fun ImageView.loadIcon(url: String?) = this.loadIcon(url) {}

fun ImageView?.clearImage() {
    if (this != null && context.isValidContext()) {
        GlideApp.with(this.context).clear(this)
    }
}

@PublishedApi
internal fun ImageView.call(source: Any?, properties: Properties) {
    if (context.isValidContext()) {
        try {
            LoaderApi.loadImage(
                    imageView = this,
                    properties = properties.apply {
                        // passing the image source (url, uri, etc.)
                        setSource(source)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()

            /*
            * don't let the imageView haven't image
            * render with error drawable
            * */
            this.loadImage(R.drawable.ic_media_default_error)
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
@file:JvmName("ExtensionKt")
package com.tokopedia.media.loader

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.tokopedia.media.loader.MediaLoaderApi.loadGifImage
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.utils.DEFAULT_ROUNDED

fun ImageView.loadAsGif(url: String) = loadGifImage(this, url, Properties())

fun ImageView.loadAsGif(
    url: String,
    properties: Properties.() -> Unit
) = loadGifImage(this, url, Properties().apply(properties))

fun ImageView.loadImage(bitmap: Bitmap?) = call(bitmap, Properties())

fun ImageView.loadImage(drawable: Drawable?) = this.setImageDrawable(drawable)

fun ImageView.loadImage(resource: Int) {
    if (resource != 0) {
        this.setImageResource(resource)
    } else {
        this.setImageResource(ERROR_RES_UNIFY)
    }
}

inline fun ImageView.loadImage(
    resource: Int,
    crossinline properties: Properties.() -> Unit = {}
) = call(resource, Properties()
    .apply(properties))

fun ImageView.loadImage(uri: Uri) {
    if (uri != Uri.EMPTY) {
        this.setImageURI(uri)
    } else {
        this.loadImage(ERROR_RES_UNIFY)
    }
}

inline fun ImageView.loadImage(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties()
    .apply(properties))

inline fun ImageView.loadImageFitCenter(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties()
    .apply(properties)
    .fitCenter())

inline fun ImageView.loadImageWithoutPlaceholder(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties()
    .apply(properties)
    .setPlaceHolder(-1))

inline fun ImageView.loadImageCircle(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties()
    .apply(properties)
    .isCircular(true)

    /*
     * loadImageCircle() extension must be haven't placeholder,
     * the loader effect should be handled by team by
     * using own shimmering.
     * */
    .setPlaceHolder(-1)
)

fun ImageView.loadImageRounded(
    resource: Int,
    rounded: Float
) = this.setImageResource(resource)

inline fun ImageView.loadImageRounded(
    uri: Uri,
    rounded: Float = DEFAULT_ROUNDED,
    crossinline properties: Properties.() -> Unit = {}
) = call(uri, Properties()
    .apply(properties)
    .setRoundedRadius(rounded)
)

inline fun ImageView.loadImageRounded(
    data: Bitmap,
    rounded: Float = DEFAULT_ROUNDED,
    crossinline properties: Properties.() -> Unit = {}
) {
    call(data, Properties()
        .apply(properties)
        .setRoundedRadius(rounded)
    )
}

inline fun ImageView.loadImageRounded(
    url: String?,
    rounded: Float = DEFAULT_ROUNDED,
    crossinline properties: Properties.() -> Unit = {}
) {
    call(url, Properties()
        .apply(properties)
        .setRoundedRadius(rounded)
    )
}

inline fun ImageView.loadIcon(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) {
    call(url, Properties()
        .apply(properties)
        .overrideSize(Resize(300, 300))
        .isIcon(true)
    )
}

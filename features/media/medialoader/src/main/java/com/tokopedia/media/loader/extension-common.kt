@file:JvmName("ExtensionKt")
package com.tokopedia.media.loader

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.tokopedia.media.loader.MediaLoaderApi.loadGifImage
import com.tokopedia.media.loader.data.DEFAULT_ROUNDED
import com.tokopedia.media.loader.data.DEFAULT_ICON_SIZE
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.data.Resize

fun ImageView.loadAsGif(
    url: String
) = loadGifImage(
    this,
    url,
    Properties()
        .isGif(true)
)

/**
 * An ImageLoader for loading GIF images into an ImageView on Android. This class provides methods to
 * efficiently load and display GIF images in an ImageView with additional options for customization and control.
 *
 * <b>Sample Usage</b>
 *
 * ```
 * imageView.loadAsGif("https://tokopedia.net/sample.gif")
 * ```
 *
 * <b>Sample Usage with Custom Properties</b>
 *
 * ```
 * imageView.loadAsGif("https://tokopedia.net/sample.gif") {
 *    setErrorDrawable(R.drawable.ic_error)
 *    setCacheStrategy(MediaCacheStrategy.RESOURCE)
 *    overrideSize(Resize(540, 540))
 * }
 * ```
 *
 * @receiver [ImageView]
 * @param url Url to load image
 * @param properties A custom properties
 * @since v1.0.0
 */
fun ImageView.loadAsGif(
    url: String,
    properties: Properties.() -> Unit
) = loadGifImage(
    this,
    url,
    Properties()
        .apply(properties)
        .isGif(true)
)

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

/**
 * An extension function on [ImageView] receiver to load an image from some remote url.
 *
 * This function uses <b>Glide</b> which is a 3rd party library to handle all the networking and
 * caching side of things. Glide handles the loading w.r.t. the lifecycle of the view for you.
 *
 * <b>Sample Usage</b>
 *
 * ```
 * imageView.loadImage("https://tokopedia.net/sample.png")
 * ```
 *
 * <b>Sample Usage with Custom Properties</b>
 *
 * ```
 * imageView.loadImage("https://tokopedia.net/sample.png") {
 *    setErrorDrawable(R.drawable.ic_error)
 *    setCacheStrategy(MediaCacheStrategy.RESOURCE)
 * }
 * ```
 *
 * @receiver [ImageView]
 * @param url Url to load image
 * @param properties A custom properties
 * @since v1.0.0
 */
inline fun ImageView.loadImage(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties()
    .apply(properties))

inline fun ImageView.loadImage(
    url: Uri?,
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

/**
 * An ImageLoader for loading small icon images into an ImageView on Android.
 * This class provides methods to efficiently load and display small icon images in an ImageView
 * with supporting a custom properties.
 *
 * The differentiate between the [loadImage] one is, the [loadIcon] doesn't send the log into tracker,
 * hence we could optimize our tracking services. Also the [loadIcon] extension it will be override
 * the image size into [DEFAULT_ICON_SIZE].
 *
 * <b>Sample Usage</b>
 *
 * ```
 * imageView.loadIcon("https://tokopedia.net/cart-icon.png")
 * ```
 *
 * @receiver [ImageView]
 * @param url Url to load icon
 * @param properties A custom properties
 * @since v1.0.0
 */
inline fun ImageView.loadIcon(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) {
    call(url, Properties()
        .apply(properties)
        .overrideSize(Resize(DEFAULT_ICON_SIZE, DEFAULT_ICON_SIZE))
        .isIcon(true)
    )
}

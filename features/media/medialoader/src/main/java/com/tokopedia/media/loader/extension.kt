package com.tokopedia.media.loader

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.tokopedia.media.loader.MediaLoaderApi.loadGifImage
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.utils.DEFAULT_ROUNDED
import com.tokopedia.media.loader.utils.MediaTarget
import com.tokopedia.media.loader.utils.drawableFromId
import com.tokopedia.media.loader.wrapper.MediaDataSource
import com.tokopedia.media.loader.MediaLoaderApi.loadImage as loadImageBuilder
import com.tokopedia.media.loader.MediaLoaderTarget.loadImage as loadImageWithTarget

fun ImageView.loadAsGif(url: String) = loadGifImage(this, url, Properties())

fun ImageView.loadAsGif(
        url: String,
        properties: Properties.() -> Unit
) = loadGifImage(this, url, Properties().apply(properties))

fun ImageView.loadImage(bitmap: Bitmap?) = call(bitmap, Properties())

fun ImageView.loadImage(drawable: Drawable?) = this.setImageDrawable(drawable)

fun ImageView.loadImage(resource: Int) = this.setImageDrawable(drawableFromId(this.context, resource))

fun ImageView.loadImage(uri: Uri) = this.setImageURI(uri)

inline fun ImageView.loadImage(
        url: String?,
        crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties().apply(properties))

inline fun ImageView.loadImageFitCenter(
        url: String?,
        crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties().apply(properties).fitCenter())

inline fun ImageView.loadImageWithoutPlaceholder(
        url: String?,
        crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties()
        .apply(properties)
        .setPlaceHolder(-1))

inline fun ImageView.loadImageCircle(
        url: String?,
        crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties().apply(properties)
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
) = call(url, Properties().apply {
    apply(properties)
    useBlurHash(false)

    /*
    * loadIcon() extension must be haven't placeholder,
    * the loader effect should be handled by team by
    * using own shimmering.
    * */
    setPlaceHolder(-1)
})

fun ImageView.loadImageTopRightCrop(source: String) {
    if (context.isValid()) {
        try {
            MediaLoaderApi.loadImage(this, source)
        } catch (e: Exception) {
            e.printStackTrace()

            /*
            * don't let the imageView haven't image
            * render with error drawable
            * */
            this.loadImage(ERROR_RES_UNIFY)
        }
    }
}

fun ImageView.loadImageRounded(
        source: String,
        radius: Int,
        onSuccess: (Bitmap?, MediaDataSource?) -> Unit
) {
    if (context.isValid()) {
        try {
            MediaLoaderApi.loadImageRounded(this, source, radius, onSuccess)
        } catch (e: Exception) {
            e.printStackTrace()

            /*
            * don't let the imageView haven't image
            * render with error drawable
            * */
            this.loadImage(ERROR_RES_UNIFY)
        }
    }
}

@PublishedApi
internal fun ImageView.call(source: Any?, properties: Properties) {
    if (context.isValid()) {
        try {
            loadImageBuilder(
                    imageView = this,
                    properties = properties.apply {
                        useBlurHash(false)
                        setSource(source)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()

            /*
            * don't let the imageView haven't image
            * render with error drawable
            * */
            this.loadImage(ERROR_RES_UNIFY)
        }
    }
}

fun <T: View> loadImageWithTarget(
        context: Context,
        url: String,
        properties: Properties.() -> Unit,
        mediaTarget: MediaTarget<T>
) {
    loadImageWithTarget(
            context,
            Properties().apply(properties).setSource(url),
            mediaTarget
    )
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
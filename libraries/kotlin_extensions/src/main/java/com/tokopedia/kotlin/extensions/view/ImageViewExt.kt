package com.tokopedia.kotlin.extensions.view

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tokopedia.kotlin.extensions.R
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.resources.DrawableUtils

/**
 * @author by milhamj on 30/11/18.
 */

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImage(url)", "com.tokopedia.media.loader")
)
fun ImageView.loadImage(url: String, resId: Int = R.drawable.ic_loading_placeholder) {
    if (context.isValidGlideContext()) {
        try {
            ImageUtils.loadImage2(this, url, resId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImageCircle(url)", "com.tokopedia.media.loader")
)
fun ImageView.loadImageCircle(url: String) {
    if (context.isValidGlideContext()) {
        try {
            ImageUtils.loadImageCircle2(context, this, url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImageRounded(url, radius)", "com.tokopedia.media.loader")
)
fun ImageView.loadImageRounded(url: String, radius: Float = 5.0f) {
    if (context.isValidGlideContext()) {
        try {
            ImageUtils.loadImageRounded2(context, this, url, radius)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImage(url) { placeError = false }", "com.tokopedia.media.loader")
)
fun ImageView.loadImageWithoutPlaceholder(@DrawableRes drawableId: Int) {
    if (context.isValidGlideContext()) {
        try {
            ImageUtils.loadImageWithIdWithoutPlaceholder(this, drawableId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImage(url) { placeError = false }", "com.tokopedia.media.loader")
)
fun ImageView.loadImageWithoutPlaceholder(url: String) {
    if (context.isValidGlideContext()) {
        try {
            ImageUtils.loadImageWithoutPlaceholderAndError( this, url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImageDrawable(resourceId)", "com.tokopedia.media.loader")
)
fun ImageView.loadImageDrawable(@DrawableRes drawableId: Int) {
    if (context.isValidGlideContext()) {
        try {
            this.setImageDrawable(DrawableUtils.getDrawable(context, drawableId))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.clearImage()", "com.tokopedia.media.loader")
)
fun ImageView.clearImage() {
    if (context.isValidGlideContext()) {
        try {
            ImageUtils.clearImage(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Context?.isValidGlideContext(): Boolean {
    return when {
        this == null -> false
        this is Activity -> !(this.isDestroyed || this.isFinishing)
        else -> true
    }
}
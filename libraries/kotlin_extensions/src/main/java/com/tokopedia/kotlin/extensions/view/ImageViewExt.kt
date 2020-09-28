package com.tokopedia.kotlin.extensions.view

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
    ImageUtils.loadImage2(this, url, resId)
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImageCircle(url)", "com.tokopedia.media.loader")
)
fun ImageView.loadImageCircle(url: String) {
    ImageUtils.loadImageCircle2(context, this, url)
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImageRounded(url, radius)", "com.tokopedia.media.loader")
)
fun ImageView.loadImageRounded(url: String, radius: Float = 5.0f) {
    ImageUtils.loadImageRounded2(context, this, url, radius)
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImage(url) { placeError = false }", "com.tokopedia.media.loader")
)
fun ImageView.loadImageWithoutPlaceholder(@DrawableRes drawableId: Int) {
    ImageUtils.loadImageWithIdWithoutPlaceholder(this, drawableId)
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImage(url) { placeError = false }", "com.tokopedia.media.loader")
)
fun ImageView.loadImageWithoutPlaceholder(url: String) {
    ImageUtils.loadImageWithoutPlaceholderAndError( this, url)
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.loadImageDrawable(resourceId)", "com.tokopedia.media.loader")
)
fun ImageView.loadImageDrawable(@DrawableRes drawableId: Int) {
    this.setImageDrawable(DrawableUtils.getDrawable(context, drawableId))
}

@Deprecated(
        message = "Please use medialoader module instead",
        replaceWith = ReplaceWith("imageView.clearImage()", "com.tokopedia.media.loader")
)
fun ImageView.clearImage() {
    ImageUtils.clearImage(this)
}
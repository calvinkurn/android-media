package com.tokopedia.kotlin.extensions.view

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tokopedia.kotlin.extensions.R
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.resources.DrawableUtils

/**
 * @author by milhamj on 30/11/18.
 */

fun ImageView.loadImage(url: String, resId: Int = R.drawable.ic_loading_placeholder) {
    ImageUtils.loadImage2(this, url, resId)
}

fun ImageView.loadImageCircle(url: String) {
    ImageUtils.loadImageCircle2(context, this, url)
}

fun ImageView.loadImageRounded(url: String, radius: Float = 5.0f) {
    ImageUtils.loadImageRounded2(context, this, url, radius)
}

fun ImageView.loadImageWithoutPlaceholder(@DrawableRes drawableId: Int) {
    ImageUtils.loadImageWithIdWithoutPlaceholder(this, drawableId)
}

fun ImageView.loadImageWithoutPlaceholder(url: String) {
    ImageUtils.loadImageWithoutPlaceholderAndError( this, url)
}

fun ImageView.loadImageDrawable(@DrawableRes drawableId: Int) {
    this.setImageDrawable(DrawableUtils.getDrawable(context, drawableId))
}

fun ImageView.clearImage() {
    ImageUtils.clearImage(this)
}
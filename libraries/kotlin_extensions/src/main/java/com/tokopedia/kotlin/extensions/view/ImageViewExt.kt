package com.tokopedia.kotlin.extensions.view

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.R

/**
 * @author by milhamj on 30/11/18.
 */

fun ImageView.loadImage(url: String) {
    this.loadImage(url, R.drawable.ic_loading_image)
}

fun ImageView.loadImage(url: String, @DrawableRes errorRedId: Int) {
    ImageHandler.loadImage2(this, url, errorRedId)
}

fun ImageView.loadImage(@DrawableRes drawableId: Int) {
    ImageHandler.loadImageWithId(this, drawableId)
}

fun ImageView.loadImageWithoutPlaceholder(@DrawableRes drawableId: Int) {
    ImageHandler.loadImageWithIdWithoutPlaceholder(this, drawableId)
}

fun ImageView.loadImageCircle(url: String) {
    ImageHandler.loadImageCircle2(this.context, this, url)
}

fun ImageView.loadImageRounded(url: String) {
    ImageHandler.loadImageRounded2(this.context, this, url)
}

fun ImageView.loadImageRounded(url: String, radius: Float) {
    ImageHandler.loadImageRounded2(this.context, this, url, radius)
}

fun ImageView.clearImage() {
    ImageHandler.clearImage(this)
}
package com.tokopedia.kotlin.extensions.view

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.R

/**
 * @author by milhamj on 30/11/18.
 */

fun ImageView.loadImage(url: String, resId: Int = R.drawable.ic_loading_image) {
    ImageHandler.loadImage2(this, url, resId)
}

fun ImageView.loadImageCircle(url: String) {
    ImageHandler.loadImageCircle2(context, this, url)
}

fun ImageView.loadImageRounded(url: String, radius: Float = 5.0f) {
    ImageHandler.loadImageRounded2(context, this, url, radius)
}

fun ImageView.loadImageWithoutPlaceholder(@DrawableRes drawableId: Int) {
    ImageHandler.loadImageWithIdWithoutPlaceholder(this, drawableId)
}

fun ImageView.loadImageWithoutPlaceholder(url: String) {
    ImageHandler.loadImageWithoutPlaceholderAndError( this, url)
}

fun ImageView.loadImageDrawable(@DrawableRes drawableId: Int) {
    this.setImageDrawable(MethodChecker.getDrawable(context, drawableId))
}

fun ImageView.clearImage() {
    ImageHandler.clearImage(this)
}
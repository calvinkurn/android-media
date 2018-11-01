package com.tokopedia.flashsale.management.ekstension

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler

fun ImageView.loadUrl(url: String, radius: Float) {
    ImageHandler.loadImageRounded2(context, this, url, radius)
}

val View.isVisible
    get() = visibility == View.VISIBLE

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}
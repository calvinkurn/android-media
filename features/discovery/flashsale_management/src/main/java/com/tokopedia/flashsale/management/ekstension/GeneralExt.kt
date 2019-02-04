package com.tokopedia.flashsale.management.ekstension

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.AppCompatTextView
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler

fun ImageView.loadUrl(url: String, radius: Float) {
    ImageHandler.loadImageRounded2(context, this, url, radius)
}

fun AppCompatTextView.setTextDrawableColor(color: Int) {
    setTextColor(color)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
        for (drawable: Drawable? in compoundDrawablesRelative) {
            drawable?.mutate()?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    } else {
        for (drawable: Drawable? in compoundDrawables) {
            drawable?.mutate()?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}

val View.isVisible
    get() = visibility == View.VISIBLE

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}
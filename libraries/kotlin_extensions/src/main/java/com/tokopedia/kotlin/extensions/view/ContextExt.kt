package com.tokopedia.kotlin.extensions.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * Created By @ilhamsuaib on 2020-02-22
 */

fun Context.getResColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getResDrawable(@DrawableRes drawable: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawable)
}

fun Context.dpToPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
)

fun Context.pxToDp(px: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        px.toFloat(),
        resources.displayMetrics
)
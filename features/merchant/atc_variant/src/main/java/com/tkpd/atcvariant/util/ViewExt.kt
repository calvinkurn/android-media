package com.tkpd.atcvariant.util

import android.graphics.drawable.GradientDrawable
import android.view.View
import com.tokopedia.unifycomponents.toPx

fun View.setBorderWithColor(
    borderWidth: Int,
    borderRadius: Int,
    borderColor: Int,
    backgroundColor: Int
) {
    val gradientDrawableDefault = GradientDrawable()
    gradientDrawableDefault.setStroke(
        borderWidth.toPx(),
        borderColor
    )
    gradientDrawableDefault.cornerRadius = borderRadius.toPx().toFloat()
    gradientDrawableDefault.setColor(backgroundColor)
    background = gradientDrawableDefault
}
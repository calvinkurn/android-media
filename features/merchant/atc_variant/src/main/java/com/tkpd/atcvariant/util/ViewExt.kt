package com.tkpd.atcvariant.util

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import com.tokopedia.unifycomponents.toPx

fun View.setBorderWithColor(
    borderWidth: Int,
    borderRadius: Int,
    borderColor: String,
    backgroundColor: String
) {
    val gradientDrawableDefault = GradientDrawable()
    gradientDrawableDefault.setStroke(
        borderWidth.toPx(),
        Color.parseColor(borderColor)
    )
    gradientDrawableDefault.cornerRadius = borderRadius.toPx().toFloat()
    gradientDrawableDefault.setColor(Color.parseColor(backgroundColor))
    background = gradientDrawableDefault
}
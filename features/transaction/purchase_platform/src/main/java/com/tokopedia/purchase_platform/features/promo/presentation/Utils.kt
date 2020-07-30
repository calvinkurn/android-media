package com.tokopedia.purchase_platform.features.promo.presentation

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView

const val IMAGE_ALPHA_DISABLED = 128
const val IMAGE_ALPHA_ENABLED = 255

fun setImageFilterGrayScale(imageView: ImageView) {
    val matrix = ColorMatrix()
    matrix.setSaturation(0f)
    val disabledColorFilter = ColorMatrixColorFilter(matrix)
    imageView.colorFilter = disabledColorFilter
    imageView.imageAlpha = IMAGE_ALPHA_DISABLED
}

fun setImageFilterNormal(imageView: ImageView) {
    imageView.colorFilter = null
    imageView.imageAlpha = IMAGE_ALPHA_ENABLED
}

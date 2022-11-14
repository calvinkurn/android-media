package com.tokopedia.mvc.util.extension

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView

private const val VIEW_SATURATION_LEVEL_MIN = 0.09F
private const val VIEW_SATURATION_LEVEL_MAX = 1F

fun ImageView.grayscale() {
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(VIEW_SATURATION_LEVEL_MIN)
    val filter = ColorMatrixColorFilter(colorMatrix)
    this.colorFilter = filter
}

fun ImageView.resetGrayscale() {
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(VIEW_SATURATION_LEVEL_MAX)
    val filter = ColorMatrixColorFilter(colorMatrix)
    this.colorFilter = filter
}

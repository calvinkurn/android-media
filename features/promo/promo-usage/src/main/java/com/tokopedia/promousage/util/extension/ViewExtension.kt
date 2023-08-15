package com.tokopedia.promousage.util.extension

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView

internal fun ImageView.grayscale() {
    val colorMatrix = ColorMatrix().apply {
        setSaturation(0f)
    }
    val colorFilter = ColorMatrixColorFilter(colorMatrix)
    this.colorFilter = colorFilter
}

internal fun ImageView.removeGrayscale() {
    this.colorFilter = null
}

internal var ImageView.isGreyscale: Boolean
    get() = colorFilter != null
    set(value) {
        if (value) {
            this.grayscale()
        } else {
            this.removeGrayscale()
        }
    }

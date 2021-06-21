package com.tokopedia.imagepicker.editor.watermark.utils

import android.graphics.Bitmap
import android.graphics.Matrix

fun createBitmap(width: Int, height: Int): Bitmap {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
}

fun createBitmap(width: Int, height: Int, config: Bitmap.Config): Bitmap {
    return Bitmap.createBitmap(width, height, config)
}

fun Bitmap.createBitmap(axisX: Int = 0, axisY: Int = 0, matrix: Matrix, isFilter: Boolean = true): Bitmap {
    return Bitmap.createBitmap(
        this,
        axisX,
        axisY,
        this.width,
        this.height,
        matrix,
        isFilter
    )
}
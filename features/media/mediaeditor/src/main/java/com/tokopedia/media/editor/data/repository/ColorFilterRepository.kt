package com.tokopedia.media.editor.data.repository

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import javax.inject.Inject

interface ColorFilterRepository {
    fun brightness(value: Float): ColorMatrixColorFilter
    fun contrast(value: Float): ColorMatrixColorFilter
}

class ColorFilterRepositoryImpl @Inject constructor() : ColorFilterRepository {

    override fun brightness(value: Float): ColorMatrixColorFilter {
        val cmB = ColorMatrix()
        cmB.set(
            floatArrayOf(
                1f, 0f, 0f, 0f, value,
                0f, 1f, 0f, 0f, value,
                0f, 0f, 1f, 0f, value,
                0f, 0f, 0f, 1f, 0f
            )
        )
        return ColorMatrixColorFilter(cmB)
    }

    override fun contrast(value: Float): ColorMatrixColorFilter {
        val array = floatArrayOf(
            value, 0f, 0f, 0f, 0f,
            0f, value, 0f, 0f, 0f,
            0f, 0f, value, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
        val matrix = ColorMatrix(array)
        return ColorMatrixColorFilter(matrix)
    }

}
package com.tokopedia.media.editor.data.repository

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import javax.inject.Inject

interface ColorFilterRepository {
    fun brightness(value: Float): ColorMatrixColorFilter
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
}
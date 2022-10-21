package com.tokopedia.media.editor.data.repository

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import com.tokopedia.media.editor.ui.component.BrightnessToolUiComponent
import javax.inject.Inject

interface ColorFilterRepository {
    fun brightness(value: Float): ColorMatrixColorFilter
}

class ColorFilterRepositoryImpl @Inject constructor() : ColorFilterRepository {

    override fun brightness(value: Float): ColorMatrixColorFilter {
        val stdValue = BrightnessToolUiComponent.sliderValueToBrightness(value)
        val cmB = ColorMatrix()
        cmB.set(
            floatArrayOf(
                1f, 0f, 0f, 0f, stdValue,
                0f, 1f, 0f, 0f, stdValue,
                0f, 0f, 1f, 0f, stdValue,
                0f, 0f, 0f, 1f, 0f
            )
        )
        return ColorMatrixColorFilter(cmB)
    }
}
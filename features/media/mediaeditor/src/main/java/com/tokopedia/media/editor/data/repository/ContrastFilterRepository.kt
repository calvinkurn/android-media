package com.tokopedia.media.editor.data.repository

import android.graphics.Bitmap
import com.tokopedia.media.editor.ui.component.ContrastToolsUiComponent
import javax.inject.Inject

interface ContrastFilterRepository {
    fun contrast(value: Float, source: Bitmap): Bitmap?
}

class ContrastFilterRepositoryImpl @Inject constructor(
    private val bitmapCreationRepository: BitmapCreationRepository
) : ContrastFilterRepository {
    /**
     * @param source mutable copy of target bitmap
     * @param value float value for contrast adjustment
     */
    override fun contrast(value: Float, source: Bitmap): Bitmap? {
        val standardContrastValue = ContrastToolsUiComponent.contrastRawToStdValue(value)
        return if (standardContrastValue == 0f) {
            source
        } else {
            val width = source.width
            val height = source.height

            if (bitmapCreationRepository.isBitmapOverflow(width, height)) {
                // return
                null
            } else {
                val pixels = IntArray(width * height)
                source.getPixels(pixels, 0, width, 0, 0, width, height)
                shiftPixel(
                    width = width,
                    height = height,
                    pixels = pixels,
                    tempValue = standardContrastValue
                )
                source.setPixels(pixels, 0, width, 0, 0, width, height)

                // return
                source
            }
        }
    }

    private fun shiftPixel(width: Int, height: Int, pixels: IntArray, tempValue: Float) {
        var red: Float
        var green: Float
        var blue: Float

        for (i in 0 until (width * height)) {
            red = ((pixels[i] shr RED_SHIFT_VALUE) and HEX_STANDARDIZE_VALUE).toFloat()
            green = ((pixels[i] shr GREEN_SHIFT_VALUE) and HEX_STANDARDIZE_VALUE).toFloat()
            blue = (pixels[i] and HEX_STANDARDIZE_VALUE).toFloat()

            red =
                (((((red / CHANNEL_COLOR_MAX_VALUE) - COLOR_STANDARDIZE_VALUE) * tempValue) + COLOR_STANDARDIZE_VALUE) * CHANNEL_COLOR_MAX_VALUE)
            green =
                (((((green / CHANNEL_COLOR_MAX_VALUE) - COLOR_STANDARDIZE_VALUE) * tempValue) + COLOR_STANDARDIZE_VALUE) * CHANNEL_COLOR_MAX_VALUE)
            blue =
                (((((blue / CHANNEL_COLOR_MAX_VALUE) - COLOR_STANDARDIZE_VALUE) * tempValue) + COLOR_STANDARDIZE_VALUE) * CHANNEL_COLOR_MAX_VALUE)

            // validation check
            if (red > CHANNEL_COLOR_MAX_VALUE) {
                red = CHANNEL_COLOR_MAX_VALUE
            } else if (red < CHANNEL_COLOR_MIN_VALUE) {
                red = CHANNEL_COLOR_MIN_VALUE
            }

            if (green > CHANNEL_COLOR_MAX_VALUE) {
                green = CHANNEL_COLOR_MAX_VALUE
            } else if (green < CHANNEL_COLOR_MIN_VALUE) {
                green = CHANNEL_COLOR_MIN_VALUE
            }

            if (blue > CHANNEL_COLOR_MAX_VALUE) {
                blue = CHANNEL_COLOR_MAX_VALUE
            } else if (blue < CHANNEL_COLOR_MIN_VALUE) {
                blue = CHANNEL_COLOR_MIN_VALUE
            }

            val finalRed = red.toInt()
            val finalGreen = green.toInt()
            val finalBlue = blue.toInt()
            pixels[i] = (pixels[i] and ALPHA_COLOR_INT.toInt()) or
                    ((finalRed shl RED_SHIFT_VALUE) and RED_COLOR_INT) or
                    ((finalGreen shl GREEN_SHIFT_VALUE) and GREEN_COLOR_INT) or
                    (finalBlue and BLUE_COLOR_INT)
        }
    }

    companion object {
        private const val HEX_STANDARDIZE_VALUE = 0xFF
        private const val COLOR_STANDARDIZE_VALUE = 0.5f
        private const val CHANNEL_COLOR_MAX_VALUE = 255f
        private const val CHANNEL_COLOR_MIN_VALUE = 0F
        private const val RED_SHIFT_VALUE = 16
        private const val GREEN_SHIFT_VALUE = 8

        private const val RED_COLOR_INT = 0x00FF0000
        private const val GREEN_COLOR_INT = 0x0000FF00
        private const val BLUE_COLOR_INT = 0x000000FF
        private const val ALPHA_COLOR_INT = 0xFF000000
    }
}

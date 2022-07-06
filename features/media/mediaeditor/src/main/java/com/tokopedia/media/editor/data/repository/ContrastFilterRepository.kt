package com.tokopedia.media.editor.data.repository

import android.graphics.Bitmap
import javax.inject.Inject

interface ContrastFilterRepository {
    fun contrast(value: Float, source: Bitmap): Bitmap
}

class ContrastFilterRepositoryImpl @Inject constructor(): ContrastFilterRepository {
    /**
     * @param source mutable copy of target bitmap
     * @param value float value for contrast adjustment
     */
    override fun contrast(value: Float, source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        source.getPixels(pixels, 0, width, 0, 0, width, height)
        shiftPixel(
            width = width,
            height = height,
            pixels = pixels,
            tempValue = value
        )
        source.setPixels(pixels, 0, width, 0, 0, width, height)
        return source
    }

    private fun shiftPixel(width: Int, height: Int, pixels: IntArray, tempValue: Float){
        var red: Float
        var green: Float
        var blue: Float

        var R: Int
        var G: Int
        var B: Int

        for (i in 0 until (width*height)){
            red = ((pixels[i] shr 16) and 0xFF).toFloat()
            green = ((pixels[i] shr 8) and 0xFF).toFloat()
            blue = (pixels[i] and 0xFF).toFloat()

            red = (((((red / 255.0f) - 0.5f) * tempValue) + 0.5f) * 255.0f)
            green = (((((green / 255.0f) - 0.5f) * tempValue) + 0.5f) * 255.0f)
            blue = (((((blue / 255.0f) - 0.5f) * tempValue) + 0.5f) * 255.0f)

            // validation check
            if (red > 255)
                red = 255f
            else if (red < 0)
                red = 0f

            if (green > 255)
                green = 255f
            else if (green < 0)
                green = 0f

            if (blue > 255)
                blue = 255f
            else if (blue < 0)
                blue = 0f

            R = red.toInt()
            G = green.toInt()
            B = blue.toInt()
            pixels[i] = (pixels[i] and 0xFF000000.toInt()) or
                    ((R shl 16) and 0x00FF0000) or
                    ((G shl 8) and 0x0000FF00) or
                    (B and 0x000000FF)
        }
    }
}
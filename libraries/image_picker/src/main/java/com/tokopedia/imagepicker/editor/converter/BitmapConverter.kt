package com.tokopedia.imagepicker.editor.converter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.tokopedia.imagepicker.editor.converter.config.ConverterConfig

object BitmapConverter {

    private fun toBitmap(view: View, config: ConverterConfig): Bitmap {
        view.measure(config.canvasConfig.width.specSize, config.canvasConfig.height.specSize)
        return Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, config.bitmapConfig)
            .also {
                val canvas = Canvas(it)
                canvas.drawColor(config.canvasConfig.color)
                view.layout(0, 0, view.measuredWidth, view.measuredHeight)
                view.draw(canvas)
            }
    }

    @JvmOverloads
    fun toBitmap(view: View, configPatch: ConverterConfig.() -> Unit = {}): Bitmap {
        return toBitmap(view, ConverterConfig().apply(configPatch))
    }

    @JvmStatic
    fun toBitmapWithBackgroundColor(view: View, backgroundColor: Int): Bitmap {
        return toBitmap(view, ConverterConfig().apply {
            this.canvasConfig.color = backgroundColor
        })
    }

}
package com.tokopedia.rechargeocr

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.tokopedia.utils.image.ImageUtil

class RechargeCameraUtil {

    companion  object {

        private const val LEFT_DIMEN_DIVIDER = 20
        private const val TOP_DIMEN_DIVIDER = 3.22
        private const val RIGHT_DIMEN_DIVIDER = 20
        private const val BOTTOM_DIMEN_DIVIDER = 2.96

        @JvmStatic
        fun trimBitmap(imagePath: String): String {
            val bitmapToEdit = ImageUtil.getBitmapFromPath(imagePath, ImageUtil.DEF_WIDTH,
                    ImageUtil.DEF_HEIGHT, false)
            val width = bitmapToEdit!!.width
            val height = bitmapToEdit.height
            var left = 0
            var right = width
            var top = 0
            var bottom = height

            val isPng = ImageUtil.isPng(imagePath)

            var outputBitmap: Bitmap? = null
            try {
                val newLeft = left + (right - left) / LEFT_DIMEN_DIVIDER
                val newTop = (top + (bottom - top) / TOP_DIMEN_DIVIDER).toInt()
                val newRight = right - (right - left) / RIGHT_DIMEN_DIVIDER
                val newBottom = (bottom - (bottom - top) / BOTTOM_DIMEN_DIVIDER).toInt()

                val expectedWidth = newRight - newLeft
                val expectedHeight = newBottom - newTop

                outputBitmap = Bitmap.createBitmap(expectedWidth, expectedHeight, bitmapToEdit.config)
                val canvas = Canvas(outputBitmap!!)
                canvas.drawBitmap(bitmapToEdit, Rect(newLeft, newTop, newRight, newBottom),
                        Rect(0, 0, expectedWidth, expectedHeight), null)
                val file = ImageUtil.writeImageToTkpdPath(outputBitmap, isPng)
                bitmapToEdit.recycle()
                outputBitmap.recycle()
                System.gc()

                return file.absolutePath
            } catch (e: Throwable) {
                if (outputBitmap != null && !outputBitmap.isRecycled) {
                    outputBitmap.recycle()
                }
                return imagePath
            }

        }
    }
}
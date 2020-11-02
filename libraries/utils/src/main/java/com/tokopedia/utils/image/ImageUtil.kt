package com.tokopedia.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Point
import android.net.Uri
import android.view.Display
import android.view.WindowManager
import java.io.File
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

object ImageUtil {
    /**
     * This method calculates maximum size of both width and height of bitmap.
     * It is twice the device screen diagonal for default implementation (extra quality to zoom image).
     * Size cannot exceed max texture size.
     *
     * @return - max bitmap size in pixels.
     */
    private fun calculateMaxBitmapSize(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display
        val width: Int
        val height: Int
        val size = Point()
        display = wm.defaultDisplay
        display.getSize(size)
        width = size.x
        height = size.y
        // Twice the device screen diagonal as default
        var maxBitmapSize = sqrt(width.toDouble().pow(2.0) + height.toDouble().pow(2.0)).toInt()
        // Check for max texture size via Canvas
        val canvas = Canvas()
        val maxCanvasSize: Int = min(canvas.maximumBitmapWidth, canvas.maximumBitmapHeight)
        if (maxCanvasSize > 0) {
            maxBitmapSize = min(maxBitmapSize, maxCanvasSize)
        }
        return maxBitmapSize
    }

    // This will handle OOM for too large Bitmap
    @JvmStatic
    fun getBitmapFromFile(context: Context, imagePath: String): Bitmap? {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        val maxBitmapSize = calculateMaxBitmapSize(context)
        options.inSampleSize = calculateInSampleSize(options, maxBitmapSize, maxBitmapSize)
        options.inJustDecodeBounds = false
        var tempPic: Bitmap? = null
        var decodeAttemptSuccess = false
        while (!decodeAttemptSuccess) {
            try {
                tempPic = BitmapFactory.decodeFile(imagePath, options)
                decodeAttemptSuccess = true
            } catch (error: OutOfMemoryError) {
                options.inSampleSize *= 2
            }
        }
        return tempPic
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int { // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width lower or equal to the requested height and width.
        if (height > reqHeight || width > reqWidth) {
            while (height / inSampleSize > reqHeight || width / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun getWidthAndHeight(file: File): Pair<Int, Int> {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)
            options.outWidth to options.outHeight
        } catch (e:Exception) {
            0 to 0
        }
    }

    /**
     * This function is to determine if the image from file should be load as fit center or center crop
     * If the ratio between width/height is too big, it should be viewed as fit center. Otherwise, it will OOM
     */
    @JvmStatic
    fun shouldLoadFitCenter(file:File) : Boolean {
        val (width, height) = getWidthAndHeight(file)
        val min: Int
        val max: Int
        if (width > height) {
            min = height
            max = width
        } else {
            min = width
            max = height
        }
        return min != 0 && max / min > 2
    }

    @JvmStatic
    fun getWidthAndHeight(context: Context, uri: Uri): Pair<Int, Int> {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val input = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(input, null, options)
            input?.close()
            options.outWidth to options.outHeight
        } catch (ignored: Exception) {
            0 to 0
        }
    }
}
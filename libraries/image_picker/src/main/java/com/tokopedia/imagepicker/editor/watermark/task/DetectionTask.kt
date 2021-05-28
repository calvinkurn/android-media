package com.tokopedia.imagepicker.editor.watermark.task

import android.graphics.Bitmap
import com.tokopedia.imagepicker.editor.watermark.listener.DetectFinishListener
import com.tokopedia.imagepicker.editor.watermark.uimodel.DetectionReturnValue
import com.tokopedia.imagepicker.editor.watermark.utils.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.ceil
import kotlin.math.min

class DetectionTask {

    fun execute(
        bitmap: Bitmap?,
        listener: DetectFinishListener
    ): DetectionReturnValue? {
        if (bitmap == null) {
            listener.onError("ERROR_BITMAP_NULL")
            return null
        }

        if (bitmap.width > MAX_IMAGE_SIZE || bitmap.height > MAX_IMAGE_SIZE) {
            listener.onError("WARNING_BIG_IMAGE")
            return null
        }

        val result = DetectionReturnValue()
        val pixels = BitmapUtils.getBitmapPixels(bitmap)

        if (pixels.size < CHUNK_SIZE) {
            val watermarkRGB = BitmapUtils.pixel2ARGBArray(pixels)
            val watermarkArray = StringUtils.copyFromIntArray(watermarkRGB)

            FastDctFft.transform(watermarkArray)
        } else {
            val numOfChunks = ceil((pixels.size / CHUNK_SIZE).toDouble()).toInt()

            for (i in 0..numOfChunks) {
                val start = i * CHUNK_SIZE
                val length = min(pixels.size - start, CHUNK_SIZE)
                val temp = IntArray(length)

                System.arraycopy(pixels, start, temp, 0, length)

                val source = BitmapUtils.pixel2ARGBArray(temp)
                val colorTempArray = StringUtils.copyFromIntArray(source)

                FastDctFft.transform(colorTempArray)
            }
        }

        return result
    }

    companion object : CoroutineScope {

        private val job = SupervisorJob()

        override val coroutineContext: CoroutineContext
            get() = job + Dispatchers.IO

        fun post(bitmap: Bitmap?, listener: DetectFinishListener) {
            launch {
                val result = DetectionTask().execute(bitmap, listener)

                withContext(Dispatchers.Main) {
                    if (result != null) {
                        listener.onSuccess(result)
                    } else {
                        listener.onError("ERROR_DETECT_FAILED")
                    }
                }
            }
        }

    }

}
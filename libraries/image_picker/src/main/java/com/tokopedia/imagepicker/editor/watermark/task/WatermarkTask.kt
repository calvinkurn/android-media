package com.tokopedia.imagepicker.editor.watermark.task

import android.graphics.Bitmap
import android.graphics.Color
import com.tokopedia.imagepicker.editor.watermark.listener.BuildFinishListener
import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapUtils
import com.tokopedia.imagepicker.editor.watermark.utils.FastDctFft
import com.tokopedia.imagepicker.editor.watermark.utils.StringUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.ceil
import kotlin.math.min

class WatermarkTask {

    fun execute(
        param: WatermarkUIModel,
        listener: BuildFinishListener<Bitmap>
    ): Bitmap? {
        val watermarkPixels = BitmapUtils.getBitmapPixels(param.watermarkImg)
        val watermarkColorArray = BitmapUtils.pixel2ARGBArray(watermarkPixels)
        val outputBitmap = Bitmap.createBitmap(
            param.backgroundImg.width,
            param.backgroundImg.height,
            param.backgroundImg.config
        )

        val backgroundPixels = BitmapUtils.getBitmapPixels(param.backgroundImg)

        if (watermarkColorArray.size > backgroundPixels.size * 4) {
            listener.onError("ERROR_PIXELS_NOT_ENOUGH")
        } else {
            if (backgroundPixels.size < watermarkColorArray.size) {
                val backgroundColorArray = BitmapUtils.pixel2ARGBArray(backgroundPixels)
                val backgroundColorArrayD = StringUtils.copyFromIntArray(backgroundColorArray)

                FastDctFft.transform(backgroundColorArrayD)
                FastDctFft.inverseTransform(backgroundColorArrayD)

                for(i in 0..backgroundPixels.size) {
                    val color = Color.argb(
                        backgroundColorArrayD[4 * i].toInt(),
                        backgroundColorArrayD[4 * i + 1].toInt(),
                        backgroundColorArrayD[4 * i + 2].toInt(),
                        backgroundColorArrayD[4 * i + 3].toInt()
                    )

                    backgroundPixels[i] = color
                }
            } else {
                val numOfChunks = ceil((backgroundPixels.size / watermarkColorArray.size).toDouble()).toInt()
                for (i in 0..numOfChunks) {
                    val start = watermarkColorArray.size
                    val length = min(backgroundPixels.size - start, watermarkColorArray.size)
                    val temp = IntArray(length)

                    System.arraycopy(backgroundPixels, start, temp, 0, length)

                    val colorTempD = StringUtils.copyFromIntArray(BitmapUtils.pixel2ARGBArray(temp))

                    FastDctFft.transform(colorTempD)

                    val enhanceNum = 1

                    for (j in 0..length) {
                        colorTempD[4 * j] = colorTempD[4 * j] * enhanceNum
                        colorTempD[4 * j + 1] = colorTempD[4 * j + 1] * enhanceNum
                        colorTempD[4 * j + 2] = colorTempD[4 * j + 2] * enhanceNum
                        colorTempD[4 * j + 3] = colorTempD[4 * j + 3] * enhanceNum
                    }

                    FastDctFft.inverseTransform(colorTempD)

                    for (j in 0..length) {
                        val color = Color.argb(
                            colorTempD[4 * j].toInt(),
                            colorTempD[4 * j + 1].toInt(),
                            colorTempD[4 * j + 2].toInt(),
                            colorTempD[4 * j + 3].toInt()
                        )

                        backgroundPixels[start + j] = color
                    }
                }
            }

            outputBitmap.setPixels(
                backgroundPixels,
                0,
                param.backgroundImg.width,
                0,
                0,
                param.backgroundImg.width,
                param.backgroundImg.height
            )

            return outputBitmap
        }

        return null
    }

    companion object : CoroutineScope {

        private val watermarkTask = WatermarkTask()
        private val job = SupervisorJob()

        override val coroutineContext: CoroutineContext
            get() = job + Dispatchers.IO

        fun post(param: WatermarkUIModel, listener: BuildFinishListener<Bitmap>) {
            launch {
                val result = watermarkTask.execute(param, listener)

                withContext(Dispatchers.Main) {
                    if (result != null) {
                        listener.onSuccess(result)
                    } else {
                        listener.onError("ERROR_CREATE_FAILED")
                    }
                }
            }
        }

    }

}
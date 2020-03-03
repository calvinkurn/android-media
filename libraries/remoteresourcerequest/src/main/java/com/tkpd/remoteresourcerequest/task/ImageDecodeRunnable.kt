package com.tkpd.remoteresourcerequest.task

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import kotlin.math.max


internal class ImageDecodeRunnable(
        private val task: TaskDecodeProperties
) : Runnable {

    interface TaskDecodeProperties {
        val mTargetWidth: Int
        val mTargetHeight: Int
        fun getByteBuffer(): ByteArray
        fun setCurrentThread(thread: Thread?)
        fun setImage(image: Bitmap)
        fun handleDecodeState(state: Int)
        fun getDisplayMetrics(): DisplayMetrics
    }

    override fun run() {

        task.setCurrentThread(Thread.currentThread())

        val imageBuffer = task.getByteBuffer()

        var returnBitmap: Bitmap? = null

        task.handleDecodeState(DECODE_STATE_STARTED)

        val bitmapOptions = BitmapFactory.Options()
        var targetWidth = task.mTargetWidth
        var targetHeight = task.mTargetHeight

        if (Thread.interrupted()) {
            return
        }
        bitmapOptions.inJustDecodeBounds = true
        BitmapFactory
                .decodeByteArray(imageBuffer, 0, imageBuffer.size, bitmapOptions)

        val metrics = task.getDisplayMetrics()

        if (targetHeight == 0) {
            targetHeight = metrics.heightPixels
        }
        if (targetWidth == 0) {
            targetWidth = metrics.widthPixels
        }
        val hScale = bitmapOptions.outHeight / targetHeight

        val wScale = bitmapOptions.outWidth / targetWidth

        val sampleSize = max(hScale, wScale)

        if (sampleSize > 1) {
            bitmapOptions.inSampleSize = sampleSize
        }

        if (Thread.interrupted()) {
            return
        }

        bitmapOptions.inJustDecodeBounds = false
        var i = 0
        while (i < NUMBER_OF_DECODE_TRIES) {
            try {
                returnBitmap = BitmapFactory.decodeByteArray(
                        imageBuffer, 0, imageBuffer.size,
                        bitmapOptions
                )
                break
            } catch (e: Exception) {
                System.gc()
                if (Thread.interrupted()) {
                    return
                }
                try {
                    Thread.sleep(SLEEP_TIME_MILLISECONDS)
                } catch (e: Exception) {
                    return
                }
            } finally {
                if (null == returnBitmap) {
                    task.handleDecodeState(DECODE_STATE_FAILED)
                } else {
                    task.setImage(returnBitmap)
                    task.handleDecodeState(DECODE_STATE_COMPLETED)
                }
                task.setCurrentThread(null)
                Thread.interrupted()

            }
            i++
        }
    }

    companion object {
        const val DECODE_STATE_FAILED = 0
        const val DECODE_STATE_STARTED = 1
        const val DECODE_STATE_COMPLETED = 2

        private const val NUMBER_OF_DECODE_TRIES = 2

        private const val SLEEP_TIME_MILLISECONDS = 100L
    }
}
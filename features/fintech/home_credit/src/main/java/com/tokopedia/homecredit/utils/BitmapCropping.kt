package com.tokopedia.homecredit.utils

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.otaliastudios.cameraview.CameraView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.unifycomponents.toPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt


interface BitmapProcessingListener {
    fun onBitmapReady(bitmap: Bitmap)
    fun onFailed(originalBitmap: Bitmap, throwable: Throwable)
}

class BitmapCropping constructor(
    private val context: Context,
    private val camera: CameraView
) : CoroutineScope {

    private val parentJob = SupervisorJob()
    private val coroutineDispatchers = CoroutineDispatchersProvider
    private val localScope = CoroutineScope(coroutineDispatchers.io + parentJob)

    override val coroutineContext: CoroutineContext
        get() = localScope.coroutineContext

    fun doCropping(bitmap: Bitmap, frame: View, listener: BitmapProcessingListener) {
        launchCatchError(coroutineContext, block = {
            val bitmapCropped = doCroppingWithFrame(bitmap, frame, 0)

            withContext(coroutineDispatchers.main) {
                listener.onBitmapReady(bitmapCropped)
            }
        }, onError = {
            withContext(coroutineDispatchers.main) {
                listener.onFailed(bitmap, it)
            }
        })
    }

    /**
     * @param bitmap
     * @param frame is cropping guidelines
     * @param padding to add padding between frame and final cropping result
     * @return cropped bitmap
     */
    private fun doCroppingWithFrame(bitmap: Bitmap, frame: View, padding: Int = DEFAULT_PADDING): Bitmap {
        val framePadding = padding.dpToPx(context.resources.displayMetrics)

        val bitmapW = bitmap.width.toFloat()
        val bitmapH = bitmap.height.toFloat()

        val diffScaleW = camera.width.toFloat() / bitmapW
        val diffScaleH = camera.height.toFloat() / bitmapH

        val frameW = if (frame.width.toFloat() > bitmapW) bitmapW else frame.width.toFloat()
        val frameH = if (frame.height.toFloat() > bitmapH) bitmapH else frame.height.toFloat()
        val frameLeft = if (frame.left.toFloat() < 0) 0F else frame.left.toFloat()
        val frameTop = if (frame.top.toFloat() < 0) 0F else frame.top.toFloat()

        var offsetX: Float = (frameLeft / diffScaleW) - (camera.left.toFloat() / diffScaleW) - framePadding
        offsetX = if (offsetX > bitmapW) frameLeft else if (offsetX < 0) 0F else offsetX
        var offsetY: Float = (frameTop / diffScaleH) - (camera.top.toFloat() / diffScaleH) - framePadding
        offsetY = if (offsetY > bitmapH) frameTop else if (offsetY < 0) 0F else offsetY

        var newW: Float = (frameW / diffScaleW) + (framePadding * 2).toFloat() // 2 for padding on 2 side (left & right)
        newW = when {
            newW > bitmapW -> bitmapW
            newW + offsetX > bitmapW -> newW - offsetX
            else -> newW
        }

        var newH: Float = (frameH / diffScaleH) + (framePadding * 2).toFloat() // 2 for padding on 2 side (top & bottom)
        newH = when {
            newH > bitmapH -> bitmapH
            newH + offsetY > bitmapH -> newH - offsetY
            else -> newH
        }

//        return Bitmap.createBitmap(
//            bitmap,
//            offsetX.roundToInt(),
//            offsetY.roundToInt(),
//            newW.roundToInt(),
//            newH.roundToInt()
//        )

        val ratioW = bitmap.width / camera.width
        val ratioH = bitmap.height / camera.height

        val framePos = IntArray(2)
        frame.getLocationInWindow(framePos)

        return Bitmap.createBitmap(
            bitmap,
            (bitmap.width / 2) - (frame.width * ratioW),
            (bitmap.height / 2) - (frame.height * ratioH),
            frame.width * ratioW,
            frame.height * ratioH
        )
    }

    /**
     * @param bitmap
     * @param quality bitmap compress to X quality of original source
     * @return compressed bitmap
     */
    private fun doCompression(bitmap: Bitmap, quality: Float): Bitmap {
        val reduceQualityPercentage = quality / 100
        val width = (bitmap.width * reduceQualityPercentage).roundToInt()
        val height = (bitmap.height * reduceQualityPercentage).roundToInt()

        if (width <= 0) throw Throwable("Please make sure final width > 0")
        if (height <= 0) throw Throwable("Please make sure final height > 0")

        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    companion object {
        const val DEFAULT_PADDING = 8
    }
}

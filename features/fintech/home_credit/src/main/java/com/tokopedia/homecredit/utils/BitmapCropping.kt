package com.tokopedia.homecredit.utils

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.otaliastudios.cameraview.CameraView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
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
    private val context: Context
) : CoroutineScope {

    private val parentJob = SupervisorJob()
    private val coroutineDispatchers = CoroutineDispatchersProvider
    private val localScope = CoroutineScope(coroutineDispatchers.io + parentJob)

    override val coroutineContext: CoroutineContext
        get() = localScope.coroutineContext

    fun doCropping(bitmap: Bitmap, frame: View, listener: BitmapProcessingListener) {
        launchCatchError(coroutineContext, block = {
            val bitmapCropped = doCroppingWithFrame(bitmap, frame)

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
    private fun doCroppingWithFrame(bitmap: Bitmap, frame: View): Bitmap {
        val framePos = IntArray(2)
        frame.getLocationOnScreen(framePos)

        val x = ((bitmap.width - getScreenWidth()) / 2) + framePos[0]
        val y = ((bitmap.height - getScreenHeight()) / 2) + framePos[1] - DisplayMetricUtils.getStatusBarHeight(context)
        val w = getScreenWidth() - FRAME_MARGIN.toPx()
        val h = w * 2 / 3

        return Bitmap.createBitmap(
            bitmap,
            x,
            y,
            w,
            h
        )
    }

    companion object {
        const val FRAME_MARGIN = 48
    }
}

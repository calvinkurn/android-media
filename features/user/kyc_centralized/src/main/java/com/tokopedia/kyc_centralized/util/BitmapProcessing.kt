package com.tokopedia.kyc_centralized.util

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.otaliastudios.cameraview.CameraView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KycServerLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

interface BitmapProcessingListener {
    fun onBitmapReady(bitmap: Bitmap)
    fun onFailed(originalBitmap: Bitmap, throwable: Throwable)
}

class BitmapCroppingAndCompression constructor(
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
            val startTime = System.currentTimeMillis()
            val bitmapCropped = doCroppingWithFrame(bitmap, frame)
            val croppingTimeProcessTime = System.currentTimeMillis() - startTime

            withContext(coroutineDispatchers.main) {
                listener.onBitmapReady(bitmapCropped)

                KycServerLogger.kycCropAndCompression(true,
                        bitmapOriginal = bitmap,
                        bitmapCropped = bitmapCropped,
                        bitmapCompressed = null,
                        bitmapFinal = bitmapCropped,
                        croppingTimeProcess = croppingTimeProcessTime,
                        compressionTimeProcess = -1L,
                        compressionQuality = 100F
                )
            }
        }, onError = {
            withContext(coroutineDispatchers.main) {
                listener.onFailed(bitmap, it)
                KycServerLogger.kycCropAndCompression(isSuccess = false, throwable = it)
            }
        })
    }

    fun doCompression(bitmap: Bitmap, listener: BitmapProcessingListener) {
        launchCatchError(coroutineContext, block = {
            val startTime = System.currentTimeMillis()
            var compressionTimeProcess = -1L
            val quality = getQualityRedcution(bitmap)
            var bitmapCompressed = bitmap
            if (quality != 100F) {
                bitmapCompressed = doCompression(bitmap, quality)
                compressionTimeProcess = System.currentTimeMillis() - startTime
            }

            withContext(coroutineDispatchers.main) {
                listener.onBitmapReady(bitmapCompressed)

                KycServerLogger.kycCropAndCompression(true,
                        bitmapOriginal = bitmap,
                        bitmapCropped = null,
                        bitmapCompressed = bitmapCompressed,
                        bitmapFinal = bitmapCompressed,
                        croppingTimeProcess = -1L,
                        compressionTimeProcess = compressionTimeProcess,
                        compressionQuality = 100F
                )
            }
        }, onError = {
            withContext(coroutineDispatchers.main) {
                listener.onFailed(bitmap, it)
                KycServerLogger.kycCropAndCompression(isSuccess = false, throwable = it)
            }
        })
    }

    fun doCropAndCompress(bitmap: Bitmap, frame: View, listener: BitmapProcessingListener) {
        launchCatchError(coroutineContext, {
            val startTime = System.currentTimeMillis()
            val bitmapCropped = doCroppingWithFrame(bitmap, frame)
            val croppingTimeProcessTime = System.currentTimeMillis() - startTime

            var bitmapCompressed: Bitmap? = null
            var bitmapFinal: Bitmap? = bitmapCropped
            val quality = getQualityRedcution(bitmapCropped)
            var compressionTimeProcess = -1L

            if (quality != 100F) {
                bitmapCompressed = doCompression(bitmapCropped, quality)
                bitmapFinal = bitmapCompressed
                compressionTimeProcess = System.currentTimeMillis() - startTime
            }

            withContext(Dispatchers.Main) {
                if (bitmapFinal != null) {
                    listener.onBitmapReady(bitmapFinal)

                    KycServerLogger.kycCropAndCompression(true,
                            bitmapOriginal = bitmap,
                            bitmapCropped = bitmapCropped,
                            bitmapCompressed = bitmapCompressed,
                            bitmapFinal = bitmapFinal,
                            croppingTimeProcess = croppingTimeProcessTime,
                            compressionTimeProcess = compressionTimeProcess,
                            compressionQuality = quality
                    )
                }
            }
        }, {
            withContext(coroutineDispatchers.main) {
                KycServerLogger.kycCropAndCompression(isSuccess = false, throwable = it)
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

        return Bitmap.createBitmap(
                bitmap,
                offsetX.roundToInt(),
                offsetY.roundToInt(),
                newW.roundToInt(),
                newH.roundToInt()
        )
    }

    /**
     * @param bitmap
     * @return quality reduction
     */
    private fun getQualityRedcution(bitmap: Bitmap): Float {
        val size = calculateSize(bitmap) / MB_DIVIDER
        return when {
            size > KYCConstant.MB_2 && size < KYCConstant.MB_3 -> { KYCConstant.QUALITY_70 }
            size > KYCConstant.MB_3 && size < KYCConstant.MB_6 -> { KYCConstant.QUALITY_50 }
            size > KYCConstant.MB_6 && size < KYCConstant.MB_10 -> { KYCConstant.QUALITY_40 }
            size > KYCConstant.MB_10 && size < KYCConstant.MB_15 -> { KYCConstant.QUALITY_30 }
            size > KYCConstant.MB_15 -> { KYCConstant.QUALITY_20 }
            else -> { KYCConstant.QUALITY_100 }
        }
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

    /**
     * @return size in MB
     */
    private fun calculateSize(bitmap: Bitmap): Float {
        return try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val result = stream.size().toFloat() / MB_DIVIDER_FLOAT
            stream.flush()
            stream.close()
            result
        } catch (e: Exception) {
            0F
        }
    }

    companion object {
        const val DEFAULT_PADDING = 8

        private const val MB_DIVIDER = 1024
        private const val MB_DIVIDER_FLOAT = 1024F
    }
}

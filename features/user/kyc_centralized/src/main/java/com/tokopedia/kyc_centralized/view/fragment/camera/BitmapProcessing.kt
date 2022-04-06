package com.tokopedia.kyc_centralized.view.fragment.camera

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.otaliastudios.cameraview.CameraView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

interface BitmapProcessingListener {
    fun onBitmapReady(bitmap: Bitmap)
    fun onFailed(throwable: Throwable)
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

    fun doCropping(bitmap: Bitmap, frame: View, padding: Int = DEFAULT_PADDING, listener: BitmapProcessingListener) {
        launchCatchError(coroutineContext, block = {
            val startTime = System.currentTimeMillis()
            val bitmapCropped = doCroppingWithFrame(bitmap, frame)
            val croppingTimeProcessTime = System.currentTimeMillis() - startTime

            withContext(coroutineDispatchers.main) {
                listener.onBitmapReady(bitmapCropped)

                sendLog(true,
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
                listener.onFailed(it)
                sendLog(isSuccess = false, throwable = it)
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

                sendLog(true,
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
                listener.onFailed(it)
                sendLog(isSuccess = false, throwable = it)
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

                    sendLog(true,
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
                sendLog(isSuccess = false, throwable = it)
                listener.onFailed(it)
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
        val size = calculateSize(bitmap) / 1024
        return when {
            size > 2 && size < 3 -> { 70F }
            size > 3 && size < 6 -> { 50F }
            size > 6 && size < 10 -> { 40F }
            size > 10 && size < 15 -> { 30F }
            size > 15 -> { 20F }
            else -> { 100F }
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
            val result = stream.size().toFloat() / 1024F
            stream.flush()
            stream.close()
            result
        } catch (e: Exception) {
            e.printStackTrace()
            0F
        }
    }

    private fun sendLog(
            isSuccess: Boolean,
            bitmapOriginal: Bitmap? = null,
            bitmapCropped: Bitmap? = null,
            bitmapCompressed: Bitmap? = null,
            bitmapFinal: Bitmap? = null,
            croppingTimeProcess: Long? = 0L,
            compressionTimeProcess: Long? = 0L,
            compressionQuality: Float = 0F,
            throwable: Throwable? = null
    ) {
        val log = mapOf(
                "status" to if (isSuccess) "Success" else "Failed",
                "originalSizeInKb" to "${bitmapOriginal?.let { calculateSize(it) }}",
                "originalResolution" to "${bitmapOriginal?.width}x${bitmapOriginal?.height}",
                "croppedSizeInKb" to "${bitmapCropped?.let { calculateSize(it) }}",
                "croppedResolution" to "${bitmapCropped?.width}x${bitmapCropped?.height}",
                "compressedSizeInKb" to "${bitmapCompressed?.let { calculateSize(it) }}",
                "compressedResolution" to "${bitmapCompressed?.width}x${bitmapCompressed?.height}",
                "compressionQualityPercentage" to compressionQuality.toString(),
                "finalResolution" to "${bitmapFinal?.width}x${bitmapFinal?.height}",
                "croppingTimeProcessInMs" to "$croppingTimeProcess",
                "compressionTimeProcessInMs" to "$compressionTimeProcess",
                "processTimeInMs" to "${croppingTimeProcess.orZero() + compressionTimeProcess.orZero()}",
                "stackTrace" to throwable?.message.toString()
        )

        ServerLogger.log(Priority.P2, TAG_LOG_CROP_AND_COMPRESSION_KYC, log)
        Timber.d("$TAG_LOG_CROP_AND_COMPRESSION_KYC | log:\n${log}")
    }

    companion object {
        private const val TAG_LOG_CROP_AND_COMPRESSION_KYC = "KYC_CROP_AND_COMPRESSION"
        const val DEFAULT_PADDING = 32
    }
}
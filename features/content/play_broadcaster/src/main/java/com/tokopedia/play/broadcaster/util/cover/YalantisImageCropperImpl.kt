package com.tokopedia.play.broadcaster.util.cover

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.AsyncTask
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.image.ImageProcessingUtil.getCompressFormat
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.model.CropParameters
import com.yalantis.ucrop.model.ExifInfo
import com.yalantis.ucrop.model.ImageState
import com.yalantis.ucrop.task.BitmapCropTask
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

/**
 * Created by jegul on 18/06/20
 */
class YalantisImageCropperImpl(private val mContext: Context) : YalantisImageCropper {

    override suspend fun cropImage(inputPath: String, cropRect: RectF, currentRect: RectF, currentScale: Float, currentAngle: Float, exifInfo: ExifInfo, viewBitmap: Bitmap): Uri = suspendCancellableCoroutine {
        val imageOutputDirectory = ImageProcessingUtil.getTokopediaPhotoPath(inputPath.getCompressFormat())
        val imageState = ImageState(cropRect, currentRect,
                currentScale, currentAngle)
        val cropParams = CropParameters(ImageProcessingUtil.DEF_WIDTH, ImageProcessingUtil.DEF_HEIGHT,
                DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY,
                inputPath, imageOutputDirectory.absolutePath, exifInfo)

        val bitmapCropCallback = object : BitmapCropCallback {
            override fun onBitmapCropped(resultUri: Uri, offsetX: Int, offsetY: Int, imageWidth: Int, imageHeight: Int) {
                it.resume(resultUri, onCancellation = {})
            }

            override fun onCropFailure(t: Throwable) {
                it.resumeWithException(t)
            }
        }

        BitmapCropTask(mContext, viewBitmap, imageState, cropParams, bitmapCropCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    companion object {
        private val DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG
        private const val DEFAULT_COMPRESS_QUALITY = 90
    }

}
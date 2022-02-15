package com.tokopedia.media.picker.utils.files

import android.graphics.Bitmap
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File

object FileGenerator {

    fun createFileVideoRecorder(): File {
        val uniqueFileName = FileUtil.generateUniqueFileName()
        val currentTimeInMillis = System.currentTimeMillis()

        val internalAppCacheDir = FileUtil
            .getTokopediaInternalDirectory(ImageProcessingUtil.DEFAULT_DIRECTORY)
            .absolutePath

        return File(
            internalAppCacheDir,
            "VID_${uniqueFileName}_${currentTimeInMillis}.mp4"
        )
    }

    fun createFileCameraCapture(captureSize: Size?, byteArray: ByteArray, invoke: (File?) -> Unit) {
        val compressFormat = Bitmap.CompressFormat.JPEG
        val nativeCaptureSize = captureSize?: return

        try {
            CameraUtils.decodeBitmap(
                byteArray,
                nativeCaptureSize.width,
                nativeCaptureSize.height
            ) {
                if (it != null) {
                    invoke(ImageProcessingUtil.writeImageToTkpdPath(it, compressFormat))
                }
            }
        } catch (e: Throwable) {
            invoke(ImageProcessingUtil.writeImageToTkpdPath(byteArray, compressFormat))
        }
    }

}
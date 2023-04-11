package com.tokopedia.media.picker.data.repository

import android.graphics.Bitmap
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

interface CreateMediaRepository {
    fun image(captureSize: Size?, byteArray: ByteArray?): Flow<File?>
    fun video(): File?
}

class CreateMediaRepositoryImpl @Inject constructor() : CreateMediaRepository {

    override fun image(captureSize: Size?, byteArray: ByteArray?): Flow<File?> {
        return flow {
            val compressFormat = Bitmap.CompressFormat.JPEG
            val nativeCaptureSize = captureSize?: return@flow
            val imageByteData = byteArray?: return@flow

            try {
                val bitmap = CameraUtils.decodeBitmap(
                    imageByteData,
                    nativeCaptureSize.width,
                    nativeCaptureSize.height
                )?: return@flow

                // if process save is change, please check editor utils getTokopediaCacheDir() for path checker
                emit(ImageProcessingUtil.writeImageToTkpdPath(bitmap, compressFormat))
            } catch (e: Throwable) {
                emit(ImageProcessingUtil.writeImageToTkpdPath(byteArray, compressFormat))
            }
        }
    }

    override fun video(): File {
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

}

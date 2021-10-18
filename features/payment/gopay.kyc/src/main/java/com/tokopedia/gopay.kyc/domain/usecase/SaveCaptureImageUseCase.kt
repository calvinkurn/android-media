package com.tokopedia.gopay.kyc.domain.usecase

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import com.otaliastudios.cameraview.CameraUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gopay.kyc.domain.data.CameraImageResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class SaveCaptureImageUseCase @Inject constructor(
        @ApplicationContext val context: Context,
) : UseCase<CameraImageResult>() {

    fun parseAndSaveCapture(
            onSuccess: (CameraImageResult) -> Unit,
            onError: (Throwable) -> Unit,
            imageByte: ByteArray,
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(PARAM_IMAGE, imageByte)
        }
        execute({ onSuccess(it) }, { onError(it) }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): CameraImageResult {
        val imageByte = (useCaseRequestParams.getObject(PARAM_IMAGE) as ByteArray)
        return generateImage(imageByte)
    }

    private fun generateImage(imageByte: ByteArray): CameraImageResult {
        val compressedBitmap = CameraUtils.decodeBitmap(imageByte, MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION)
        val compressedByteArray = bitmapToByteArray(compressedBitmap)
        val cameraResultFile = saveToCacheDirectory(compressedByteArray)
        return CameraImageResult(
                compressedBitmap?.width ?: 0,
                compressedBitmap?.height ?: 0,
                cameraResultFile?.absolutePath,
                ArrayList(compressedByteArray.toList())
        )
    }

    private fun saveToCacheDirectory(byteArray: ByteArray): File? {
        var out: FileOutputStream? = null
        return try {
            val file = getFileLocationFromDirectory()
            out = FileOutputStream(file)
            out.write(byteArray)
            file
        } catch (e: Exception) {
            null
        } finally {
            out?.let {
                out.flush()
                out.close()
            }
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
        try {
            val stream = ByteArrayOutputStream()
            bitmap?.compress(
                    Bitmap.CompressFormat.JPEG,
                    IMAGE_QUALITY,
                    stream
            )
            return stream.toByteArray()
        } finally {
            bitmap?.recycle()
        }
    }

    private fun getFileLocationFromDirectory(): File {
        val directory = ContextWrapper(context).getDir(FOLDER_NAME, Context.MODE_PRIVATE)
        if (!directory.exists()) directory.mkdir()
        val imageName = System.currentTimeMillis().toString() + FILE_EXTENSIONS
        return File(directory.absolutePath, imageName)
    }

    companion object {
        const val PARAM_IMAGE = "byte array"
        const val FOLDER_NAME = "extras"
        const val FILE_EXTENSIONS = ".jpg"
        const val IMAGE_QUALITY = 100
        const val MAX_IMAGE_DIMENSION = 1280
    }
}

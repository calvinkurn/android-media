package com.tokopedia.gopay.kyc.domain.usecase

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.otaliastudios.cameraview.controls.Facing
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.image.ImageHandler
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
        ordinal: Int
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(PARAM_IMAGE, imageByte)
            putObject(PARAM_IMAGE_PROPERTIES, ordinal)
        }
        execute({ onSuccess(it) }, { onError(it) }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): CameraImageResult {
        val imageByte = (useCaseRequestParams.getObject(PARAM_IMAGE) as ByteArray)
        val facingOrdinal = (useCaseRequestParams.getObject(PARAM_IMAGE_PROPERTIES) as Int)
        return generateImage(imageByte, facingOrdinal)
    }

    private fun generateImage(imageByte: ByteArray, ordinal: Int): CameraImageResult {
        val cameraResultFile = saveToCacheDirectory(imageByte)
        val finalBitmap = cameraResultFile?.let { onSuccessImageTakenFromCamera(it, ordinal) }
        return CameraImageResult(
            finalBitmap?.width ?: 0,
            finalBitmap?.height ?: 0,
            cameraResultFile?.absolutePath,
            ArrayList(bitmapToByte(finalBitmap))
        )
    }

    private fun onSuccessImageTakenFromCamera(imageFile: File, ordinal: Int): Bitmap? {
        val file = File(imageFile.absolutePath)
        return if (file.exists()) {
            val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
            flipBitmapByOrdinal(myBitmap, ordinal)
        } else null
    }

    private fun flipBitmapByOrdinal(bitmap: Bitmap?, ordinal: Int): Bitmap? {
        bitmap?.let {
            return if (ordinal == Facing.FRONT.ordinal) {
                val flippedBitmap = ImageHandler.flip(bitmap, true, false)
                bitmap.recycle()
                flippedBitmap
            } else bitmap
        }
        return null
    }

    private fun saveToCacheDirectory(imageByte: ByteArray): File? {
        var out: FileOutputStream? = null
        return try {
            val file = getFileLocationFromDirectory()
            out = FileOutputStream(file)
            out.write(imageByte)
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

    private fun getFileLocationFromDirectory(): File {
        val directory = ContextWrapper(context).getDir(FOLDER_NAME, Context.MODE_PRIVATE)
        if (!directory.exists()) directory.mkdir()
        val imageName = System.currentTimeMillis().toString() + FILE_EXTENSIONS
        return File(directory.absolutePath, imageName)
    }

    private fun bitmapToByte(bitmap: Bitmap?): List<Byte> {
        try {
            val stream = ByteArrayOutputStream()
            bitmap?.compress(
                Bitmap.CompressFormat.JPEG,
                IMAGE_QUALITY,
                stream
            )
            return stream.toByteArray().toList()
        } finally {
            bitmap?.recycle()
        }
    }

    companion object {
        const val PARAM_IMAGE = "byte array"
        const val PARAM_IMAGE_PROPERTIES = "image properties"
        const val FOLDER_NAME = "extras"
        const val FILE_EXTENSIONS = ".jpg"
        const val IMAGE_QUALITY = 100
    }
}


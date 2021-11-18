package com.tokopedia.homecredit.domain.usecase

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import com.otaliastudios.cameraview.CameraUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.homecredit.domain.model.ImageDetail
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class HomeCreditUseCase @Inject constructor(
        @ApplicationContext val context: Context
) : UseCase<ImageDetail>() {


    fun saveDetail(
            success: (ImageDetail) -> Unit,
            onFail: (Throwable) -> Unit,
            imageByte: ByteArray,
    ) {

        useCaseRequestParams = RequestParams().apply { putObject(IMAGE_BYTE_ARRAY, imageByte) }
        execute({
            success(it)
        }, {
            onFail(it)
        }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): ImageDetail {
        val imgByteArray = (useCaseRequestParams.getObject(IMAGE_BYTE_ARRAY)) as ByteArray
        val compressedBitmap = CameraUtils.decodeBitmap(imgByteArray, MAX_IMAGE_DIMEN, MAX_IMAGE_DIMEN)
        val compressedByteArray = bitmapToByteArray(compressedBitmap)
        val cameraResultFile = saveToCacheDirectory(compressedByteArray)
        return ImageDetail(
                compressedBitmap?.width ?: 0,
                compressedBitmap?.height ?: 0,
                cameraResultFile?.absolutePath
        )
    }

    private fun getFileLocationFromDirectory(): File {
        val directory = ContextWrapper(context).getDir(FOLDER_NAME, Context.MODE_PRIVATE)
        if (!directory.exists()) directory.mkdir()
        val imageName = System.currentTimeMillis().toString() + FILE_EXTENSIONS
        return File(directory.absolutePath, imageName)
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
        } catch (e: Exception) {
            throw e
        }

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
            if (out != null) {
                try {
                    out.flush()
                    out.close()
                } catch (e: IOException) {
                }
            }
        }
    }

    companion object {
        const val IMAGE_QUALITY = 95
        const val MAX_IMAGE_DIMEN = 1280
        const val FOLDER_NAME = "extras"
        const val FILE_EXTENSIONS = ".jpg"
        const val IMAGE_BYTE_ARRAY = "ClickedImageByteArray"
    }


}


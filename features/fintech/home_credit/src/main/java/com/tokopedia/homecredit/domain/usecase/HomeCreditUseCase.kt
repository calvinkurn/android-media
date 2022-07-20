package com.tokopedia.homecredit.domain.usecase

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.size.Size
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

    /**
     * @param imageByte this is the image captured in byte
     * @param mCaptureNativeSize this is the image height and width that is being captured from the camera
     */

    fun saveDetail(
            success: (ImageDetail) -> Unit,
            onFail: (Throwable) -> Unit,
            imageByte: ByteArray,
            mCaptureNativeSize: Size?,
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(IMAGE_BYTE_ARRAY, imageByte)
            putObject(NATIVE_IMAGE_SIZE, mCaptureNativeSize)
        }
        execute({
            success(it)
        }, {
            onFail(it)
        }, useCaseRequestParams)
    }

    /**
     * Convert Byte Array To Bitmap MAX_IMAGE_DIMEN= 1280 means image height or weight will be at max 1280.
     * In method bitmapToByteArray() we are compressing the quality of the Image to 95 percent
     */
    override suspend fun executeOnBackground(): ImageDetail {
        val imgByteArray = (useCaseRequestParams.getObject(IMAGE_BYTE_ARRAY)) as ByteArray
        val mCaptureNativeSize = (useCaseRequestParams.getObject(NATIVE_IMAGE_SIZE)) as Size?
        val compressedBitmap = if (mCaptureNativeSize != null)
            CameraUtils.decodeBitmap(imgByteArray, mCaptureNativeSize.width, mCaptureNativeSize.height)
        else
            CameraUtils.decodeBitmap(imgByteArray, MAX_IMAGE_DIMEN, MAX_IMAGE_DIMEN)
        try {

            val compressedByteArray = bitmapToByteArray(compressedBitmap)
            val cameraResultFile = saveToCacheDirectory(compressedByteArray)
            return ImageDetail(
                    compressedBitmap?.width ?: 0,
                    compressedBitmap?.height ?: 0,
                    cameraResultFile?.absolutePath
            )
        } finally {
            compressedBitmap?.recycle()
        }
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
        const val NATIVE_IMAGE_SIZE = "native_image_size"
        const val MAX_IMAGE_DIMEN = 1280
        const val FOLDER_NAME = "extras"
        const val FILE_EXTENSIONS = ".jpg"
        const val IMAGE_BYTE_ARRAY = "ClickedImageByteArray"
    }


}


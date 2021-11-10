package com.tokopedia.homecredit.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.usecase.coroutines.UseCase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class HomeCreditUseCase @Inject constructor(
    @ApplicationContext val context: Context
) : UseCase<ImageDetail>() {

    private lateinit var imgByteArray: ByteArray
    var captureSize: Size? = null
    var file: File? = null

    fun saveDetail(
        imageByte: ByteArray,
        mCaptureNativeSize: Size,
        filePath: File,
        success: (ImageDetail) -> Unit,
        onFail: (Throwable) -> Unit
    ) {
        this.captureSize = mCaptureNativeSize
        this.imgByteArray = imageByte
        this.file = filePath

        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    override suspend fun executeOnBackground(): ImageDetail {
        return try {
            val bitmap = CameraUtils.decodeBitmap(
                imgByteArray,
                captureSize?.width ?: 0,
                captureSize?.height ?: 0
            )
            if (bitmap != null) {
                val cameraResultFile: File? = saveToCacheDirectory(imgByteArray)
                if (cameraResultFile != null)
                    ImageDetail(BitmapFactory.decodeFile(File(cameraResultFile.absolutePath).absolutePath))
                else
                    ImageDetail(null)
            } else
                ImageDetail(null)
        } catch (e: Exception) {
            ImageDetail(null)
        }

    }


    private fun saveToCacheDirectory(imageByte: ByteArray): File? {
        var out: FileOutputStream? = null
        return try {
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


}


data class ImageDetail(
    var imgBitmap: Bitmap?

)
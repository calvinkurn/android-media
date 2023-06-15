package com.tokopedia.media.editor.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.editor.ui.uimodel.BitmapCreation
import com.tokopedia.media.editor.utils.getEditorSaveFolderPath
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.FileChannel
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

interface SaveImageRepository {
    fun saveToCache(
        bitmapParam: Bitmap,
        filename: String? = null,
        sourcePath: String
    ): File?

    fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String
}

class SaveImageRepositoryImpl @Inject constructor(
    private val bitmapConverter: BitmapConverterRepository,
    private val bitmapCreation: BitmapCreationRepository,
) : SaveImageRepository {
    override fun saveToCache(
        bitmapParam: Bitmap,
        filename: String?,
        sourcePath: String
    ): File? {
        val isPng = ImageProcessingUtil.isPng(sourcePath)
        return ImageProcessingUtil.writeImageToTkpdPath(
            bitmapParam,
            if (isPng) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG,
            getEditorSaveFolderPath()
        )
    }

    override fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String {
        val latch = CountDownLatch(1)

        var resultBitmap: Bitmap? = null
        Thread {
            bitmapConverter.uriToBitmap(Uri.parse(imageBaseUrl))?.let { baseBitmap ->
                resultBitmap = baseBitmap

                bitmapConverter.uriToBitmap(Uri.parse(imageAddedUrl))?.let { overlayBitmap ->
                    val widthValidation = baseBitmap.width != overlayBitmap.width
                    val heightValidation = baseBitmap.height != overlayBitmap.height

                    val finalBitmap = if (widthValidation || heightValidation) {
                        bitmapCreation.createBitmap(
                            BitmapCreation.scaledBitmap(
                                overlayBitmap,
                                baseBitmap.width,
                                baseBitmap.height,
                                true
                            )
                        )
                    } else {
                        overlayBitmap
                    }

                    val canvas = Canvas(baseBitmap)
                    finalBitmap?.let {
                        canvas.drawBitmap(it,
                            XY_FLATTEN_COORDINATE,
                            XY_FLATTEN_COORDINATE,
                            Paint()
                        )
                    }
                }
            }

            latch.countDown()
        }.start()

        latch.await()
        return resultBitmap?.let {
            saveToCache(it, sourcePath = sourcePath)?.path ?: ""
        } ?: ""
    }

    companion object {
        private const val FILE_NAME_PREFIX = "Tkpd"
        private const val MIME_IMAGE_TYPE = "image/jpeg"

        private const val XY_FLATTEN_COORDINATE = 0f
    }
}

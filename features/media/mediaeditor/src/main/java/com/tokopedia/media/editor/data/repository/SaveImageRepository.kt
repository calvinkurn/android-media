package com.tokopedia.media.editor.data.repository

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import com.tokopedia.media.editor.ui.uimodel.BitmapCreation
import com.tokopedia.media.editor.utils.GENERAL_ERROR
import com.tokopedia.media.editor.utils.getEditorSaveFolderPath
import com.tokopedia.media.editor.utils.newRelicLog
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import javax.inject.Inject

interface SaveImageRepository {
    fun saveToCache(
        bitmapParam: Bitmap,
        filename: String? = null,
        sourcePath: String
    ): File?

    suspend fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String?
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

    override suspend fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String? {
        var errorCode = NO_ERROR
        var resultBitmap: Bitmap? = null
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
                    canvas.drawBitmap(
                        it,
                        XY_FLATTEN_COORDINATE,
                        XY_FLATTEN_COORDINATE,
                        Paint()
                    )
                }
            } ?: run {
                errorCode = ERROR_LOAD_FAILED_ADDED_SOURCE
            }
        } ?: run {
            errorCode = ERROR_LOAD_FAILED_BASE
        }

        if (errorCode != NO_ERROR) {
            val errorMsg = if (errorCode == ERROR_LOAD_FAILED_BASE) {
                ERROR_LOAD_FAILED_BASE_TEXT
            } else {
                ERROR_LOAD_FAILED_ADDED_SOURCE_TEXT
            }

            newRelicLog(
                mapOf(
                    GENERAL_ERROR to "Failed flatten - failed load$errorMsg"
                )
            )

            return null
        }

        return resultBitmap?.let {
            saveToCache(it, sourcePath = sourcePath)?.path ?: ""
        } ?: ""
    }

    companion object {
        private const val XY_FLATTEN_COORDINATE = 0f

        private const val NO_ERROR = -1

        private const val ERROR_LOAD_FAILED_BASE = 0
        private const val ERROR_LOAD_FAILED_BASE_TEXT = "Base"

        private const val ERROR_LOAD_FAILED_ADDED_SOURCE = 1
        private const val ERROR_LOAD_FAILED_ADDED_SOURCE_TEXT = "Added Source"
    }
}

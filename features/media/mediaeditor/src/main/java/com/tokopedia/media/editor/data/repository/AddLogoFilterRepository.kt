package com.tokopedia.media.editor.data.repository

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import com.tokopedia.media.editor.utils.mediaCreateScaledBitmap
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

interface AddLogoFilterRepository {
    fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String
}

class AddLogoFilterRepositoryImpl @Inject constructor(
    private val saveImage: SaveImageRepository,
    private val bitmapConverter: BitmapConverterRepository
) : AddLogoFilterRepository {
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
                        mediaCreateScaledBitmap(
                            overlayBitmap,
                            baseBitmap.width,
                            baseBitmap.height,
                            true
                        )
                    } else {
                        overlayBitmap
                    }

                    val canvas = Canvas(baseBitmap)
                    finalBitmap?.let {
                        canvas.drawBitmap(it, XY_FLATTEN_COORDINATE, XY_FLATTEN_COORDINATE, Paint())
                    }
                }
            }

            latch.countDown()
        }.start()

        latch.await()
        return resultBitmap?.let {
            saveImage.saveToCache(it, sourcePath = sourcePath)?.path ?: ""
        } ?: run {
            ""
        }
    }

    companion object {
        const val XY_FLATTEN_COORDINATE = 0f
    }
}

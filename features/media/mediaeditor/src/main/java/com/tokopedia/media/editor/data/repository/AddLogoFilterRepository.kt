package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
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
    @ApplicationContext private val context: Context,
    private val saveImage: SaveImageRepository,
    private val bitmapConverter: BitmapConverterRepository
) : AddLogoFilterRepository {
    override fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String {
        val latch = CountDownLatch(1)

        var baseBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        Thread {
            baseBitmap = bitmapConverter.uriToBitmap(Uri.parse(imageBaseUrl))

            bitmapConverter.uriToBitmap(Uri.parse(imageAddedUrl))?.let {
                val widthValidation = baseBitmap.width != it.width
                val heightValidation = baseBitmap.height != it.height

                val finalBitmap = if (widthValidation || heightValidation) {
                    Bitmap.createScaledBitmap(
                        it,
                        baseBitmap.width,
                        baseBitmap.height,
                        true
                    )
                } else {
                    it
                }

                val canvas = Canvas(baseBitmap)
                canvas.drawBitmap(finalBitmap, XY_FLATTEN_COORDINATE, XY_FLATTEN_COORDINATE, Paint())
            }

            latch.countDown()
        }.start()

        latch.await()
        return saveImage.saveToCache(baseBitmap, sourcePath = sourcePath)?.path ?: ""
    }

    companion object {
        const val XY_FLATTEN_COORDINATE = 0f
    }
}

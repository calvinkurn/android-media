package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.Glide
import java.util.concurrent.CountDownLatch

interface AddLogoFilterRepository {
    fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String
}

class AddLogoFilterRepositoryImpl(
    private val context: Context,
    private val saveImage: SaveImageRepository
): AddLogoFilterRepository {
    override fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String {
        val latch = CountDownLatch(1)

        var baseBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        Thread{
            baseBitmap = Glide
                .with(context)
                .asBitmap()
                .load(imageBaseUrl)
                .submit()
                .get()

            val addedBitmap = Glide
                .with(context)
                .asBitmap()
                .load(imageAddedUrl)
                .submit()
                .get()

            val canvas = Canvas(baseBitmap)
            canvas.drawBitmap(addedBitmap, XY_FLATTEN_COORDINATE, XY_FLATTEN_COORDINATE, Paint())
            latch.countDown()
        }.start()

        latch.await()
        return saveImage.saveToCache(baseBitmap, sourcePath = sourcePath)?.path ?: ""
    }

    companion object {
        const val XY_FLATTEN_COORDINATE = 0f
    }
}

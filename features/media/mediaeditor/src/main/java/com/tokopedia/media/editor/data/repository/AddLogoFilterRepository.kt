package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

interface AddLogoFilterRepository {
    fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String

    fun setLocalLogo(path: String)
    fun getLocalLogo(): String
}

class AddLogoFilterRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val saveImage: SaveImageRepository
): AddLogoFilterRepository {
    private val localCacheHandler = LocalCacheHandler(context, PREF_NAME_CACHE_ADD_LOGO)

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

            var addedBitmap = Glide
                .with(context)
                .asBitmap()
                .load(imageAddedUrl)
                .submit()
                .get()

            val widthValidation = baseBitmap.width != addedBitmap.width
            val heightValidation = baseBitmap.height != addedBitmap.height
            if (widthValidation || heightValidation) {
                addedBitmap = Bitmap.createScaledBitmap(addedBitmap, baseBitmap.width, baseBitmap.height, true)
            }

            val canvas = Canvas(baseBitmap)
            canvas.drawBitmap(addedBitmap, XY_FLATTEN_COORDINATE, XY_FLATTEN_COORDINATE, Paint())
            latch.countDown()
        }.start()

        latch.await()
        return saveImage.saveToCache(baseBitmap, sourcePath = sourcePath)?.path ?: ""
    }

    override fun setLocalLogo(path: String) {
        localCacheHandler.putString(KEY_LOCAL_LOGO, path)
        localCacheHandler.applyEditor()
    }

    override fun getLocalLogo(): String {
        return localCacheHandler.getString(KEY_LOCAL_LOGO, "")
    }

    companion object {
        private const val PREF_NAME_CACHE_ADD_LOGO = "cache_add_logo_editor"
        private const val KEY_LOCAL_LOGO = "local_logo_path"

        const val XY_FLATTEN_COORDINATE = 0f
    }
}

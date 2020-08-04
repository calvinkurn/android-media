package com.tokopedia.play.broadcaster.util.cover

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * @author by furqan on 12/06/2020
 */
class PlayCoverImageUtilImpl(context: Context) : PlayCoverImageUtil {

    private val cacheDir = context.applicationContext.cacheDir
    private val playCoverImageFile = File(cacheDir, TEMP_COVER_NAME)

    override fun getImagePathFromBitmap(image: Bitmap): Uri {
        val fileOutputStream = FileOutputStream(playCoverImageFile)
        fileOutputStream.use {
            image.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }

        return Uri.fromFile(playCoverImageFile)
    }

    companion object {
        private const val TEMP_COVER_NAME = "temp_cover.png"
    }
}
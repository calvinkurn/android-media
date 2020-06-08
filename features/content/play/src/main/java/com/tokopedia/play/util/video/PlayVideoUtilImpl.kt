package com.tokopedia.play.util.video

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

/**
 * Created by jegul on 24/04/20
 */
class PlayVideoUtilImpl(context: Context) : PlayVideoUtil {

    companion object {
        private const val END_IMAGE_NAME = "play_end_image.png"
    }

    private val cacheDir = context.cacheDir
    private val playEndImageFile = File(cacheDir, END_IMAGE_NAME)

    override fun saveEndImage(image: Bitmap) {
        val fileOutputStream = FileOutputStream(playEndImageFile)
        try {
            image.compress(Bitmap.CompressFormat.WEBP, 60, fileOutputStream)
        } catch (e: Exception) {
        } finally {
            fileOutputStream.close()
        }
    }

    override fun getEndImage(): Bitmap? {
        return BitmapFactory.decodeFile(playEndImageFile.path)
    }

    override fun clearImage() {
        if (playEndImageFile.exists()) playEndImageFile.delete()
    }
}
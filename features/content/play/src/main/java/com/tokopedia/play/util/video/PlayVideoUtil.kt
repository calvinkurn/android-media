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
class PlayVideoUtil(context: Context) {

    companion object {
        private const val END_IMAGE_NAME = "play_end_image.png"
    }

    private val cacheDir = context.cacheDir
    private val playEndImageFile = File(cacheDir, END_IMAGE_NAME)

    fun saveEndImage(image: Bitmap) {
        val fileOutputStream = FileOutputStream(playEndImageFile)
        try {
            image.compress(Bitmap.CompressFormat.WEBP, 60, fileOutputStream)
        } catch (e: Exception) {
        } finally {
            fileOutputStream.close()
        }
    }

    fun getEndImage(): Bitmap? {
        return BitmapFactory.decodeFile(playEndImageFile.path)
    }

    fun clearImage() {
        if (playEndImageFile.exists()) playEndImageFile.delete()
    }
}
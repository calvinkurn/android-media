package com.tokopedia.mediauploader.util

import android.graphics.BitmapFactory
import java.io.File

object UploadValidatorUtil {

    fun getFileExtension(filePath: String): String {
        val lastIndexOf = filePath.lastIndexOf(".")
        return if (lastIndexOf == -1) "" else filePath.substring(lastIndexOf)
    }

    fun isMaxFileSize(filePath: String, maxFileSize: Int): Boolean {
        return File(filePath).length() > maxFileSize
    }

    fun isMaxBitmapResolution(filePath: String, maxWidth: Int, maxHeight: Int): Boolean {
        val bitmapOptions = getBitmapOptions(filePath)
        val width = bitmapOptions.outWidth
        val height = bitmapOptions.outHeight
        return width > maxWidth && height > maxHeight
    }

    fun isMinBitmapResolution(filePath: String, minWidth: Int, minHeight: Int): Boolean {
        val bitmapOptions = getBitmapOptions(filePath)
        val width = bitmapOptions.outWidth
        val height = bitmapOptions.outHeight
        return width < minWidth && height < minHeight
    }

    private fun getBitmapOptions(filePath: String): BitmapFactory.Options {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, bitmapOptions)
        return bitmapOptions
    }

}
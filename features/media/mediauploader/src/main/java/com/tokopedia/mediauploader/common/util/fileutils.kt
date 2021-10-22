package com.tokopedia.mediauploader.common.util

import android.graphics.BitmapFactory
import java.io.File

fun String.fileExtension(): String {
    val lastIndexOf = this.lastIndexOf(".")
    return if (lastIndexOf == -1) "" else this.substring(lastIndexOf)
}

fun String.isImage(): Boolean {
    val imageExtension = ".jpg,.jpeg"
    return imageExtension
        .split(",")
        .contains(fileExtension())
}

fun File.isMaxFileSize(maxFileSize: Int): Boolean {
    return this.length() > maxFileSize
}

fun String.isMaxBitmapResolution(maxWidth: Int, maxHeight: Int): Boolean {
    val bitmapOptions = getBitmapOptions(this)
    val width = bitmapOptions.outWidth
    val height = bitmapOptions.outHeight
    return width > maxWidth && height > maxHeight
}

fun String.isMinBitmapResolution(minWidth: Int, minHeight: Int): Boolean {
    val bitmapOptions = getBitmapOptions(this)
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
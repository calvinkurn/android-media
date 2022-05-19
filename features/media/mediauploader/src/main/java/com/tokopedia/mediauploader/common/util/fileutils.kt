package com.tokopedia.mediauploader.common.util

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.webkit.MimeTypeMap
import java.io.File
import java.net.URLConnection

fun Int.mbToBytes(): Int {
    return this * 1024 * 1024
}

fun String.fileExtension(): String {
    val extension = MimeTypeMap.getFileExtensionFromUrl(this)
    if (!TextUtils.isEmpty(extension)) {
        return extension
    }
    return if (this.contains(".")) {
        this.substring(this.lastIndexOf(".") + 1, this.length)
    } else {
        ""
    }
}

fun isVideoFormat(path: String): Boolean {
    val extension = path.fileExtension()
    val prefix = "video"

    val mimeType =
        if (TextUtils.isEmpty(extension)) {
            URLConnection.guessContentTypeFromName(path)
        } else {
            MimeTypeMap
                .getSingleton()
                .getMimeTypeFromExtension(extension)
        }

    return mimeType != null && mimeType.startsWith(prefix)
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
package com.tokopedia.mediauploader.common.util

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.webkit.MimeTypeMap
import java.io.File
import java.net.URLConnection
import java.util.concurrent.TimeUnit

private const val MIME_TYPE_IMAGE = "image"
private const val MIME_TYPE_VIDEO = "video"

fun Int.mbToBytes(): Int {
    return this * 1024 * 1024
}

fun getFileFormatByMimeType(type: String, path: String, extension: String): Boolean {
    val mimeType =
        if (TextUtils.isEmpty(extension)) {
            URLConnection.guessContentTypeFromName(path)
        } else {
            MimeTypeMap
                .getSingleton()
                .getMimeTypeFromExtension(extension)
        }

    return mimeType != null && mimeType.startsWith(type)
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

fun isImageFormat(path: String) = getFileFormatByMimeType(
    type = MIME_TYPE_IMAGE,
    path = path,
    extension = path.fileExtension()
)

fun isVideoFormat(path: String) = getFileFormatByMimeType(
    type = MIME_TYPE_VIDEO,
    path = path,
    extension = path.fileExtension()
)

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

fun Long.isLessThanHoursOf(hours: Int): Boolean {
    val currentTime = System.currentTimeMillis()
    val diff = TimeUnit.MILLISECONDS.toHours(currentTime - this)
    return diff < hours
}

fun String.parseErrorMessage(): Pair<String, String> {
    val pattern = "[(<]".toRegex()

    if (!this.contains(pattern)) return Pair(this, "")

    // get string index before < or (
    val requestIdIndex = this
        .indexOfFirst { it.toString().matches(pattern) }
        .takeIf { it > 0 } ?: this.length

    val message = this.substring(0, requestIdIndex).trim()
    val lastMessage = this.substring(requestIdIndex, this.length).trim()

    val requestId = try {
        val reqIdPattern = "<(.*?)>".toRegex()
        val find = reqIdPattern.find(lastMessage)

        find?.groupValues?.get(1) ?: "-1"
    } catch (t: Throwable) {
        this
    }

    return Pair(message, requestId)
}

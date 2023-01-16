package com.tokopedia.picker.common.utils

import android.text.TextUtils
import android.webkit.MimeTypeMap
import java.net.URLConnection

private const val MIME_TYPE_IMAGE = "image"
private const val MIME_TYPE_VIDEO = "video"

/**
 * This utility is used publicly, both in the module module or in the other feature modules.
 * Anyone who uses picker-common can use the utilities below.
 */

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

fun fileExtension(path: String): String {
    val extension = MimeTypeMap.getFileExtensionFromUrl(path)
    if (!extension.isNullOrEmpty()) return extension

    return if (path.contains(".")) {
        path.substring(path.lastIndexOf(".") + 1, path.length)
    } else {
        ""
    }
}

fun isImageFormat(path: String) = getFileFormatByMimeType(
    type = MIME_TYPE_IMAGE,
    path = path,
    extension = fileExtension(path)
)

fun isVideoFormat(path: String) = getFileFormatByMimeType(
    type = MIME_TYPE_VIDEO,
    path = path,
    extension = fileExtension(path)
)

fun String.isUrl(): Boolean {
    return this.contains("https") || this.contains("http")
}

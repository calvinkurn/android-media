package com.tokopedia.picker.common.util

import android.text.TextUtils
import android.webkit.MimeTypeMap
import java.net.URLConnection

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
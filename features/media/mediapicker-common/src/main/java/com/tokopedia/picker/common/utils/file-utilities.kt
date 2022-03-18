@file:JvmName("PickerUtilities")
package com.tokopedia.picker.common.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap
import java.io.File
import java.net.URLConnection

const val DEFAULT_DURATION_LABEL = "00:00"

private fun fileExtension(path: String): String {
    val extension = MimeTypeMap.getFileExtensionFromUrl(path)
    if (!extension.isNullOrEmpty()) return extension

    return if (path.contains(".")) {
        path.substring(path.lastIndexOf(".") + 1, path.length)
    } else {
        ""
    }
}

fun getBitmapOptions(filePath: String): BitmapFactory.Options {
    val bitmapOptions = BitmapFactory.Options()
    bitmapOptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(filePath, bitmapOptions)
    return bitmapOptions
}

fun isGifFormat(path: String): Boolean {
    val extension = fileExtension(path)
    return extension.equals("gif", ignoreCase = true)
}

fun isVideoFormat(path: String): Boolean {
    val extension = fileExtension(path)
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

fun extractVideoDuration(context: Context?, uri: Uri): Long? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)

        val durationData = retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        )

        retriever.release()

        durationData?.toLongOrNull()
    } catch (e: Exception) {
        0L
    }
}

fun extractVideoDuration(context: Context?, filePath: String): Long? {
    val file = File(filePath)

    return extractVideoDuration(
        context,
        Uri.fromFile(file)
    )
}

fun Long?.videoFormat(): String {
    val duration = this?: 0L

    if (duration == 0L) return DEFAULT_DURATION_LABEL

    val second = duration / 1000 % 60
    val minute = duration / (1000 * 60) % 60
    val hour = duration / (1000 * 60 * 60) % 24

    return if (hour > 0) {
        String.format("%02d:%02d:%02d", hour, minute, second)
    } else {
        String.format("%02d:%02d", minute, second)
    }
}
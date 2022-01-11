@file:JvmName("PickerUtilities")
package com.tokopedia.picker.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap
import com.tokopedia.config.GlobalConfig
import com.tokopedia.picker.data.entity.Media
import java.net.URLConnection

const val DEFAULT_DURATION_LABEL = "00:00"

fun exceptionHandler(invoke: () -> Unit) {
    try {
        invoke()
    } catch (e: Exception) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
    }
}

private fun getExtension(path: String): String {
    val extension = MimeTypeMap.getFileExtensionFromUrl(path)
    if (!extension.isNullOrEmpty()) {
        return extension
    }
    return if (path.contains(".")) {
        path.substring(path.lastIndexOf(".") + 1, path.length)
    } else {
        ""
    }
}

fun isGifFormat(image: Media): Boolean {
    return isGifFormat(image.path)
}

fun isGifFormat(path: String): Boolean {
    val extension = getExtension(path)
    return extension.equals("gif", ignoreCase = true)
}

fun isVideoFormat(path: String): Boolean {
    val extension = getExtension(path)
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

fun getVideoDurationLabel(context: Context?, uri: Uri): String {
    try {
        val retriever = MediaMetadataRetriever()

        retriever.setDataSource(context, uri)

        val durationData = retriever
            .extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION
            )

        retriever.release()

        // Return default duration label if null
        val duration = durationData?.toLongOrNull() ?: return DEFAULT_DURATION_LABEL
        val second = duration / 1000 % 60
        val minute = duration / (1000 * 60) % 60
        val hour = duration / (1000 * 60 * 60) % 24
        return if (hour > 0) {
            String.format("%02d:%02d:%02d", hour, minute, second)
        } else {
            String.format("%02d:%02d", minute, second)
        }
    } catch (e: Exception) {
        return DEFAULT_DURATION_LABEL
    }
}
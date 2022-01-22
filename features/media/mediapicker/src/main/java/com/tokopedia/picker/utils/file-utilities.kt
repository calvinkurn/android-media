@file:JvmName("PickerUtilities")
package com.tokopedia.picker.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.utils.image.ImageProcessingUtil
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

fun generateFile(captureSize: Size?, byteArray: ByteArray, invoke: (File?) -> Unit) {
    val compressFormat = Bitmap.CompressFormat.JPEG
    val nativeCaptureSize = captureSize?: return

    try {
        CameraUtils.decodeBitmap(
            byteArray,
            nativeCaptureSize.width,
            nativeCaptureSize.height
        ) {
            if (it != null) {
                invoke(ImageProcessingUtil.writeImageToTkpdPath(it, compressFormat))
            }
        }
    } catch (e: Throwable) {
        invoke(ImageProcessingUtil.writeImageToTkpdPath(byteArray, compressFormat))
    }
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

fun videoDurationFromUri(context: Context?, uri: Uri): String {
    try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)

        val durationData = retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        )

        retriever.release()

        val duration = durationData?.toLongOrNull() ?: return DEFAULT_DURATION_LABEL

        return videoDurationLabel(duration)
    } catch (e: Exception) {
        return DEFAULT_DURATION_LABEL
    }
}

fun videoDurationLabel(duration: Long): String {
    val second = duration / 1000 % 60
    val minute = duration / (1000 * 60) % 60
    val hour = duration / (1000 * 60 * 60) % 24

    return if (hour > 0) {
        String.format("%02d:%02d:%02d", hour, minute, second)
    } else {
        String.format("%02d:%02d", minute, second)
    }
}
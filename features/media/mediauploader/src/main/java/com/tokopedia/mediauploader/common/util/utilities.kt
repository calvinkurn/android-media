package com.tokopedia.mediauploader.common.util

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.NETWORK_ERROR
import com.tokopedia.mediauploader.common.data.consts.TIMEOUT_ERROR
import com.tokopedia.mediauploader.common.logger.ERROR_MAX_LENGTH
import com.tokopedia.mediauploader.common.logger.trackToTimber
import com.tokopedia.mediauploader.common.state.UploadResult
import okhttp3.internal.http2.ConnectionShutdownException
import okhttp3.internal.http2.StreamResetException
import java.io.File
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URLConnection
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit

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

fun Long.isLessThanHoursOf(hours: Int): Boolean {
    val currentTime = System.currentTimeMillis()
    val diff = TimeUnit.MILLISECONDS.toHours(currentTime - this)
    return diff < hours
}

fun String.addPrefix(): String {
    val pattern = "[(<]".toRegex()
    val kodeError = "Kode Error:"

    if (!this.contains(pattern)) return this

    // get string index before < or (
    val requestIdIndex = this
        .indexOfFirst { it.toString().matches(pattern) }
        .takeIf { it > 0 } ?: this.length

    val message = this.substring(0, requestIdIndex).trim()
    val lastMessage = this.substring(requestIdIndex, this.length).trim()

    return "$message $kodeError $lastMessage"
}

suspend fun request(
    sourceId: String,
    file: File,
    uploaderManager: UploaderManager,
    execute: suspend () -> UploadResult,
): UploadResult {
    return try {
        execute().also {
            if (it is UploadResult.Error) {
                uploaderManager.setError(it.message, sourceId, file)
            }
        }
    } catch (e: SocketTimeoutException) {
        uploaderManager.setError(TIMEOUT_ERROR, sourceId, file)
    } catch (e: StreamResetException) {
        uploaderManager.setError(TIMEOUT_ERROR, sourceId, file)
    } catch (e: Exception) {
        if (e !is UnknownHostException &&
            e !is SocketException &&
            e !is InterruptedIOException &&
            e !is ConnectionShutdownException &&
            e !is CancellationException
        ) {
            @Suppress("UselessCallOnNotNull")
            if (Log.getStackTraceString(e).orEmpty().isNotEmpty()) {
                trackToTimber(sourceId, Log.getStackTraceString(e).take(ERROR_MAX_LENGTH).trim())
            }
        }

        uploaderManager.setError(NETWORK_ERROR, sourceId, file)
    }
}

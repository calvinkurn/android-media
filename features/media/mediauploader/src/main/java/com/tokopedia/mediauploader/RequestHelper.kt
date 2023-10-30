package com.tokopedia.mediauploader

import android.util.Log
import com.tokopedia.mediauploader.common.data.consts.NETWORK_ERROR
import com.tokopedia.mediauploader.common.data.consts.TIMEOUT_ERROR
import com.tokopedia.mediauploader.common.logger.ERROR_MAX_LENGTH
import com.tokopedia.mediauploader.common.logger.trackToTimber
import com.tokopedia.mediauploader.common.state.UploadResult
import okhttp3.internal.http2.StreamResetException
import java.io.File
import java.net.SocketTimeoutException

data class RequestHelper(
    val sourceId: String,
    val file: File
) {

    init {
        if (file.exists()) {
            
        }
    }
}

suspend inline fun request(
    sourceId: String,
    file: File,
    uploaderManager: UploaderManager,
    crossinline execute: suspend () -> UploadResult,
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
        val stackTrace = Log.getStackTraceString(e).take(ERROR_MAX_LENGTH).trim()

        if (stackTrace.isNotEmpty()) {
            trackToTimber(sourceId, stackTrace)
        }

        uploaderManager.setError(NETWORK_ERROR, sourceId, file)
    }
}

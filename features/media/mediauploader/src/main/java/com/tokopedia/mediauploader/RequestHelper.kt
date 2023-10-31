package com.tokopedia.mediauploader

import android.util.Log
import com.tokopedia.mediauploader.analytics.UploaderLogger
import com.tokopedia.mediauploader.common.data.consts.NETWORK_ERROR
import com.tokopedia.mediauploader.common.data.consts.TIMEOUT_ERROR
import com.tokopedia.mediauploader.common.state.UploadResult
import okhttp3.internal.http2.StreamResetException
import java.io.File
import java.net.SocketTimeoutException

const val ERROR_MAX_LENGTH = 1500

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
        if (stackTrace.isNotEmpty()) UploaderLogger.commonWithoutReqIdError(sourceId, stackTrace)

        uploaderManager.setError(NETWORK_ERROR, sourceId, file)
    }
}

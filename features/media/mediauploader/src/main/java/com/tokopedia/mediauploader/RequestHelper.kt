package com.tokopedia.mediauploader

import android.util.Log
import com.tokopedia.mediauploader.analytics.UploaderLogger
import com.tokopedia.mediauploader.common.data.consts.NETWORK_ERROR
import com.tokopedia.mediauploader.common.data.consts.TIMEOUT_ERROR
import com.tokopedia.mediauploader.common.state.UploadResult
import okhttp3.internal.http2.StreamResetException
import timber.log.Timber
import java.net.SocketTimeoutException

const val ERROR_MAX_LENGTH = 1500

fun BaseParam.trackStackTrace(e: Exception) {
    val stackTrace = Log.getStackTraceString(e).take(ERROR_MAX_LENGTH).trim()

    if (stackTrace.isNotEmpty()) {
        UploaderLogger.commonWithoutReqIdError(this, stackTrace)
    }
}

suspend inline fun request(
    param: BaseParam,
    crossinline execute: suspend () -> UploadResult
): UploadResult {
    return try {
        execute()
    } catch (e: SocketTimeoutException) {
        Timber.d(e)
        param.trackStackTrace(e)
        UploadResult.Error(TIMEOUT_ERROR)
    } catch (e: StreamResetException) {
        Timber.d(e)
        param.trackStackTrace(e)
        UploadResult.Error(TIMEOUT_ERROR)
    } catch (e: Exception) {
        param.trackStackTrace(e)
        UploadResult.Error(NETWORK_ERROR)
    }
}

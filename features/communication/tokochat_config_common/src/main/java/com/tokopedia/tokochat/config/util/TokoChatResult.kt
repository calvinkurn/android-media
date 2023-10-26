package com.tokopedia.tokochat.config.util

sealed class TokoChatResult<out T> {
    data class Success<out T>(val data: T) : TokoChatResult<T>()
    data class Error(val throwable: Throwable) : TokoChatResult<Nothing>()
    object Loading : TokoChatResult<Nothing>()
}

package com.tokopedia.topchat.common.data

sealed class TopChatResult<out T> {
    data class Success<out T>(val data: T) : TopChatResult<T>()
    data class Error(val throwable: Throwable) : TopChatResult<Nothing>()
    object Loading : TopChatResult<Nothing>()
}

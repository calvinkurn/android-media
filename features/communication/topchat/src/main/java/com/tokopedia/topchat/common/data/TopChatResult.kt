package com.tokopedia.topchat.common.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class TopChatResult<out T> {
    data class Success<out T>(val data: T) : TopChatResult<T>()
    data class Error(val throwable: Throwable) : TopChatResult<Nothing>()
    object Loading : TopChatResult<Nothing>()
}

fun <T> Flow<T>.asFlowResult(): Flow<TopChatResult<T>> {
    return this
        .map<T, TopChatResult<T>> {
            TopChatResult.Success(it)
        }
        .onStart { emit(TopChatResult.Loading) }
        .catch { emit(TopChatResult.Error(it)) }
}

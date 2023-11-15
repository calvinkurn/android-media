package com.tokopedia.tokochat.config.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class TokoChatResult<out T> {
    data class Success<out T>(val data: T) : TokoChatResult<T>()
    data class Error(val throwable: Throwable) : TokoChatResult<Nothing>()
    object Loading : TokoChatResult<Nothing>()
}

fun <T> Flow<T>.asFlowResult(): Flow<TokoChatResult<T>> {
    return this
        .map<T, TokoChatResult<T>> {
            TokoChatResult.Success(it)
        }
        .onStart { emit(TokoChatResult.Loading) }
        .catch { emit(TokoChatResult.Error(it)) }
}

fun <T> LiveData<T>.asFlowResult(): Flow<TokoChatResult<T>> {
    return this
        .asFlow()
        .map<T, TokoChatResult<T>> {
            TokoChatResult.Success(it)
        }
        .onStart { emit(TokoChatResult.Loading) }
        .catch { emit(TokoChatResult.Error(it)) }
}

package com.tokopedia.shareexperience.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class ShareExResult<out T> {
    data class Success<out T>(val data: T) : ShareExResult<T>()
    data class Error(val throwable: Throwable) : ShareExResult<Nothing>()
    object Loading : ShareExResult<Nothing>()
}

fun <T> Flow<T>.asFlowResult(): Flow<ShareExResult<T>> {
    return this
        .map<T, ShareExResult<T>> {
            ShareExResult.Success(it)
        }
        .onStart { emit(ShareExResult.Loading) }
        .catch { emit(ShareExResult.Error(it)) }
}

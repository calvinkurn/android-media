package com.tokopedia.play.broadcaster.ui.model.result

/**
 * Created by jegul on 12/06/20
 */
sealed class NetworkResult<out T> {

    data class Success<T>(val data: T) : NetworkResult<T>()
    object Loading : NetworkResult<Nothing>()
    data class Fail(val error: Throwable, val onRetry: () -> Unit = {}) : NetworkResult<Nothing>()
}

internal inline fun<T, R> NetworkResult<T>.map(mapper: (T) -> R): NetworkResult<R> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.Success(mapper(data))
        is NetworkResult.Loading -> this
        is NetworkResult.Fail -> this
    }
}
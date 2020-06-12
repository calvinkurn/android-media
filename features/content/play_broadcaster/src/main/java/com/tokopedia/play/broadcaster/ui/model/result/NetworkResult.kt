package com.tokopedia.play.broadcaster.ui.model.result

/**
 * Created by jegul on 12/06/20
 */
sealed class NetworkResult<out T> {

    data class Success<T>(val data: T) : NetworkResult<T>()
    object Loading : NetworkResult<Nothing>()
    data class Fail(val error: Throwable) : NetworkResult<Nothing>()
}
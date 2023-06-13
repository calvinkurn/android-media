package com.tokopedia.kol.common.util

/**
 * Created by meyta.taliti on 03/08/22.
 */
sealed class ContentDetailResult<out T: Any> {
    object Loading : ContentDetailResult<Nothing>()
    data class Success<out T: Any>(val data: T) : ContentDetailResult<T>()
    data class Failure<out T: Any>(val error: Throwable, val onRetry: () -> Unit = {}) : ContentDetailResult<T>()
}


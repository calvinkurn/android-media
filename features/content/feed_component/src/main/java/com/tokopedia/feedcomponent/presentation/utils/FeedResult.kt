package com.tokopedia.feedcomponent.presentation.utils

/**
 * Created by shruti.agarwal on 02/03/23.
 */
sealed class FeedResult<out T: Any> {
    object Loading : FeedResult<Nothing>()
    data class Success<out T: Any>(val data: T) : FeedResult<T>()
    data class Failure<out T: Any>(val error: Throwable, val onRetry: () -> Unit = {}) : FeedResult<T>()
}


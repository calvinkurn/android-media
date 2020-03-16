package com.tokopedia.play.view.wrapper

/**
 * Created by jegul on 14/03/20
 */
sealed class PlayResult<T> {

    data class Success<T>(val data: T) : PlayResult<T>()
    data class Loading<T>(val showPlaceholder: Boolean, val placeholderList: List<Any> = emptyList()) : PlayResult<T>()
    data class Failure<T>(val error: Throwable) : PlayResult<T>()
}
package com.tokopedia.review.common.data

sealed class ReviewViewState<out T : Any>
object LoadingView : ReviewViewState<Nothing>()
data class Fail<out T : Any>(val fail: Throwable, val page: Int) : ReviewViewState<T>()
data class Success<out T : Any>(val data: T, val page: Int) : ReviewViewState<T>()
package com.tokopedia.review.common.data

sealed class ReviewViewState<out T : Any>
data class LoadingView(val search: String = "") : ReviewViewState<Nothing>()
data class Fail<out T : Any>(val fail: Throwable, val page: Int = 0, val search: String = "") : ReviewViewState<T>()
data class Success<out T : Any>(val data: T, val page: Int = 0, val search: String = "", val isRefresh: Boolean = false) : ReviewViewState<T>()
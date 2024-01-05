package com.tokopedia.topads.dashboard.recommendation.data.model.local

sealed class TopadsProductListState<out T : Any> {
    data class Success<out T : Any>(val data: T) : TopadsProductListState<T>()
    data class Fail(val throwable: Throwable) : TopadsProductListState<Nothing>()
    data class Empty<out T : Any>(val data: T) : TopadsProductListState<T>()
    data class Loading(val type: Int) : TopadsProductListState<Nothing>()
}

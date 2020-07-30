package com.tokopedia.tkpd.tkpdreputation.createreputation.util

sealed class LoadingDataState<out T : Any>
object LoadingView : LoadingDataState<Nothing>()
data class Fail<out T : Any>(val fail: Throwable) : LoadingDataState<T>()
data class Success<out T : Any>(val data: T) : LoadingDataState<T>()
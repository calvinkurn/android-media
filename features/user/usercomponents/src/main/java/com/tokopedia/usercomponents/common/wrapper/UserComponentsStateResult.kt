package com.tokopedia.usercomponents.common.wrapper

sealed class UserComponentsStateResult <T> {
    class Loading<T> : UserComponentsStateResult<T>()
    data class Success<T>(val data: T?): UserComponentsStateResult<T>()
    data class Fail<T>(val error: Throwable) : UserComponentsStateResult<T>()
}
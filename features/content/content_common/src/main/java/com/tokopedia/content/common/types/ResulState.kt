package com.tokopedia.content.common.types

sealed class ResultState {
    object Success : ResultState()
    object Loading : ResultState()
    data class Fail(val error: Throwable) : ResultState()

    val isSuccess: Boolean
        get() = this == Success

    val isLoading: Boolean
        get() = this == Loading

    val isFail: Boolean
        get() = this is Fail
}

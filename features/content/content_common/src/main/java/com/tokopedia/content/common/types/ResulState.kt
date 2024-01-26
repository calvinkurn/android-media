package com.tokopedia.content.common.types

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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

@OptIn(ExperimentalContracts::class)
fun ResultState.isFailed(): Boolean {
    contract {
        returns(true) implies (this@isFailed is ResultState.Fail)
    }
    return isFail
}

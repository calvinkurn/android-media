package com.tokopedia.buyerorder.detail.revamp.viewModel.uiEvent

/**
 * created by @bayazidnasir on 6/9/2022
 */

sealed class UiEvent<out T> {
    object Loading : UiEvent<Nothing>()
    data class Success<T>(val data: T) : UiEvent<T>()
    data class Fail(val error: Throwable): UiEvent<Nothing>()
}
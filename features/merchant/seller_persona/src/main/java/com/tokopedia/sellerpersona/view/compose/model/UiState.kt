package com.tokopedia.sellerpersona.view.compose.model

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

sealed class UiState<out T> {
    object Default : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Error(val t: Throwable) : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
}
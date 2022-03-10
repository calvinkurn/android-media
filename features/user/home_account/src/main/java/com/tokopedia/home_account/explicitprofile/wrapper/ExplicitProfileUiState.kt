package com.tokopedia.home_account.explicitprofile.wrapper

sealed class ExplicitProfileUiState<T> {
    data class Hide<T>(val data: T) : ExplicitProfileUiState<T>()
    data class Show<T>(val data: T) : ExplicitProfileUiState<T>()
}
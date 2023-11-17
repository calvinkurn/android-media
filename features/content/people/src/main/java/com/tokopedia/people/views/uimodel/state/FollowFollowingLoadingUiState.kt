package com.tokopedia.people.views.uimodel.state

sealed interface LoadingState {
    object Show : LoadingState
    object Hide : LoadingState
    data class Error(val throwable: Throwable) : LoadingState
}

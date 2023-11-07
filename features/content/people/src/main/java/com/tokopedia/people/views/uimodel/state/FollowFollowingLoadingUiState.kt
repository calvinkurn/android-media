package com.tokopedia.people.views.uimodel.state

data class FollowFollowingLoadingUiState(
    val state: LoadingState = LoadingState.ShowLoading,
) {
    sealed class LoadingState {
        object ShowLoading: LoadingState()
        object HideLoading: LoadingState()
        data class Error(val throwable: Throwable): LoadingState()
    }
}

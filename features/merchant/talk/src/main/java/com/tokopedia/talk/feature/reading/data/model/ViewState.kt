package com.tokopedia.talk.feature.reading.data.model

sealed class ViewState {
    data class Loading(val isRefreshing: Boolean): ViewState()
    data class Success(val isEmpty: Boolean, val page: Int): ViewState()
    data class Error(val page: Int): ViewState()
}
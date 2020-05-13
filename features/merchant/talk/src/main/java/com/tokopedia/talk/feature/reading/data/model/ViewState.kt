package com.tokopedia.talk.feature.reading.data.model

sealed class ViewState {
    object Loading: ViewState()
    data class Success(val isEmpty: Boolean): ViewState()
    data class Error(val page: Int): ViewState()
}
package com.tokopedia.feedplus.browse.presentation.model

/**
 * Created by meyta.taliti on 11/08/23.
 */
sealed class FeedBrowseUiState {
    object Placeholder : FeedBrowseUiState()
    data class Error(val throwable: Throwable) : FeedBrowseUiState()
    data class Success(
        val title: String,
        val widgets: List<FeedBrowseUiModel>
    ) : FeedBrowseUiState()
}

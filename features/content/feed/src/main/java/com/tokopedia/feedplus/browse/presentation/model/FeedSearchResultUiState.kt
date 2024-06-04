package com.tokopedia.feedplus.browse.presentation.model

import android.graphics.Bitmap

sealed interface FeedSearchResultUiState {
    object NotFound : FeedSearchResultUiState
    object Success : FeedSearchResultUiState
    object Restricted: FeedSearchResultUiState
    object InternalError: FeedSearchResultUiState
    object NoConnection: FeedSearchResultUiState
}


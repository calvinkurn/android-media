package com.tokopedia.feedplus.browse.presentation.model.state

import com.tokopedia.feedplus.browse.presentation.model.srp.FeedSearchResultContent

data class FeedSearchResultUiState(
    val searchKeyword: String,
    val pageState: FeedSearchResultPageState,
    val contents: List<FeedSearchResultContent>,
    val hasNextPage: Boolean,
) {
    companion object {
        val Empty: FeedSearchResultUiState
            get() = FeedSearchResultUiState(
                searchKeyword = "",
                pageState = FeedSearchResultPageState.Unknown,
                contents = emptyList(),
                hasNextPage = true,
            )
    }
}


package com.tokopedia.feedplus.browse.presentation.model.state

import com.tokopedia.feedplus.browse.presentation.model.srp.FeedSearchResultContent

data class FeedSearchResultUiState(
    val searchKeyword: String,
    val pageState: FeedSearchResultPageState,
    val contents: List<FeedSearchResultContent>,
    val hasNextPage: Boolean,
) {
    companion object {

        fun empty(searchKeyword: String) = FeedSearchResultUiState(
            searchKeyword = searchKeyword,
            pageState = FeedSearchResultPageState.UNKNOWN,
            contents = emptyList(),
            hasNextPage = true,
        )
    }
}


package com.tokopedia.feedplus.browse.presentation.model.state

/**
 * Created by Jonathan Darwin on 25 March 2024
 */
sealed interface FeedSearchResultPageState {
    object Unknown : FeedSearchResultPageState
    object NotFound : FeedSearchResultPageState
    object Loading : FeedSearchResultPageState
    object Success : FeedSearchResultPageState
    object Restricted : FeedSearchResultPageState
    object InternalError : FeedSearchResultPageState
    object NoConnection : FeedSearchResultPageState
}

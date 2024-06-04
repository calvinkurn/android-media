package com.tokopedia.feedplus.browse.presentation.model.state

/**
 * Created by Jonathan Darwin on 25 March 2024
 */
enum class FeedSearchResultPageState {
    UNKNOWN,
    NOT_FOUND,
    LOADING,
    SUCCESS,
    RESTRICTED,
    INTERNAL_ERROR;
}

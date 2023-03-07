package com.tokopedia.feedplus.view.subscriber

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct

sealed class FeedDetailViewState {
    data class LoadingState(val isLoading: Boolean, val loadingMore: Boolean) : FeedDetailViewState()

    object SuccessWithNoData : FeedDetailViewState()

    data class Success(val feedXGetActivityProductsResponse: FeedXGetActivityProductsResponse) : FeedDetailViewState()

    data class Error(val error: Throwable) : FeedDetailViewState()
}
package com.tokopedia.feedplus.view.subscriber

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderModel

sealed class FeedDetailViewState {
    data class LoadingState(val isLoading: Boolean, val loadingMore: Boolean) : FeedDetailViewState()

    object SuccessWithNoData : FeedDetailViewState()

    data class Success(val headerModel: FeedDetailHeaderModel, val feedDetailList: List<Visitable<*>>,
                       val hasNextPage: Boolean) : FeedDetailViewState()

    data class Error(val error: Throwable) : FeedDetailViewState()
}
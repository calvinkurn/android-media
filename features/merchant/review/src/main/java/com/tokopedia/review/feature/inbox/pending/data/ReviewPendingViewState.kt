package com.tokopedia.review.feature.inbox.pending.data

sealed class ReviewPendingViewState {
    data class ReviewPendingSuccess(val isEmpty: Boolean, val page: Int): ReviewPendingViewState()
    object ReviewPendingLazyLoadError: ReviewPendingViewState()
    object ReviewPendingInitialLoadError: ReviewPendingViewState()
    object ReviewPendingLoading : ReviewPendingViewState()
}
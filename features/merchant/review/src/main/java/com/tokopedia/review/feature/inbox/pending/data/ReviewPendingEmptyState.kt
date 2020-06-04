package com.tokopedia.review.feature.inbox.pending.data

sealed class ReviewPendingEmptyState {
    object ReviewPendingNoProductsBought : ReviewPendingEmptyState()
    object ReviewPendingNoProductsSearchResult : ReviewPendingEmptyState()
}
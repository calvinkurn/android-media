package com.tokopedia.review.feature.inbox.pending.presentation.util

interface ReviewPendingItemListener {
    fun onCardClicked(reputationId: Int, productId: Int, rating: Int)
}
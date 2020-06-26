package com.tokopedia.review.feature.inbox.pending.presentation.util

interface ReviewPendingItemListener {
    fun onStarsClicked(reputationId: Int, productId: Int, rating: Int)
}
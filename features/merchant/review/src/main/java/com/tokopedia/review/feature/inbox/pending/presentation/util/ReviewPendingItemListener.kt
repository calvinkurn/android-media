package com.tokopedia.review.feature.inbox.pending.presentation.util

interface ReviewPendingItemListener {
    fun trackCardClicked(reputationId: Int, productId: Int)
    fun trackStarsClicked(reputationId: Int, productId: Int, rating: Int)
    fun onStarsClicked(reputationId: Int, productId: Int, rating: Int)
}
package com.tokopedia.review.feature.inbox.pending.presentation.util

import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain

interface ReviewPendingItemListener {
    fun trackCardClicked(reputationId: Int, productId: Int)
    fun trackStarsClicked(reputationId: Int, productId: Int, rating: Int)
    fun onStarsClicked(reputationId: Int, productId: Int, rating: Int, inboxReviewId: Int, seen: Boolean)
    fun onClickOvoIncentiveTickerDescription(productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain)
    fun onDismissOvoIncentiveTicker(title: String)
}
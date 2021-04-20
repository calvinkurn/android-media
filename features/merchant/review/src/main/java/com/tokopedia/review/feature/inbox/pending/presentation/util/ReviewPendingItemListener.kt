package com.tokopedia.review.feature.inbox.pending.presentation.util

import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain

interface ReviewPendingItemListener {
    fun trackCardClicked(reputationId: Long, productId: Long, isEligible: Boolean)
    fun trackStarsClicked(reputationId: Long, productId: Long, rating: Int, isEligible: Boolean)
    fun onStarsClicked(reputationId: Long, productId: Long, rating: Int, inboxReviewId: Long, seen: Boolean)
    fun onClickOvoIncentiveTickerDescription(productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain)
    fun onDismissOvoIncentiveTicker(subtitle: String)
}
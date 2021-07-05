package com.tokopedia.review.feature.inbox.pending.presentation.util

import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain

interface ReviewPendingItemListener {
    fun trackCardClicked(reputationId: String, productId: String, isEligible: Boolean)
    fun trackStarsClicked(reputationId: String, productId: String, rating: Int, isEligible: Boolean)
    fun onStarsClicked(reputationId: String, productId: String, rating: Int, inboxReviewId: String, seen: Boolean)
    fun onClickOvoIncentiveTickerDescription(productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain)
    fun onDismissOvoIncentiveTicker(subtitle: String)
}
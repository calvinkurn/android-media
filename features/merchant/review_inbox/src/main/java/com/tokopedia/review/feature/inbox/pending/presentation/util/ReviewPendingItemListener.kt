package com.tokopedia.review.feature.inbox.pending.presentation.util

import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain

interface ReviewPendingItemListener {
    fun trackCardClicked(reputationId: String, productId: String, isEligible: Boolean)
    fun trackStarsClicked(reputationId: String, productId: String, rating: Int, isEligible: Boolean)
    fun onStarsClicked(reputationId: String, productId: String, rating: Int, inboxReviewId: String, seen: Boolean)
    fun onClickOvoIncentiveTickerDescription(productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain)
    fun onDismissOvoIncentiveTicker(subtitle: String)
    fun onReviewCredibilityWidgetClicked(appLink: String, title: String, position: Int)
    fun onReviewCredibilityWidgetImpressed(title: String, position: Int)
    fun shouldShowCoachMark(): Boolean
    fun updateCoachMark()
    fun onClickBulkReview(title: String, appLink: String)
    fun onClickStarBulkReview(title: String, appLink: String, rating: Int)
    fun onImpressBulkReviewCard(title: String)
}

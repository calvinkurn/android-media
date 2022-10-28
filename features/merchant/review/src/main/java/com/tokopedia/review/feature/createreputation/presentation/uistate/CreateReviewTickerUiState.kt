package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.TickerResponse

sealed interface CreateReviewTickerUiState {
    object Hidden : CreateReviewTickerUiState
    data class Showing(
        val ticker: TickerResponse,
        val trackerData: TrackerData
    ) : CreateReviewTickerUiState {
        data class TrackerData(
            val ovoDomain: ProductRevIncentiveOvoDomain,
            val reputationId: String,
            val orderId: String,
            val productId: String,
            val userId: String,
            val hasIncentive: Boolean,
            val hasOngoingChallenge: Boolean
        )
    }
}
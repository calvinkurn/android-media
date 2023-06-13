package com.tokopedia.review.feature.createreputation.presentation.uistate

import androidx.compose.ui.unit.dp
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating.WidgetReviewAnimatedRatingConfig

sealed interface CreateReviewRatingUiState {
    object Loading : CreateReviewRatingUiState
    data class Showing(
        val widgetConfig: WidgetReviewAnimatedRatingConfig
    ) : CreateReviewRatingUiState {

        companion object {
            private const val STAR_SIZE = 48

            fun create(
                rating: Int,
                onStarClicked: (previousRating: Int, currentRating: Int) -> Unit
            ): Showing {
                return Showing(
                    widgetConfig = WidgetReviewAnimatedRatingConfig(
                        rating = rating,
                        starSize = STAR_SIZE.dp,
                        onStarClicked = onStarClicked
                    )
                )
            }
        }

        data class TrackerData(
            val rating: Int,
            val orderId: String,
            val productId: String,
            val editMode: Boolean,
            val feedbackId: String,
            val successful: Boolean
        ) {
            companion object {
                fun create(
                    rating: Int,
                    orderId: String,
                    productId: String,
                    editMode: Boolean,
                    feedbackId: String
                ): TrackerData {
                    return TrackerData(
                        rating = rating,
                        orderId = orderId,
                        productId = productId,
                        editMode = editMode,
                        feedbackId = feedbackId,
                        successful = true
                    )
                }
            }
        }
    }
}

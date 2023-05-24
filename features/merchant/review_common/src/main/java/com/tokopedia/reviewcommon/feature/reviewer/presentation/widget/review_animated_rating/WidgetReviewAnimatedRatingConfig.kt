package com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class WidgetReviewAnimatedRatingConfig(
    val rating: Int = DEFAULT_RATING,
    val starSize: Dp = DEFAULT_STAR_SIZE.dp,
    val spaceInBetween: Dp = DEFAULT_STAR_SPACE_IN_BETWEEN.dp,
    val skipInitialAnimation: Boolean = false,
    val onStarClicked: (previousRating: Int, currentRating: Int) -> Unit
) {
    companion object {
        private const val DEFAULT_RATING = 0
        private const val DEFAULT_STAR_SIZE = 24
        private const val DEFAULT_STAR_SPACE_IN_BETWEEN = 8
    }
}

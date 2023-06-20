package com.tokopedia.unifyorderhistory.view

import androidx.compose.runtime.MutableState
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating.WidgetReviewAnimatedRatingConfig

sealed interface UohReviewRatingConfig {
    fun getWidgetConfig(): WidgetReviewAnimatedRatingConfig?
    data class Showing(
        val widgetConfig: MutableState<WidgetReviewAnimatedRatingConfig>,
        val appLink: String
    ) : UohReviewRatingConfig {
        fun updateRating(rating: Int) {
            widgetConfig.value = widgetConfig.value.copy(rating = rating)
        }

        override fun getWidgetConfig(): WidgetReviewAnimatedRatingConfig {
            return widgetConfig.value
        }
    }

    object Hidden : UohReviewRatingConfig {
        override fun getWidgetConfig() = null
    }
}

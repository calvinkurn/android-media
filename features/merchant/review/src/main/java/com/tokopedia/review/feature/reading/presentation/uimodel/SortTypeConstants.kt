package com.tokopedia.review.feature.reading.presentation.uimodel

object SortTypeConstants {
    const val MOST_HELPFUL_PARAM = "informative_score desc"
    const val MOST_HELPFUL_COPY = "Paling Membantu"
    private const val LATEST_PARAM = "create_time desc"
    private const val HIGHEST_RATING_PARAM = "rating desc"
    private const val LOWEST_RATING_PARAM = "rating asc"
    private const val LATEST_COPY = "Terbaru"
    private const val HIGHEST_RATING_COPY = "Rating Tertinggi"
    private const val LOWEST_RATING_COPY = "Rating Terendah"

    val sortMap = mapOf(
            MOST_HELPFUL_COPY to MOST_HELPFUL_PARAM,
            HIGHEST_RATING_COPY to HIGHEST_RATING_PARAM,
            LOWEST_RATING_COPY to LOWEST_RATING_PARAM,
            LATEST_COPY to LATEST_PARAM
    )
}
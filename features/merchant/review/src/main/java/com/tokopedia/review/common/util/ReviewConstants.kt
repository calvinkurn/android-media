package com.tokopedia.review.common.util

/**
 * @author by milhamj on 2020-02-14.
 */

object ReviewConstants {
    const val RESULT_INTENT_DETAIL = 772

    const val LAST_WEEK_KEY = "time=7d"
    const val LAST_MONTH_KEY = "time=30d"
    const val LAST_YEAR_KEY = "time=1y"
    const val ALL_KEY = "time=all"

    const val LAST_WEEK_VALUE = "7 Hari Terakhir"
    const val LAST_MONTH_VALUE = "30 Hari Terakhir"
    const val LAST_YEAR_VALUE = "1 Tahun Terakhir"
    const val ALL_VALUE = "Semua"

    const val MOST_REVIEW_KEY = "review_count desc"
    const val HIGHEST_RATING_KEY = "rating_avg desc"
    const val LOWEST_RATING_KEY = "rating_avg asc"

    const val HIGHEST_RATING_DETAIL_KEY = "rating desc"
    const val LOWEST_RATING_DETAIL_KEY = "rating asc"

    const val LATEST_REVIEW_KEY = "create_time desc"

    const val LATEST_REVIEW_VALUE = "Ulasan Terbaru"

    const val MOST_REVIEW_VALUE = "Ulasan Terbanyak"
    const val HIGHEST_RATING_VALUE = "Rating Tertinggi"
    const val LOWEST_RATING_VALUE = "Rating Terendah"

    fun mapSortReviewProduct(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[MOST_REVIEW_KEY] = MOST_REVIEW_VALUE
        map[HIGHEST_RATING_KEY] = HIGHEST_RATING_VALUE
        map[LOWEST_RATING_KEY] = LOWEST_RATING_VALUE
        return map
    }

    fun mapSortReviewDetail(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[LATEST_REVIEW_KEY] = LATEST_REVIEW_VALUE
        map[HIGHEST_RATING_DETAIL_KEY] = HIGHEST_RATING_VALUE
        map[LOWEST_RATING_DETAIL_KEY] = LOWEST_RATING_VALUE
        return map
    }

    fun mapFilterReviewProduct(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[LAST_WEEK_KEY] = LAST_WEEK_VALUE
        map[LAST_MONTH_KEY] = LAST_MONTH_VALUE
        map[LAST_YEAR_KEY] = LAST_YEAR_VALUE
        return map
    }

    fun mapFilterReviewDetail(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[LAST_WEEK_KEY] = LAST_WEEK_VALUE
        map[LAST_MONTH_KEY] = LAST_MONTH_VALUE
        map[LAST_YEAR_KEY] = LAST_YEAR_VALUE
        map[ALL_KEY] = ALL_VALUE
        return map
    }

    const val DEFAULT_PER_PAGE = 10
    const val HAS_TAB_RATING_PRODUCT = "hasTabRatingProduct"
    const val HAS_FILTER_AND_SORT = "hasFilterAndSort"
    const val HAS_OVERALL_RATING_PRODUCT = "hasOverallRatingProduct"

    const val ARGS_REPUTATION_ID = "ARGS_REPUTATION_ID"
    const val ARGS_PRODUCT_ID = "ARGS_PRODUCT_ID"
    const val ARGS_RATING = "ARGS_RATING"
}



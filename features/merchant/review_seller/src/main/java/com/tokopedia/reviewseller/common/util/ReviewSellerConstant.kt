package com.tokopedia.reviewseller.common.util

/**
 * @author by milhamj on 2020-02-14.
 */

object ReviewSellerConstant {
    const val TOPIC_POPULAR_UNSELECTED = false
    const val TOPIC_POPULAR_SELECTED = true

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

    const val MOST_REVIEW_VALUE = "Ulasan Terbanyak"
    const val HIGHEST_RATING_VALUE = "Rating Tertinggi - Terendah"
    const val LOWEST_RATING_VALUE = "Rating Terendah - Tertinggi"

    const val LATEST_REVIEW_VALUE = "Ulasan Terbaru"
    const val HIGHEST_REVIEW_VALUE = "Ulasan Tertinggi"
    const val LOWEST_REVIEW_VALUE = "Ulasan Terendah"

    const val LATEST_REVIEW_KEY = "review desc"
    const val HIGHEST_REVIEW_KEY = "review_count desc"
    const val LOWEST_REVIEW_KEY = "review_count asc"

    fun mapSortReviewProduct(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[LATEST_REVIEW_KEY] = LATEST_REVIEW_VALUE
        map[HIGHEST_REVIEW_KEY] = HIGHEST_REVIEW_VALUE
        map[LOWEST_REVIEW_KEY] = LOWEST_REVIEW_VALUE
        return map
    }

    fun mapSortReviewDetail(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[MOST_REVIEW_KEY] = MOST_REVIEW_VALUE
        map[HIGHEST_RATING_KEY] = HIGHEST_RATING_VALUE
        map[LOWEST_RATING_KEY] = LOWEST_RATING_VALUE
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
}

const val GQL_GET_PRODUCT_REVIEW_LIST = "GQL_GET_PRODUCT_REVIEW_LIST"
const val GQL_GET_PRODUCT_RATING_OVERALL = "GQL_GET_PRODUCT_RATING_OVERALL"
const val GQL_GET_PRODUCT_REVIEW_DETAIL_OVERALL = "GQL_GET_PRODUCT_REVIEW_DETAIL_OVERALL"
const val GQL_GET_PRODUCT_FEEDBACK_LIST_DETAIL = "GQL_GET_PRODUCT_FEEDBACK_LIST_DETAIL"
const val GQL_GET_PRODUCT_FEEDBACK_FILTER = "GQL_GET_PRODUCT_FEEDBACK_FILTER"

package com.tokopedia.reviewseller.common

/**
 * @author by milhamj on 2020-02-14.
 */

object ReviewSellerConstant {

    val mapSortReviewProduct = mutableMapOf<String, String>().apply {
        "review_count desc" to "Ulasan Terbanyak"
        "rating_avg desc" to "Rating Tertinggi - Terendah"
        "rating_avg asc" to "Rating Terendah - Tertinggi"
    }

    val mapRatingReviewProduct = mutableMapOf<String, String>().apply {
        "time=7d" to "7 Hari Terakhir"
        "time=30d" to "30 Hari Terakhir"
        "time=1y" to "1 Tahun Terakhir"
    }

    const val DEFAULT_PER_PAGE = 10
}

const val GQL_GET_PRODUCT_REVIEW_LIST = "GQL_GET_PRODUCT_REVIEW_LIST"
const val GQL_GET_PRODUCT_RATING_OVERALL = "GQL_GET_PRODUCT_RATING_OVERALL"

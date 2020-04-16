package com.tokopedia.reviewseller.common

/**
 * @author by milhamj on 2020-02-14.
 */

object ReviewSellerConstant {

    fun mapSortReviewProduct(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map["review_count desc"] = "Ulasan Terbanyak"
        map["rating_avg desc"] = "Rating Tertinggi - Terendah"
        map["rating_avg asc"] = "Rating Terendah - Tertinggi"
        return map
    }

    fun mapFilterReviewProduct(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map["time=7d"] = "7 Hari Terakhir"
        map["time=30d"] = "30 Hari Terakhir"
        map["time=1y"] = "1 Tahun Terakhir"
        return map
    }

    const val DEFAULT_PER_PAGE = 10
}

const val GQL_GET_PRODUCT_REVIEW_LIST = "GQL_GET_PRODUCT_REVIEW_LIST"
const val GQL_GET_PRODUCT_RATING_OVERALL = "GQL_GET_PRODUCT_RATING_OVERALL"

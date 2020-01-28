package com.tokopedia.recommendation_widget_common.domain.request

import android.text.TextUtils

data class GetRecommendationRequestParam(
        val pageNumber: Int = 1,
        val productIds: List<String> = listOf(),
        val queryParam: String = "",
        val pageName: String = ""
) {
    private val PAGE_NUMBER = "pageNumber"
    private val PAGE_NAME = "pageName"
    private val QUERY_PARAM = "queryParam"
    private val PRODUCT_IDS = "productIDs"

    fun toGqlRequest(): Map<String, Any?> = mapOf(
            PAGE_NUMBER to pageNumber,
            QUERY_PARAM to queryParam,
            PRODUCT_IDS to TextUtils.join(",", productIds),
            PAGE_NAME to pageName
    )
}
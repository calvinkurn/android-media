package com.tokopedia.recommendation_widget_common.domain.request

import android.text.TextUtils

data class GetRecommendationRequestParam(
        val pageNumber: Int = 1,
        val productIds: List<String> = listOf(),
        val queryParam: String = "",
        val pageName: String = "",
        val categoryIds: List<String> = listOf(),
        val xSource: String = "",
        val xDevice: String = "",
        val location: String = "",
        val keywords: List<String> = listOf(),
        val isTokonow: Boolean = false,

) {
    private val PAGE_NUMBER = "pageNumber"
    private val PAGE_NAME = "pageName"
    private val QUERY_PARAM = "queryParam"
    private val PRODUCT_IDS = "productIDs"
    private val CATEGORY_IDS = "categoryIDs"
    private val X_SOURCE = "xSource"
    private val X_DEVICE = "xDevice"
    private val KEYWORDS = "keywords"
    private val PARAM_TOKONOW = "tokoNow"

    fun toGqlRequest(): Map<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PAGE_NUMBER] = pageNumber
        requestMap[QUERY_PARAM] = queryParam
        requestMap[PARAM_TOKONOW] = isTokonow
        if (productIds.isNotEmpty())
            requestMap[PRODUCT_IDS] = TextUtils.join(",", productIds)
        if (categoryIds.isNotEmpty())
            requestMap[CATEGORY_IDS] = TextUtils.join(",", categoryIds)
        requestMap[KEYWORDS] = keywords
        requestMap[PAGE_NAME] = pageName
        if(xSource.isNotEmpty())
            requestMap[X_SOURCE] = xSource
        if(xDevice.isNotEmpty())
            requestMap[X_DEVICE] = xDevice
        return requestMap
    }
}
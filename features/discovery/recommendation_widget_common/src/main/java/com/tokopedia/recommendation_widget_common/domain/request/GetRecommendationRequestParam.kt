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
        var userId: Int = 0,
) {
    fun toGqlRequest(): Map<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PAGE_NUMBER] = pageNumber
        requestMap[QUERY_PARAM] = queryParam
        requestMap[PARAM_TOKONOW] = isTokonow
        if (userId != 0) {
            requestMap[USER_ID] = userId
        }
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

    companion object {
        private const val PAGE_NUMBER = "pageNumber"
        private const val PAGE_NAME = "pageName"
        private const val QUERY_PARAM = "queryParam"
        private const val PRODUCT_IDS = "productIDs"
        private const val CATEGORY_IDS = "categoryIDs"
        private const val X_SOURCE = "xSource"
        private const val X_DEVICE = "xDevice"
        private const val KEYWORDS = "keywords"
        private const val PARAM_TOKONOW = "tokoNow"
        private const val USER_ID = "userID"
    }
}

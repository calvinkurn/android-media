package com.tokopedia.recommendation_widget_common.domain.request

import android.text.TextUtils
import com.tokopedia.productcard.experiments.ProductCardExperiment
import com.tokopedia.recommendation_widget_common.extension.PAGENAME_IDENTIFIER_RECOM_ATC

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
    val shopIds: List<String> = listOf(),
    val criteriaThematicIDs: List<String> = listOf(),
    var hasNewProductCardEnabled: Boolean = false,
) {
    fun toGqlRequest(): Map<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()

        requestMap[PRODUCT_CARD_VERSION] = getProductCardReimagineVersion()
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
        if (xSource.isNotEmpty()) requestMap[X_SOURCE] = xSource
        if (xDevice.isNotEmpty()) requestMap[X_DEVICE] = xDevice
        if (shopIds.isNotEmpty()) requestMap[SHOP_IDS] = TextUtils.join(",", shopIds)

        if (criteriaThematicIDs.isNotEmpty()) {
            requestMap[CRITERIA_THEMATIC_IDS] = criteriaThematicIDs.joinToString(",")
        }
        return requestMap
    }

    fun toViewToViewGqlRequest(): Map<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()

        requestMap[PRODUCT_CARD_VERSION] = getProductCardReimagineVersion()
        requestMap[PAGE_NUMBER] = pageNumber
        requestMap[QUERY_PARAM] = queryParam
        if (userId != 0) {
            requestMap[USER_ID] = userId
        }
        if (productIds.isNotEmpty())
            requestMap[PRODUCT_IDS] = TextUtils.join(",", productIds)
        if (categoryIds.isNotEmpty())
            requestMap[CATEGORY_IDS] = TextUtils.join(",", categoryIds)
        if (xSource.isNotEmpty()) requestMap[X_SOURCE] = xSource
        if (xDevice.isNotEmpty()) requestMap[X_DEVICE] = xDevice
        if (criteriaThematicIDs.isNotEmpty()) {
            requestMap[CRITERIA_THEMATIC_IDS] = criteriaThematicIDs.joinToString(",")
        }
        return requestMap
    }

    private fun getProductCardReimagineVersion(): Int {
        val shouldReimagineEnabled = ProductCardExperiment.isReimagine()
        val isQuantityEditor = pageName.contains(PAGENAME_IDENTIFIER_RECOM_ATC)
        return if (shouldReimagineEnabled && hasNewProductCardEnabled && isQuantityEditor.not()) {
            CARD_REIMAGINE_VERSION
        } else {
            CARD_REVERT_VERSION
        }
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
        private const val SHOP_IDS = "shopIDs"
        private const val PRODUCT_CARD_VERSION = "productCardVersion"
        private const val CRITERIA_THEMATIC_IDS = "criteriaThematicIDs"

        private const val CARD_REIMAGINE_VERSION = 5
        private const val CARD_REVERT_VERSION = 0
    }
}

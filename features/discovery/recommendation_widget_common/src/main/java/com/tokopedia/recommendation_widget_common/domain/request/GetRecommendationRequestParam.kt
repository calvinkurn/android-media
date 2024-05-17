package com.tokopedia.recommendation_widget_common.domain.request

import android.text.TextUtils
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.productcard.experiments.ProductCardExperiment
import com.tokopedia.recommendation_widget_common.byteio.RefreshType

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
    val refreshType: RefreshType = RefreshType.UNKNOWN,
    val bytedanceSessionId: String = "",
    val totalData: Int = 0,
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

        if(refreshType != RefreshType.UNKNOWN) {
            requestMap[REFRESH_TYPE] = refreshType.value.toString()
        }
        requestMap[CURRENT_SESSION_ID] = bytedanceSessionId
        requestMap[ENTER_FROM] = AppLogAnalytics.getLastData(AppLogParam.ENTER_FROM)?.toString().orEmpty()
        requestMap[SOURCE_PAGE_TYPE] = AppLogAnalytics.getLastData(AppLogParam.SOURCE_PAGE_TYPE)?.toString().orEmpty()
        return requestMap
    }

    private fun getProductCardReimagineVersion(): Int {
        val shouldReimagineEnabled = ProductCardExperiment.isReimagine()
        return if (shouldReimagineEnabled && hasNewProductCardEnabled) {
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
        private const val REFRESH_TYPE = "refreshType"
        private const val CURRENT_SESSION_ID = "currentSessionID"
        private const val ENTER_FROM = "enterFrom"
        private const val SOURCE_PAGE_TYPE = "sourcePageType"

        private const val CARD_REIMAGINE_VERSION = 5
        private const val CARD_REVERT_VERSION = 0

        const val X_SOURCE_DEFAULT_VALUE = "recom_widget"
    }
}

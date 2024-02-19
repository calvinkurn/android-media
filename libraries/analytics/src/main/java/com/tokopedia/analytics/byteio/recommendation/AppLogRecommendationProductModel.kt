package com.tokopedia.analytics.byteio.recommendation

/**
 * Byte.io tracking model
 */
data class AppLogRecommendationProductModel (
    val productId: String,
    val listName: String?,
    val listNum: Int?,
    val sourceModule: String,
    val trackId: String,
    val isAd: Int,
    val isUseCache: Int,
    val recParams: String,
    val requestId: String,
    val shopId: String,
) {
    companion object {
        fun create(
            productId: String = "",
            position: Int = 0,
            tabName: String = "",
            tabPosition: Int = 0,
            sourceModule: String = "",
            isAd: Boolean = false,
            isUseCache: Boolean = false,
            recParams: String = "",
            requestId: String = "",
            shopId: String = "",
        ): AppLogRecommendationProductModel {
            return AppLogRecommendationProductModel(
                productId = productId,
                listName = tabName,
                listNum = tabPosition.inc(),
                sourceModule = sourceModule,
                trackId = "${requestId}_${productId}_${position.inc()}",
                isAd = if(isAd) 1 else 0,
                isUseCache = if(isUseCache) 1 else 0,
                recParams = recParams,
                requestId = requestId,
                shopId = shopId,
            )
        }
    }
}

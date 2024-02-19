package com.tokopedia.analytics.byteio.recommendation

/**
 * Byte.io tracking model
 */
data class AppLogRecommendationCardModel (
    val cardName: String,
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
    val groupId: String,
) {
    companion object {
        fun create(
            cardName: String = "",
            productId: String = "",
            tabName: String = "",
            tabPosition: Int = 0,
            sourceModule: String = "",
            trackId: String = "",
            isAd: Boolean = false,
            isUseCache: Boolean = false,
            recParams: String = "",
            requestId: String = "",
            shopId: String = "",
            groupId: String = "",
        ): AppLogRecommendationCardModel {
            return AppLogRecommendationCardModel(
                cardName = cardName,
                productId = productId,
                listName = tabName,
                listNum = tabPosition.inc(),
                sourceModule = sourceModule,
                trackId = trackId,
                isAd = if(isAd) 1 else 0,
                isUseCache = if(isUseCache) 1 else 0,
                recParams = recParams,
                requestId = requestId,
                shopId = shopId,
                groupId = groupId,
            )
        }
    }
}

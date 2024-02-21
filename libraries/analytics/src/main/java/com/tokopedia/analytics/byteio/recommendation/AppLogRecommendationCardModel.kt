package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics.addEntranceForm
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogAnalytics.addSourceModule
import com.tokopedia.analytics.byteio.AppLogAnalytics.addSourcePageType
import com.tokopedia.analytics.byteio.AppLogParam
import org.json.JSONObject

/**
 * Byte.io tracking model
 */
data class AppLogRecommendationCardModel (
    val cardName: String,
    val productId: String,
    val listName: String?,
    val listNum: Int?,
    val sourceModule: String,
    val isAd: Int,
    val isUseCache: Int,
    val recParams: String,
    val requestId: String,
    val shopId: String,
    val groupId: String,
    val itemOrder: Int,
) {

    fun toJson() = JSONObject().apply {
        put(AppLogParam.CARD_NAME, cardName)
        put(AppLogParam.LIST_NAME, listName)
        put(AppLogParam.LIST_NUM, listNum)
        put(AppLogParam.SOURCE_MODULE, sourceModule)
        put(AppLogParam.PRODUCT_ID, productId)
        put(AppLogParam.IS_AD, isAd)
        put(AppLogParam.IS_USE_CACHE, isUseCache)
        put(AppLogParam.REQUEST_ID, requestId)
        put(AppLogParam.SHOP_ID, shopId)
        put(AppLogRecommendationConst.REC_PARAMS, recParams)
        put(AppLogParam.GROUP_ID, groupId)
        put(AppLogParam.ITEM_ORDER, itemOrder)
        addPage()
        addEntranceForm()
        addSourcePageType()
        addSourceModule()
    }

    companion object {
        fun create(
            cardName: String = "",
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
            groupId: String = "",
        ): AppLogRecommendationCardModel {
            return AppLogRecommendationCardModel(
                cardName = cardName,
                productId = productId,
                listName = tabName,
                listNum = tabPosition.inc(),
                sourceModule = sourceModule,
                isAd = if(isAd) 1 else 0,
                isUseCache = if(isUseCache) 1 else 0,
                recParams = recParams,
                requestId = requestId,
                shopId = shopId,
                groupId = groupId,
                itemOrder = position.inc(),
            )
        }
    }
}

package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.ActionType
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEntranceForm
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
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
    val moduleName: String,
    val isAd: Int,
    val isUseCache: Int,
    val recSessionId: String,
    val recParams: String,
    val requestId: String,
    val shopId: String,
    val groupId: String,
    val itemOrder: Int,
    val type: AppLogRecommendationType,
) {

    fun toShowClickJson() = JSONObject().apply {
        put(AppLogParam.CARD_NAME, cardName)
        put(AppLogParam.LIST_NAME, listName)
        put(AppLogParam.LIST_NUM, listNum)
        put(AppLogParam.SOURCE_MODULE, moduleName)
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
    }

    fun toRecTriggerJson() = JSONObject().apply {
        addPage()
        put(AppLogParam.GLIDE_DISTANCE, "0")

        put(AppLogParam.LIST_NAME, listName)
        put(AppLogParam.LIST_NUM, listNum)

        put(AppLogParam.ACTION_TYPE, ActionType.CLICK_CARD)
        put(AppLogParam.MODULE_NAME, moduleName)
        put(AppLogParam.REC_SESSION_ID, recSessionId)
        put(AppLogParam.REQUEST_ID, requestId)
    }

    companion object {
        fun create(
            cardName: String = "",
            productId: String = "",
            position: Int = 0,
            tabName: String = "",
            tabPosition: Int = 0,
            moduleName: String = "",
            isAd: Boolean = false,
            isUseCache: Boolean = false,
            recSessionId: String = "",
            recParams: String = "",
            requestId: String = "",
            shopId: String = "",
            groupId: String = "",
            type: AppLogRecommendationType,
        ): AppLogRecommendationCardModel {
            return AppLogRecommendationCardModel(
                cardName = cardName,
                productId = productId,
                listName = tabName,
                listNum = tabPosition.inc(),
                moduleName = moduleName,
                isAd = if(isAd) 1 else 0,
                isUseCache = if(isUseCache) 1 else 0,
                recSessionId = recSessionId,
                recParams = recParams,
                requestId = requestId,
                shopId = shopId,
                groupId = groupId,
                itemOrder = position.inc(),
                type = type,
            )
        }
    }
}

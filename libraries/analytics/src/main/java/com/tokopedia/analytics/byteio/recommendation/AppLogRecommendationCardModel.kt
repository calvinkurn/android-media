package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.ActionType
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogAnalytics.addSourcePageType
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.EntranceForm
import org.json.JSONObject

/**
 * Byte.io tracking model
 */
data class AppLogRecommendationCardModel(
    val cardName: CardName,
    val cardType: String,
    val productId: String,
    val listName: String?,
    val listNum: Int?,
    val moduleName: String,
    val sourceModule: String,
    val trackId: String,
    val isAd: Int,
    val isUseCache: Int,
    val recSessionId: String,
    val recParams: String,
    val requestId: String,
    val shopId: String,
    val groupId: String,
    val itemOrder: Int,
    val entranceForm: EntranceForm,
    val volume: Int? = null,
    val rate: Float? = null,
    val originalPrice: Float? = null,
    val salesPrice: Float? = null,
    val type: AppLogRecommendationType
) {

    fun toShowClickJson() = JSONObject().apply {
        put(AppLogParam.CARD_NAME, cardName.str.format(cardType))
        put(AppLogParam.PRODUCT_ID, productId)
        put(AppLogParam.LIST_NAME, listName)
        put(AppLogParam.LIST_NUM, listNum)
        put(AppLogParam.IS_AD, isAd)
        put(AppLogParam.IS_USE_CACHE, isUseCache)
        put(AppLogParam.SHOP_ID, shopId)

        put(AppLogParam.ITEM_ORDER, itemOrder)
        put(AppLogParam.TRACK_ID, trackId)

        put(AppLogParam.REQUEST_ID, requestId)
        put(AppLogRecommendationConst.REC_PARAMS, recParams)

        put(AppLogParam.ENTRANCE_FORM, entranceForm.str)
        put(AppLogParam.SOURCE_MODULE, sourceModule)

        addPage()
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
            cardId: String = "",
            productId: String = "",
            cardName: CardName,
            cardType: String = "",
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
            entranceForm: EntranceForm,
            volume: Int? = null,
            rate: Float? = null,
            originalPrice: Float? = null,
            salesPrice: Float? = null,
            type: AppLogRecommendationType
        ): AppLogRecommendationCardModel {
            return AppLogRecommendationCardModel(
                cardName = cardName,
                cardType = cardType,
                productId = productId,
                listName = tabName,
                listNum = tabPosition.inc(),
                moduleName = moduleName,
                sourceModule = constructSourceModule(false, isAd, moduleName, type),
                trackId = "${requestId}_${cardId}_${position.inc()}",
                isAd = if (isAd) 1 else 0,
                isUseCache = if (isUseCache) 1 else 0,
                recSessionId = recSessionId,
                recParams = recParams,
                requestId = requestId,
                shopId = shopId,
                groupId = groupId,
                itemOrder = position.inc(),
                entranceForm = entranceForm,
                volume = volume,
                rate = rate,
                originalPrice = originalPrice,
                salesPrice = salesPrice,
                type = type
            )
        }
    }
}

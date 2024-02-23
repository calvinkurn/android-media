package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.ActionType
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterFrom
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.SourcePageType
import org.json.JSONObject

/**
 * Byte.io tracking model
 */
data class AppLogRecommendationCardModel(
    val cardName: CardName,
    val cardType: String,
    val productId: String,
    val listName: String,
    val listNum: Int,
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
    val entranceForm: String,
    val volume: Int?,
    val rate: Float?,
    val originalPrice: Float?,
    val salesPrice: Float?,
    val sourcePageType: String,
    val type: AppLogRecommendationType,
    val enterMethod: String,
    val authorId: String,
) {

    fun toShowClickJson() = JSONObject().apply {
        addPage()
        put(AppLogParam.CARD_NAME, cardName.str.format(cardType))
        put(AppLogParam.LIST_NAME, listName)
        put(AppLogParam.LIST_NUM, listNum)
        addEnterFrom()
        put(AppLogParam.SOURCE_PAGE_TYPE, sourcePageType)
        put(AppLogParam.SOURCE_MODULE, sourceModule)
        put(AppLogParam.AUTHOR_ID, authorId)
        put(AppLogParam.PRODUCT_ID, productId)
        put(AppLogParam.IS_AD, isAd)
        put(AppLogParam.IS_USE_CACHE, isUseCache)
        put(AppLogParam.TRACK_ID, trackId)
        put(AppLogParam.REQUEST_ID, requestId)
        put(AppLogParam.REC_PARAMS, recParams)
        put(AppLogParam.SHOP_ID, shopId)
        put(AppLogParam.ITEM_ORDER, itemOrder)
        put(AppLogParam.VOLUME, volume)
        put(AppLogParam.ORIGINAL_PRICE, originalPrice)
        put(AppLogParam.SALES_PRICE, salesPrice)
        //TODO P1: group_id, main_video_id
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
            volume: Int = 0,
            rate: Float = 0f,
            originalPrice: Float = 0f,
            salesPrice: Float = 0f,
            type: AppLogRecommendationType,
            enterMethod: String = "",
            sourcePageType: SourcePageType? = null,
            authorId: String = "",
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
                entranceForm = entranceForm.str,
                volume = volume,
                rate = rate,
                originalPrice = originalPrice,
                salesPrice = salesPrice,
                type = type,
                enterMethod = enterMethod,
                sourcePageType = sourcePageType?.str ?: AppLogAnalytics.getCurrentData(AppLogParam.PAGE_NAME).toString(),
                authorId = authorId,
            )
        }
    }
}

package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.ActionType
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterFrom
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterMethod
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogAnalytics.intValue
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.analytics.byteio.util.spacelessParam
import com.tokopedia.analytics.byteio.util.underscoredParam
import org.json.JSONObject

/**
 * Byte.io tracking model
 */
data class AppLogRecommendationProductModel(
    val productId: String,
    val listName: String,
    val listNum: String,
    val moduleName: String,
    val sourceModule: String,
    val isAd: Int,
    val isUseCache: Int,
    val recSessionId: String,
    val recParams: String,
    val requestId: String,
    val shopId: String,
    val itemOrder: Int,
    val entranceForm: String,
    val volume: Int,
    val rate: Float,
    val originalPrice: Float,
    val salesPrice: Float,
    val enterMethod: String?,
    val authorId: String,
    val groupId: String,
    val cardName: String,
    val isEligibleForRecTrigger: Boolean,
    val additionalParam: AppLogAdditionalParam,
) {

    val trackId = constructTrackId(null, productId, requestId, itemOrder, cardName)

    fun asCardModel() = AppLogRecommendationCardModel(
        cardId = "",
        productId = productId,
        listName = listName,
        listNum = listNum,
        moduleName = moduleName,
        sourceModule = sourceModule,
        isAd = isAd,
        isUseCache = isUseCache,
        recSessionId = recSessionId,
        recParams = recParams,
        requestId = requestId,
        shopId = shopId,
        cardName = cardName,
        groupId = groupId,
        itemOrder = itemOrder,
        entranceForm = entranceForm,
        sourcePageType = SourcePageType.PRODUCT_CARD,
        enterMethod = enterMethod,
        originalPrice = originalPrice,
        salesPrice = salesPrice,
        rate = rate,
        volume = volume,
        authorId = authorId,
        additionalParam = additionalParam,
    )

    fun toShowClickJson() = JSONObject(additionalParam.parameters).apply {
        addPage()
        put(AppLogParam.LIST_NAME, listName)
        put(AppLogParam.LIST_NUM, listNum)
        addEnterFrom()
        put(AppLogParam.SOURCE_PAGE_TYPE, SourcePageType.PRODUCT_CARD)
        put(AppLogParam.ENTRANCE_FORM, entranceForm)
        put(AppLogParam.SOURCE_MODULE, sourceModule)
        addEnterMethod()
        put(AppLogParam.AUTHOR_ID, authorId)
        put(AppLogParam.PRODUCT_ID, productId)
        put(AppLogParam.IS_AD, isAd)
        put(AppLogParam.IS_USE_CACHE, isUseCache)
        put(AppLogParam.GROUP_ID, groupId)
        put(AppLogParam.TRACK_ID, trackId)
        put(AppLogParam.REC_PARAMS, recParams)
        put(AppLogParam.REQUEST_ID, requestId)
        put(AppLogParam.SHOP_ID, shopId)
        put(AppLogParam.ITEM_ORDER, itemOrder)
        put(AppLogParam.REC_SESSION_ID, recSessionId)
        if(volume > 0) put(AppLogParam.VOLUME, volume)
        if(rate > 0) put(AppLogParam.RATE, rate)
        if(originalPrice > 0) put(AppLogParam.ORIGINAL_PRICE, originalPrice)
        if(salesPrice > 0) put(AppLogParam.SALES_PRICE, salesPrice)
        //TODO P1: group_id, main_video_id
    }

    fun toRecTriggerJson() = JSONObject(additionalParam.parameters).apply {
        addPage()
        addEnterFrom()
        put(AppLogParam.LIST_NAME, listName)
        put(AppLogParam.LIST_NUM, listNum)
        put(AppLogParam.ACTION_TYPE, ActionType.CLICK_CARD)
        put(AppLogParam.MODULE_NAME, moduleName)
        put(AppLogParam.GLIDE_DISTANCE, 0)
        put(AppLogParam.REC_SESSION_ID, recSessionId)
        put(AppLogParam.REQUEST_ID, requestId)
    }

    companion object {
        fun create(
            productId: String = "",
            parentProductId: String = "",
            position: Int = 0,
            tabName: String = "",
            tabPosition: Int = -1,
            moduleName: String = "",
            isAd: Boolean = false,
            isUseCache: Boolean = false,
            recSessionId: String = "",
            recParams: String = "",
            requestId: String = "",
            shopId: String = "",
            entranceForm: EntranceForm,
            volume: Int = 0,
            rate: Float = 0f,
            originalPrice: Float = 0f,
            salesPrice: Float = 0f,
            enterMethod: String? = null,
            authorId: String = "",
            groupId: String = "",
            cardName: String = CardName.REC_GOODS_CARD,
            isTrackAsHorizontalSourceModule : Boolean = false,
            isEligibleForRecTrigger: Boolean = false,
            additionalParam: AppLogAdditionalParam = AppLogAdditionalParam.None,
        ): AppLogRecommendationProductModel {
            return AppLogRecommendationProductModel(
                productId = getProductId(productId, parentProductId),
                listName = tabName.underscoredParam(),
                listNum = tabPosition.inc().zeroAsEmpty(),
                moduleName = moduleName,
                sourceModule = constructSourceModule(isAd, moduleName, entranceForm, isTrackAsHorizontalSourceModule),
                isAd = isAd.intValue,
                isUseCache = isUseCache.intValue,
                recSessionId = recSessionId,
                recParams = recParams,
                requestId = requestId,
                shopId = shopId.zeroAsEmpty(),
                itemOrder = position.inc(),
                entranceForm = entranceForm.str,
                volume = volume,
                rate = rate,
                originalPrice = originalPrice,
                salesPrice = salesPrice,
                enterMethod = enterMethod,
                authorId = authorId.zeroAsEmpty(),
                groupId = groupId.zeroAsEmpty(),
                cardName = getCardName(cardName, isAd).spacelessParam(),
                isEligibleForRecTrigger = isEligibleForRecTrigger,
                additionalParam = additionalParam,
            )
        }
    }
}

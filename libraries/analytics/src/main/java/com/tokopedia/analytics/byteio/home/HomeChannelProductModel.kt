package com.tokopedia.analytics.byteio.home

import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterFrom
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterMethod
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.analytics.byteio.recommendation.zeroAsEmpty
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import org.json.JSONObject

data class HomeChannelProductModel(
    val listName: String,
    val listNum: String,
    val entranceForm: String,
    val sourceModule: String,
    val enterMethod: String?,
    val productId: String,
    val isAd: Int,
    val isUseCache: Int,
    val trackId: String,
    val recSessionId: String,
    val recParams: String,
    val requestId: String,
    val shopId: String,
    val itemOrder: String,
    val cardName: String,
) {

    fun asCardModel(): HomeChannelCardModel {
        return HomeChannelCardModel(
            listName = listName,
            listNum = listNum,
            cardName = cardName,
            sourceModule = sourceModule,
            productId = productId.zeroAsEmpty(),
            isAd = isAd,
            isUseCache = isUseCache,
            trackId = trackId,
            requestId = requestId,
            recSessionId = recSessionId,
            recParams = recParams,
            shopId = shopId,
            itemOrder = itemOrder,
            enterMethod = enterMethod,
            entranceForm = entranceForm
        )
    }

    fun toShowClickJson() = JSONObject().apply {
        addPage()
        put(AppLogParam.LIST_NAME, listName)
        put(AppLogParam.LIST_NUM, listNum)
        addEnterFrom()
        put(AppLogParam.SOURCE_PAGE_TYPE, SourcePageType.PRODUCT_CARD)
        put(AppLogParam.ENTRANCE_FORM, entranceForm)
        put(AppLogParam.SOURCE_MODULE, sourceModule)
        addEnterMethod()
        put(AppLogParam.PRODUCT_ID, productId)
        put(AppLogParam.IS_AD, isAd)
        put(AppLogParam.IS_USE_CACHE, isUseCache)
        put(AppLogParam.TRACK_ID, trackId)
        put(AppLogParam.REC_SESSION_ID, recSessionId)
        put(AppLogParam.REC_PARAMS, recParams)
        put(AppLogParam.REQUEST_ID, requestId)
        put(AppLogParam.SHOP_ID, shopId)
        put(AppLogParam.ITEM_ORDER, itemOrder.toIntOrZero())
    }
}

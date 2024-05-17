package com.tokopedia.analytics.byteio.home

import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterFrom
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.analytics.byteio.recommendation.zeroAsEmpty
import org.json.JSONObject

data class HomeChannelCardModel(
    val listName: String,
    val listNum: String,
    val cardName: String,
    val sourceModule: String,
    val productId: String,
    val isAd: Int,
    val isUseCache: Int,
    val trackId: String,
    val requestId: String,
    val recSessionId: String,
    val recParams: String,
    val shopId: String,
    val itemOrder: String,
    val entranceForm: String,
    val enterMethod: String?,
) {

    fun toShowClickJson() = JSONObject().apply {
        addPage()
        put(AppLogParam.CARD_NAME, cardName)
        put(AppLogParam.LIST_NAME, listName)
        put(AppLogParam.LIST_NUM, listNum)
        addEnterFrom()
        put(AppLogParam.SOURCE_PAGE_TYPE, SourcePageType.PRODUCT_CARD)
        put(AppLogParam.SOURCE_MODULE, sourceModule)
        put(AppLogParam.PRODUCT_ID, productId)
        put(AppLogParam.IS_AD, isAd)
        put(AppLogParam.IS_USE_CACHE, isUseCache)
        put(AppLogParam.TRACK_ID, trackId)
        put(AppLogParam.REQUEST_ID, requestId)
        put(AppLogParam.REC_SESSION_ID, recSessionId)
        put(AppLogParam.REC_PARAMS, recParams)
        put(AppLogParam.SHOP_ID, shopId)
        put(AppLogParam.ITEM_ORDER, itemOrder.toInt())
    }
}

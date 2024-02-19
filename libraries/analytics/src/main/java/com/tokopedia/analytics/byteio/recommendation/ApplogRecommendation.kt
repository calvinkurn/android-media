package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogParam.CARD_NAME
import com.tokopedia.analytics.byteio.AppLogParam.GROUP_ID
import com.tokopedia.analytics.byteio.AppLogParam.IS_AD
import com.tokopedia.analytics.byteio.AppLogParam.IS_USE_CACHE
import com.tokopedia.analytics.byteio.AppLogParam.LIST_NAME
import com.tokopedia.analytics.byteio.AppLogParam.LIST_NUM
import com.tokopedia.analytics.byteio.AppLogParam.PRODUCT_ID
import com.tokopedia.analytics.byteio.AppLogParam.REQUEST_ID
import com.tokopedia.analytics.byteio.AppLogParam.SHOP_ID
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_MODULE
import com.tokopedia.analytics.byteio.AppLogParam.TRACK_ID
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.SourceModule
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.REC_PARAMS
import org.json.JSONObject

/**
 * Byte.io tracking
 */
object AppLogRecommendation {

    private fun buildProductParams(model: AppLogRecommendationProductModel): JSONObject {
        return JSONObject().apply {
            put(LIST_NAME, model.listName)
            put(LIST_NUM, model.listNum)
            put(SOURCE_MODULE, model.sourceModule)
            put(TRACK_ID, model.trackId)
            put(PRODUCT_ID, model.productId)
            put(IS_AD, model.isAd)
            put(IS_USE_CACHE, model.isUseCache)
            put(REQUEST_ID, model.requestId)
            put(SHOP_ID, model.shopId)
            put(REC_PARAMS, model.recParams)
            addPage()
        }
    }

    fun sendProductShowAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.send(
            EventName.PRODUCT_SHOW,
            buildProductParams(model)
        )
    }

    fun sendProductClickAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.globalTrackId = model.trackId
        AppLogAnalytics.globalRequestId = model.requestId
        AppLogAnalytics.sourcePageType = SourcePageType.PRODUCT_CARD
        AppLogAnalytics.entranceForm = EntranceForm.PURE_GOODS_CARD
        AppLogAnalytics.sourceModule = SourceModule.HOME_FOR_YOU
        AppLogAnalytics.send(
            EventName.PRODUCT_CLICK,
            buildProductParams(model)
        )
    }

    private fun buildCardParams(model: AppLogRecommendationCardModel): JSONObject {
        return JSONObject().apply {
            put(CARD_NAME, model.cardName)
            put(LIST_NAME, model.listName)
            put(LIST_NUM, model.listNum)
            put(SOURCE_MODULE, model.sourceModule)
            put(TRACK_ID, model.trackId)
            put(PRODUCT_ID, model.productId)
            put(IS_AD, model.isAd)
            put(IS_USE_CACHE, model.isUseCache)
            put(REQUEST_ID, model.requestId)
            put(SHOP_ID, model.shopId)
            put(REC_PARAMS, model.recParams)
            put(REC_PARAMS, model.recParams)
            put(GROUP_ID, model.groupId)
            addPage()
        }
    }

    fun sendCardShowAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.send(
            EventName.CARD_SHOW,
            buildCardParams(model)
        )
    }

    fun sendCardClickAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.globalTrackId = model.trackId
        AppLogAnalytics.globalRequestId = model.requestId
        AppLogAnalytics.sourcePageType = SourcePageType.PRODUCT_CARD
        AppLogAnalytics.entranceForm = EntranceForm.PURE_GOODS_CARD
        AppLogAnalytics.send(
            EventName.CARD_CLICK,
            buildCardParams(model)
        )
    }
}

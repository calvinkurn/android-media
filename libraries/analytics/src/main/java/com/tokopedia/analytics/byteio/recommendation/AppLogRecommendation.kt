package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogParam.CARD_NAME
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.AppLogParam.EVENT_ORIGIN_FEATURE
import com.tokopedia.analytics.byteio.AppLogParam.EVENT_ORIGIN_FEATURE_DEFAULT_VALUE
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
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.REC_PARAMS
import org.json.JSONObject

/**
 * Byte.io tracking
 */
object AppLogRecommendation {

    fun sendProductShowAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.send(
            EventName.PRODUCT_SHOW,
            buildProductParams(model)
        )
    }

    fun sendProductClickAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.send(
            EventName.PRODUCT_CLICK,
            buildProductParams(model)
        )
    }

    private fun buildProductParams(model: AppLogRecommendationProductModel): JSONObject {
        return JSONObject().apply {
            put(EVENT_ORIGIN_FEATURE, EVENT_ORIGIN_FEATURE_DEFAULT_VALUE)
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
            put(ENTER_FROM, "homepage")
            addPage()
        }
    }

    fun sendCardShowAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.send(
            EventName.PRODUCT_SHOW,
            buildCardParams(model)
        )
    }

    fun sendCardClickAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.globalTrackId = model.trackId
        AppLogAnalytics.globalRequestId = model.requestId
        AppLogAnalytics.send(
            EventName.PRODUCT_CLICK,
            buildCardParams(model)
        )
    }

    private fun buildCardParams(model: AppLogRecommendationCardModel): JSONObject {
        return JSONObject().apply {
            EVENT_ORIGIN_FEATURE to EVENT_ORIGIN_FEATURE_DEFAULT_VALUE
            ENTER_FROM to "homepage"
            CARD_NAME to model.cardName
            LIST_NAME to model.listName
            LIST_NUM to model.listNum
            SOURCE_MODULE to model.sourceModule
            TRACK_ID to model.trackId
            PRODUCT_ID to model.productId
            IS_AD to model.isAd
            IS_USE_CACHE to model.isUseCache
            REQUEST_ID to model.requestId
            SHOP_ID to model.shopId
            REC_PARAMS to model.recParams
            REC_PARAMS to model.recParams
            GROUP_ID to model.groupId
            addPage()
        }
    }
}

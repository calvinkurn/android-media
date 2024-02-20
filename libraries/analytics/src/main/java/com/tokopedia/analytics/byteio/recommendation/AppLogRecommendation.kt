package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.SourceModule
import com.tokopedia.analytics.byteio.SourcePageType

/**
 * Byte.io tracking
 */
object AppLogRecommendation {

    fun sendProductShowAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.send(EventName.PRODUCT_SHOW, model.toJson())
    }

    fun sendProductClickAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.globalTrackId = model.trackId
        AppLogAnalytics.globalRequestId = model.requestId
        AppLogAnalytics.sourcePageType = SourcePageType.PRODUCT_CARD
        AppLogAnalytics.entranceForm = EntranceForm.PURE_GOODS_CARD
        AppLogAnalytics.sourceModule = SourceModule.HOME_FOR_YOU
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, model.toJson())
    }

    fun sendCardShowAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.send(EventName.CARD_SHOW, model.toJson())
    }

    fun sendCardClickAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.globalTrackId = model.trackId
        AppLogAnalytics.globalRequestId = model.requestId
        AppLogAnalytics.sourcePageType = SourcePageType.PRODUCT_CARD
        AppLogAnalytics.entranceForm = EntranceForm.PURE_GOODS_CARD
        AppLogAnalytics.send(EventName.CARD_CLICK, model.toJson())
    }
}

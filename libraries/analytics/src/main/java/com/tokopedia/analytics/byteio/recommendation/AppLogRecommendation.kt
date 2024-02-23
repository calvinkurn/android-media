package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.SourcePageType
import org.json.JSONObject

/**
 * Byte.io recommendation tracking
 * https://bytedance.sg.larkoffice.com/docx/MSiydFty1o0xIYxUe4LltuRHgue
 */
object AppLogRecommendation {

    fun sendProductShowAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.send(EventName.PRODUCT_SHOW, model.toShowClickJson())
        if (model.type == AppLogRecommendationType.MIXED_CAROUSEL) {
            AppLogAnalytics.send(EventName.CARD_SHOW, model.asCardModel().toShowClickJson())
        }
    }

    fun sendProductClickAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.globalTrackId = model.trackId
        AppLogAnalytics.globalRequestId = model.requestId
        AppLogAnalytics.sourcePageType = SourcePageType.PRODUCT_CARD
        AppLogAnalytics.entranceForm = EntranceForm.PURE_GOODS_CARD
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, model.toShowClickJson())
        if (model.type == AppLogRecommendationType.MIXED_CAROUSEL) {
            AppLogAnalytics.send(EventName.CARD_CLICK, model.asCardModel().toShowClickJson())
        }
        if (model.type == AppLogRecommendationType.VERTICAL) {
            AppLogAnalytics.send(EventName.REC_TRIGGER, model.toRecTriggerJson())
        }
    }

    fun sendCardShowAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.send(EventName.CARD_SHOW, model.toShowClickJson())
    }

    fun sendCardClickAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.globalTrackId = null
        AppLogAnalytics.globalRequestId = model.requestId
        AppLogAnalytics.sourcePageType = SourcePageType.PRODUCT_CARD
        AppLogAnalytics.entranceForm = EntranceForm.PURE_GOODS_CARD
        AppLogAnalytics.send(EventName.CARD_CLICK, model.toShowClickJson())
        if (model.type == AppLogRecommendationType.VERTICAL) {
            AppLogAnalytics.send(EventName.REC_TRIGGER, model.toRecTriggerJson())
        }
    }

    fun sendEnterPageAppLog() {
        AppLogAnalytics.send(
            EventName.ENTER_PAGE,
            JSONObject().apply {
                // TODO enter_from, enter_method
                addPage()
            }
        )
    }
}

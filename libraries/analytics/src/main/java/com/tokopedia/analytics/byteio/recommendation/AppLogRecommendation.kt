package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogParam
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
        if (model.entranceForm == EntranceForm.MISSION_HORIZONTAL_GOODS_CARD.str ||
            model.entranceForm == EntranceForm.PURE_GOODS_CARD.str) {
            AppLogAnalytics.send(EventName.CARD_SHOW, model.asCardModel().toShowClickJson())
        }
    }

    fun sendProductClickAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, model.toShowClickJson())
        if (model.entranceForm == EntranceForm.MISSION_HORIZONTAL_GOODS_CARD.str ||
            model.entranceForm == EntranceForm.PURE_GOODS_CARD.str) {
            AppLogAnalytics.send(EventName.CARD_CLICK, model.asCardModel().toShowClickJson())
        }
        if (model.entranceForm == EntranceForm.PURE_GOODS_CARD.str ||
            model.entranceForm == EntranceForm.CONTENT_GOODS_CARD.str ||
            model.entranceForm == EntranceForm.DETAIL_GOODS_CARD.str ) {
            AppLogAnalytics.send(EventName.REC_TRIGGER, model.toRecTriggerJson())
        }
        model.setGlobalParams()
    }

    fun sendCardShowAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.send(EventName.CARD_SHOW, model.toShowClickJson())
    }

    fun sendCardClickAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.send(EventName.CARD_CLICK, model.toShowClickJson())
        if (model.entranceForm == EntranceForm.PURE_GOODS_CARD.str) {
            AppLogAnalytics.send(EventName.REC_TRIGGER, model.toRecTriggerJson())
        }
        model.setGlobalParams()
    }

    fun sendEnterPageAppLog() {
        AppLogAnalytics.send(
            EventName.ENTER_PAGE,
            JSONObject().apply {
                AppLogAnalytics.getLastData(AppLogParam.ENTER_FROM)
                AppLogAnalytics.getLastData(AppLogParam.ENTER_METHOD)
                addPage()
            }
        )
    }

    private fun AppLogRecommendationProductModel.setGlobalParams() {
        AppLogAnalytics.setGlobalParams(
            entranceForm = entranceForm,
            enterMethod = enterMethod,
            sourceModule = sourceModule,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = SourcePageType.PRODUCT_CARD,
            requestId = requestId,
        )
    }

    private fun AppLogRecommendationCardModel.setGlobalParams() {
        AppLogAnalytics.setGlobalParams(
            entranceForm = entranceForm,
            enterMethod = enterMethod,
            sourceModule = sourceModule,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = SourcePageType.PRODUCT_CARD,
            requestId = requestId,
        )
    }
}

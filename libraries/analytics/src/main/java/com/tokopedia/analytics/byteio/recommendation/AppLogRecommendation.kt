package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_METHOD
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
        if (model.shouldSendCardEvent()) {
            AppLogAnalytics.send(EventName.CARD_SHOW, model.asCardModel().toShowClickJson())
        }
    }

    fun sendProductClickAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, model.toShowClickJson())
        if (model.shouldSendCardEvent()) {
            AppLogAnalytics.send(EventName.CARD_CLICK, model.asCardModel().toShowClickJson())
        }
        if (shouldSendRecTrigger(model.entranceForm, model.isEligibleForRecTrigger)) {
            AppLogAnalytics.send(EventName.REC_TRIGGER, model.toRecTriggerJson())
        }
        model.setGlobalParamOnClick()
    }

    fun sendCardShowAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.send(EventName.CARD_SHOW, model.toShowClickJson())
    }

    fun sendCardClickAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.send(EventName.CARD_CLICK, model.toShowClickJson())
        if (shouldSendRecTrigger(model.entranceForm)) {
            AppLogAnalytics.send(EventName.REC_TRIGGER, model.toRecTriggerJson())
        }
        model.setGlobalParamOnClick()
    }

    fun sendEnterPageAppLog() {
        AppLogAnalytics.send(
            EventName.ENTER_PAGE,
            JSONObject().apply {
                put(ENTER_FROM, AppLogAnalytics.getLastData(ENTER_FROM))
                put(ENTER_METHOD, AppLogAnalytics.getLastData(ENTER_METHOD))
                addPage()
            }
        )
    }

    private fun AppLogRecommendationProductModel.setGlobalParamOnClick() {
        AppLogAnalytics.setGlobalParamOnClick(
            entranceForm = entranceForm,
            sourceModule = sourceModule,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = SourcePageType.PRODUCT_CARD,
            requestId = requestId
        )
        additionalParam.setAdditionalToGlobalParam()
    }

    private fun AppLogRecommendationCardModel.setGlobalParamOnClick() {
        AppLogAnalytics.setGlobalParamOnClick(
            entranceForm = entranceForm,
            sourceModule = sourceModule,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = sourcePageType,
            requestId = requestId,
        )
    }

    private fun shouldSendRecTrigger(
        entranceForm: String,
        eligibleForRecTrigger: Boolean = false
    ): Boolean {
        return entranceForm == EntranceForm.PURE_GOODS_CARD.str ||
            entranceForm == EntranceForm.CONTENT_GOODS_CARD.str ||
            (entranceForm == EntranceForm.DETAIL_GOODS_CARD.str &&
                eligibleForRecTrigger)
    }

    private fun AppLogRecommendationProductModel.shouldSendCardEvent(): Boolean {
        return entranceForm == EntranceForm.MISSION_HORIZONTAL_GOODS_CARD.str ||
            entranceForm == EntranceForm.PURE_GOODS_CARD.str
    }
}

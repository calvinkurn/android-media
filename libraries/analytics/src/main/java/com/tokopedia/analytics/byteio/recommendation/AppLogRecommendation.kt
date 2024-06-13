package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogParam.CLICK_AREA
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_METHOD
import com.tokopedia.analytics.byteio.AppLogParam.ENTRANCE_FORM
import com.tokopedia.analytics.byteio.AppLogParam.IS_AD
import com.tokopedia.analytics.byteio.AppLogParam.REQUEST_ID
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_MODULE
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_PAGE_TYPE
import com.tokopedia.analytics.byteio.AppLogParam.TRACK_ID
import com.tokopedia.analytics.byteio.ClickAreaType
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.analytics.byteio.TrackConfirmCart
import com.tokopedia.analytics.byteio.TrackConfirmCartResult
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

    fun sendProductClickAppLog(
        model: AppLogRecommendationProductModel,
        areaType: ClickAreaType = ClickAreaType.UNDEFINED
    ) {
        val params = model.toShowClickJson()
            .also {
                it.appendDefineClickArea(areaType)
            }

        AppLogAnalytics.send(EventName.PRODUCT_CLICK, params)
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

    fun sendConfirmCartAppLog(model: AppLogRecommendationProductModel, product: TrackConfirmCart) {
        /**
         * This product click event is sent as requested to accommodate
         * value for entrance info. From an analytical point of view,
         * When pressing the ATC button on the Discovery page, the journey will be the same
         * like going to PDP and pressing the ATC button there.
         */
        sendProductClickAppLog(model, ClickAreaType.ATC)

        AppLogAnalytics.send(EventName.CONFIRM_CART, model.toConfirmCartJson(product))
    }

    fun sendConfirmCartResultAppLog(
        model: AppLogRecommendationProductModel,
        product: TrackConfirmCartResult
    ) {
        AppLogAnalytics.send(EventName.CONFIRM_CART_RESULT, model.toConfirmCartResultJson(product))
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

    fun AppLogRecommendationProductModel.asEntranceInfoMap(): Map<String, Any> =
        mapOf(
            ENTRANCE_FORM to entranceForm,
            SOURCE_MODULE to sourceModule,
            IS_AD to isAd,
            TRACK_ID to trackId,
            SOURCE_PAGE_TYPE to SourcePageType.PRODUCT_CARD,
            REQUEST_ID to requestId
        )


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

    private fun JSONObject.appendDefineClickArea(areaType: ClickAreaType): JSONObject {
        if (areaType == ClickAreaType.UNDEFINED) return this

        return put(CLICK_AREA, areaType.value)
    }
}

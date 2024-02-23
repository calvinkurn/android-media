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
        if (model.type == AppLogRecommendationType.MIXED_CAROUSEL ||
            model.type == AppLogRecommendationType.VERTICAL) {
            AppLogAnalytics.send(EventName.CARD_SHOW, model.asCardModel().toShowClickJson())
        }
    }

    fun sendProductClickAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, model.toShowClickJson())
        if (model.type == AppLogRecommendationType.MIXED_CAROUSEL ||
            model.type == AppLogRecommendationType.VERTICAL) {
            AppLogAnalytics.send(EventName.CARD_CLICK, model.asCardModel().toShowClickJson())
        }
        if (model.type == AppLogRecommendationType.VERTICAL) {
            AppLogAnalytics.send(EventName.REC_TRIGGER, model.toRecTriggerJson())
        }
        model.setGlobalParams()
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
        model.setGlobalParams()
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

    private fun AppLogRecommendationProductModel.setGlobalParams() {
        setGlobalParam(
            entranceForm = entranceForm,
            enterMethod = enterMethod,
            sourceModule = sourceModule,
            listName = listName,
            listNum = listNum,
            itemOrder = itemOrder,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = SourcePageType.PRODUCT_CARD,
            recParams = recParams,
            requestId = requestId,
        )
    }

    private fun AppLogRecommendationCardModel.setGlobalParams() {
        setGlobalParam(
            entranceForm = entranceForm,
            enterMethod = enterMethod,
            sourceModule = sourceModule,
            listName = listName,
            listNum = listNum,
            itemOrder = itemOrder,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = SourcePageType.PRODUCT_CARD,
            recParams = recParams,
            requestId = requestId,
            cardName = cardName,
        )
    }

    private fun setGlobalParam(
        entranceForm: String,
        enterMethod: String = "",
        sourceModule: String,
        listName: String,
        listNum: Int,
        itemOrder: Int,
        isAd: Int,
        trackId: String,
        cardName: CardName? = null,
        sourcePageType: SourcePageType,
        recParams: String,
        requestId: String,
    ) {
        AppLogAnalytics.putPageData(AppLogParam.ENTRANCE_FORM, entranceForm)
        AppLogAnalytics.putPageData(AppLogParam.ENTER_FROM, enterMethod)
        AppLogAnalytics.putPageData(AppLogParam.SOURCE_MODULE, sourceModule)
        AppLogAnalytics.putPageData(AppLogParam.LIST_NAME, listName)
        AppLogAnalytics.putPageData(AppLogParam.LIST_NUM, listNum)
        AppLogAnalytics.putPageData(AppLogParam.ITEM_ORDER, itemOrder)
        AppLogAnalytics.putPageData(AppLogParam.IS_AD, isAd)
        AppLogAnalytics.putPageData(AppLogParam.TRACK_ID, trackId)
        cardName?.let { AppLogAnalytics.putPageData(AppLogParam.CARD_NAME, it.str) }
        AppLogAnalytics.putPageData(AppLogParam.SOURCE_PAGE_TYPE, sourcePageType.str)
        AppLogAnalytics.putPageData(AppLogParam.REC_PARAMS, recParams)
        AppLogAnalytics.putPageData(AppLogParam.REQUEST_ID, requestId)
    }
}

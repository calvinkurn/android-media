package com.tokopedia.analytics.byteio.home

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_METHOD
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.SourcePageType

object AppLogHomeChannel {

    fun sendProductShow(model: HomeChannelProductModel) {
        AppLogAnalytics.send(EventName.PRODUCT_SHOW, model.toShowClickJson())
        if (model.shouldSendCardEvent()) {
            AppLogAnalytics.send(EventName.CARD_SHOW, model.asCardModel().toShowClickJson())
        }
    }

    fun sendProductClick(model: HomeChannelProductModel) {
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, model.toShowClickJson())
        if (model.shouldSendCardEvent()) {
            AppLogAnalytics.send(EventName.CARD_CLICK, model.toShowClickJson())
        }
        model.setGlobalParams()
    }

    fun sendCardShow(model: HomeChannelCardModel) {
        AppLogAnalytics.send(EventName.CARD_SHOW, model.toShowClickJson())
    }

    fun sendCardClick(model: HomeChannelCardModel) {
        AppLogAnalytics.send(EventName.CARD_CLICK, model.toShowClickJson())
        model.setGlobalParams()
    }

    fun missionEnterMethod(recomPageName: String, index: Int): String {
        return generateEnterMethod("2mission", recomPageName, index)
    }

    fun dealsEnterMethod(recomPageName: String, index: Int): String {
        return generateEnterMethod("2deals", recomPageName, index)
    }

    private fun generateEnterMethod(type: String, recomPageName: String, index: Int): String {
        return "${PageName.HOME}_${type}_${recomPageName}_${index + 1}"
    }

    private fun HomeChannelProductModel.setGlobalParams() {
        AppLogAnalytics.setGlobalParams(
            entranceForm = entranceForm,
            enterMethod = enterMethod ?: AppLogAnalytics.getLastDataExactStep(ENTER_METHOD)?.toString(),
            sourceModule = sourceModule,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = SourcePageType.PRODUCT_CARD,
            requestId = requestId
        )
    }

    private fun HomeChannelCardModel.setGlobalParams() {
        AppLogAnalytics.setGlobalParams(
            entranceForm = entranceForm,
            enterMethod = enterMethod ?: AppLogAnalytics.getLastDataExactStep(ENTER_METHOD)?.toString(),
            sourceModule = sourceModule,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = SourcePageType.PRODUCT_CARD,
            requestId = requestId
        )
    }

    private fun HomeChannelProductModel.shouldSendCardEvent(): Boolean {
        return entranceForm == EntranceForm.TWO_MISSION_HORIZONTAL_GOODS_CARD.str
    }
}

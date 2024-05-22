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
        AppLogAnalytics.send(EventName.CARD_SHOW, model.asCardModel().toShowClickJson())
    }

    fun sendProductClick(model: HomeChannelProductModel) {
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, model.toShowClickJson())
        AppLogAnalytics.send(EventName.CARD_CLICK, model.toShowClickJson())
        model.setGlobalParamOnClick()
    }

    fun sendCardShow(model: HomeChannelCardModel) {
        AppLogAnalytics.send(EventName.CARD_SHOW, model.toShowClickJson())
    }

    fun sendCardClick(model: HomeChannelCardModel) {
        AppLogAnalytics.send(EventName.CARD_CLICK, model.toShowClickJson())
        model.setGlobalParamOnClick()
    }

    fun getEnterMethod(type: String, recomPageName: String, index: Int): String {
        return "${PageName.HOME}_${type}_${recomPageName}_${index + 1}"
    }

    private fun HomeChannelProductModel.setGlobalParamOnClick() {
        AppLogAnalytics.setGlobalParamOnClick(
            entranceForm = entranceForm,
            sourceModule = sourceModule,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = SourcePageType.PRODUCT_CARD,
            requestId = requestId
        )
    }

    private fun HomeChannelCardModel.setGlobalParamOnClick() {
        AppLogAnalytics.setGlobalParamOnClick(
            entranceForm = entranceForm,
            sourceModule = sourceModule,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = SourcePageType.PRODUCT_CARD,
            requestId = requestId
        )
    }
}

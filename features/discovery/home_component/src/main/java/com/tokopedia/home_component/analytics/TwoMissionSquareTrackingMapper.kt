package com.tokopedia.home_component.analytics

import com.tokopedia.analytics.byteio.home.HomeChannelCardModel
import com.tokopedia.analytics.byteio.home.HomeChannelProductModel
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel

object TwoMissionSquareTrackingMapper {

    fun ItemMissionWidgetUiModel.asProductModel(
        enterMethod: String = ""
    ): HomeChannelProductModel {
        return HomeChannelProductModel(
            listName = "",
            listNum = "0",
            entranceForm = tracker.entranceForm,
            sourceModule = tracker.sourceModule(),
            enterMethod = enterMethod,
            productId = tracker.productId,
            isAd = if (tracker.isTopAds) 1 else 0,
            isUseCache = 0,
            trackId = tracker.trackId,
            recSessionId = tracker.recSessionId,
            recParams = tracker.recParams,
            requestId = tracker.requestId,
            shopId = shopId,
            itemOrder = tracker.itemOrder,
            cardName = tracker.cardName,
        )
    }

    fun ItemMissionWidgetUiModel.asCardModel(
        enterMethod: String = "",
    ): HomeChannelCardModel {
        return HomeChannelCardModel(
            cardName = tracker.cardName,
            sourceModule = tracker.sourceModule(),
            productId = tracker.productId,
            isAd = if (tracker.isTopAds) 1 else 0,
            isUseCache = 0,
            trackId = tracker.trackId,
            requestId = tracker.requestId,
            recSessionId = tracker.recSessionId,
            recParams = tracker.recParams,
            shopId = tracker.shopId,
            itemOrder = tracker.itemOrder,
            entranceForm = tracker.entranceForm,
            enterMethod = enterMethod,
        )
    }
}

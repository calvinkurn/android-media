package com.tokopedia.home_component.analytics

import com.tokopedia.analytics.byteio.home.HomeChannelCardModel
import com.tokopedia.analytics.byteio.home.HomeChannelProductModel
import com.tokopedia.home_component.visitable.shorten.ItemThumbnailWidgetUiModel

object TwoThumbnailSquareTrackingMapper {

    fun ItemThumbnailWidgetUiModel.asProductModel(
        enterMethod: String = "",
    ): HomeChannelProductModel {
        return HomeChannelProductModel(
            listName = tracker.listName,
            listNum = tracker.listNum,
            entranceForm = tracker.entranceForm,
            sourceModule = tracker.sourceModule(),
            enterMethod = enterMethod,
            productId = tracker.productId.ifEmpty { "0" },
            isAd = tracker.isAdsAsInt(),
            isUseCache = tracker.alwaysOnRemote(),
            trackId = tracker.trackId,
            recSessionId = tracker.recSessionId,
            recParams = tracker.recParams,
            requestId = tracker.requestId,
            shopId = tracker.getProperShopId(),
            itemOrder = tracker.itemOrder,
            cardName = tracker.cardName,
        )
    }

    fun ItemThumbnailWidgetUiModel.asCardModel(
        enterMethod: String = "",
    ): HomeChannelCardModel {
        return HomeChannelCardModel(
            listName = "",
            listNum = "",
            cardName = tracker.cardName,
            sourceModule = tracker.sourceModule(),
            productId = tracker.productId.ifEmpty { "0" },
            isAd = tracker.isAdsAsInt(),
            isUseCache = tracker.alwaysOnRemote(),
            trackId = tracker.trackId,
            requestId = tracker.requestId,
            recSessionId = tracker.recSessionId,
            recParams = tracker.recParams,
            shopId = tracker.getProperShopId(),
            itemOrder = tracker.itemOrder,
            entranceForm = tracker.entranceForm,
            enterMethod = enterMethod,
        )
    }
}

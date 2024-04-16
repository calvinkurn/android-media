package com.tokopedia.home_component.analytics

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationCardModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.byteio.recommendation.CardName
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel

object TrackRecommendationMapper {
    const val MISSION_MODULE_NAME = "mission"

    fun CarouselMissionWidgetDataModel.asProductTrackModel(
        isCache: Boolean = false,
        enterMethod: String = "",
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = data.productID,
            parentProductId = data.parentProductID,
            moduleName = data.pageName,
            isAd = data.isTopads,
            isUseCache = isCache,
            recParams = appLog.recParam,
            requestId = appLog.requestId,
            recSessionId = appLog.sessionId,
            shopId = data.shopId,
            entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD,
            enterMethod = enterMethod,
            position = cardPosition,
            cardName = CardName.MISSION_PRODUCT_CARD.format(data.title),
        )
    }

    fun CarouselMissionWidgetDataModel.asCardTrackModel(
        isCache: Boolean = false,
        enterMethod: String = "",
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardId = data.id.toString(),
            cardName = CardName.MISSION_PAGE_CARD.format(data.title),
            productId = data.productID,
            parentProductId = data.parentProductID,
            moduleName = data.pageName,
            isAd = data.isTopads,
            isUseCache = isCache,
            recParams = appLog.recParam,
            requestId = appLog.requestId,
            recSessionId = appLog.sessionId,
            shopId = data.shopId,
            entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD,
            enterMethod = enterMethod,
            position = cardPosition,
        )
    }

    fun ChannelGrid.asAdsLogRealtimeClickModel(refer: String): AdsLogRealtimeClickModel {
        return AdsLogRealtimeClickModel(refer,
            // todo this value from BE
            0,
            // todo this value from BE
            "", AdsLogRealtimeClickModel.AdExtraData(
            productId = id,
            productName = name,
        ))
    }

    fun ChannelGrid.asAdsLogShowOverModel(visiblePercentage: Int): AdsLogShowOverModel {
        return AdsLogShowOverModel(
            // todo this value from BE
            0,
            // todo this value from BE
            "",
            AdsLogShowOverModel.AdExtraData(
                productId = id,
                productName = name,
                sizePercent = visiblePercentage.toString())
        )
    }

    fun ChannelGrid.asAdsLogShowModel(): AdsLogShowModel {
        return AdsLogShowModel(
            // todo this value from BE
            0,
            // todo this value from BE
            "", AdsLogShowModel.AdExtraData(
            productId = id,
            productName = name,
        ))
    }
}

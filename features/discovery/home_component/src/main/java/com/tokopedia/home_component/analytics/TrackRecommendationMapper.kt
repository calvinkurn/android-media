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
import com.tokopedia.kotlin.extensions.view.toLongOrZero

object TrackRecommendationMapper {
    const val MISSION_MODULE_NAME = "mission"

    fun CarouselMissionWidgetDataModel.asProductTrackModel(
        isCache: Boolean = false,
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
            position = cardPosition,
            cardName = CardName.MISSION_PRODUCT_CARD.format(data.title),
        )
    }

    fun CarouselMissionWidgetDataModel.asCardTrackModel(
        isCache: Boolean = false,
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
            position = cardPosition,
        )
    }

    fun ChannelGrid.asAdsLogRealtimeClickModel(refer: String): AdsLogRealtimeClickModel {
        return AdsLogRealtimeClickModel(refer,
            creativeID.toLongOrZero(),
            logExtra,
            AdsLogRealtimeClickModel.AdExtraData(
            productId = absoluteProductId,
            productName = name,
        ))
    }

    fun ChannelGrid.asAdsLogShowOverModel(visiblePercentage: Int): AdsLogShowOverModel {
        return AdsLogShowOverModel(
            creativeID.toLongOrZero(),
            logExtra,
            AdsLogShowOverModel.AdExtraData(
                productId = absoluteProductId,
                productName = name,
                sizePercent = visiblePercentage.toString())
        )
    }

    fun ChannelGrid.asAdsLogShowModel(): AdsLogShowModel {
        return AdsLogShowModel(
            creativeID.toLongOrZero(),
            logExtra,
            AdsLogShowModel.AdExtraData(
            productId = absoluteProductId,
            productName = name,
        ))
    }
}

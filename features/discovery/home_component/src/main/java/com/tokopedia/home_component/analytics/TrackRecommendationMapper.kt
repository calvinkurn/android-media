package com.tokopedia.home_component.analytics

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationCardModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationType
import com.tokopedia.analytics.byteio.recommendation.CardName
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel

object TrackRecommendationMapper {
    const val MISSION_MODULE_NAME = "mission"

    fun CarouselMissionWidgetDataModel.asProductTrackModel(
        isCache: Boolean = false
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = data.productID,
            moduleName = data.pageName,
            isAd = data.isTopads,
            isUseCache = isCache,
            recParams = "", // TODO need to confirm
            requestId = "", // TODO need BE deployment
            shopId = data.shopId,
            type = AppLogRecommendationType.MIXED_CAROUSEL,
            entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD,
        )
    }

    fun CarouselMissionWidgetDataModel.asCardTrackModel(
        isCache: Boolean = false
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardName = CardName.MISSION_CARD,
            cardType = data.pageName,
            productId = data.productID,
            moduleName = data.pageName,
            isAd = data.isTopads,
            isUseCache = isCache,
            recParams = "", // TODO need to confirm
            requestId = "", // TODO need BE deployment
            shopId = data.shopId,
            type = AppLogRecommendationType.PRODUCT_CAROUSEL,
            entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD
        )
    }
}

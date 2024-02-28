package com.tokopedia.home_component.analytics

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationCardModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.byteio.recommendation.CardName
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel

object TrackRecommendationMapper {
    const val MISSION_MODULE_NAME = "mission"

    fun CarouselMissionWidgetDataModel.asProductTrackModel(
        isCache: Boolean = false,
        enterMethod: String = "",
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = data.productID,
            moduleName = data.pageName,
            isAd = data.isTopads,
            isUseCache = isCache,
            recParams = "", // TODO need to confirm
            requestId = "", // TODO need BE deployment
            recSessionId = "", // TODO need BE deployment
            shopId = data.shopId,
            entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD,
            enterMethod = enterMethod,
            position = cardPosition,
        )
    }

    fun CarouselMissionWidgetDataModel.asCardTrackModel(
        isCache: Boolean = false,
        enterMethod: String = "",
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardName = CardName.MISSION_CARD,
            cardType = data.pageName,
            productId = data.productID,
            moduleName = data.pageName,
            isAd = data.isTopads,
            isUseCache = isCache,
            recParams = "", //TODO need to confirm
            requestId = "", //TODO need BE deployment
            recSessionId = "", //TODO need BE deployment
            shopId = data.shopId,
            entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD,
            enterMethod = enterMethod,
            position = cardPosition,
        )
    }
}

package com.tokopedia.home_component.analytics

import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationCardModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel

object TrackRecommendationMapper {
    const val MISSION_MODULE_NAME = "mission"

    fun CarouselMissionWidgetDataModel.asProductTrackerModel(
        isCache: Boolean = false,
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = data.productID,
            sourceModule = "", //TODO need to confirm
            isAd = data.isTopads,
            isUseCache = isCache,
            recParams = "", //TODO need to confirm
            requestId = "", //TODO need BE deployment
            shopId = data.shopId,
        )
    }

    fun CarouselMissionWidgetDataModel.asCardTrackerModel(
        isCache: Boolean = false,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            productId = data.productID,
            sourceModule = "", //TODO need to confirm
            isAd = data.isTopads,
            isUseCache = isCache,
            recParams = "", //TODO need to confirm
            requestId = "", //TODO need BE deployment
            shopId = data.shopId,
        )
    }
}

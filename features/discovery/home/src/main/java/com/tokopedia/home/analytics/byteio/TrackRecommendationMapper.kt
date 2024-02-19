package com.tokopedia.home.analytics.byteio

import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel

object TrackRecommendationMapper {
    fun RecommendationCardModel.asShowClickTracker(
        isCache: Boolean = false,
        tabName: String,
        tabPosition: Int,
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = recommendationProductItem.id,
            tabName = tabName,
            tabPosition = tabPosition,
            sourceModule = "", //TODO need to confirm
            trackId = "{request_id}_${recommendationProductItem.id}_${position + 1}", //TODO need BE deployment
            isAd = recommendationProductItem.isTopAds,
            isUseCache = isCache,
            recParams = "", //TODO need to confirm
            requestId = "", //TODO need BE deployment
            shopId = recommendationProductItem.shop.id,
        )
    }
}

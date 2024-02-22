package com.tokopedia.home.analytics.byteio

import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationCardModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationType
import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerTopAdsModel

object TrackRecommendationMapper {
    fun RecommendationCardModel.asProductTrackModel(
        isCache: Boolean = false,
        tabName: String,
        tabPosition: Int,
        type: AppLogRecommendationType,
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = recommendationProductItem.id,
            tabName = tabName,
            tabPosition = tabPosition,
            moduleName = "", //TODO need to confirm
            isAd = recommendationProductItem.isTopAds,
            isUseCache = isCache,
            recParams = "", //TODO need to confirm
            requestId = "", //TODO need BE deployment
            shopId = recommendationProductItem.shop.id,
            type = type,
        )
    }

    fun BannerTopAdsModel.asCardTrackModel(
        isCache: Boolean = false,
        tabName: String,
        tabPosition: Int,
        type: AppLogRecommendationType,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardName = topAdsImageViewModel?.bannerName.orEmpty(),
            tabName = tabName,
            tabPosition = tabPosition,
            moduleName = "", //TODO need to confirm
            isAd = !topAdsImageViewModel?.adViewUrl.isNullOrEmpty() && !topAdsImageViewModel?.adClickUrl.isNullOrEmpty(),
            isUseCache = isCache,
            recParams = "", //TODO need to confirm
            requestId = "", //TODO need BE deployment
            shopId = topAdsImageViewModel?.shopId.orEmpty(),
            type = type,
        )
    }

    fun ContentCardModel.asCardTrackModel(
        isCache: Boolean = false,
        tabName: String,
        tabPosition: Int,
        type: AppLogRecommendationType,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardName = title,
            tabName = tabName,
            tabPosition = tabPosition,
            moduleName = "", //TODO need to confirm
            isAd = isAds,
            isUseCache = isCache,
            recParams = "", //TODO need to confirm
            requestId = "", //TODO need BE deployment
            shopId = shopId,
            type = type,
        )
    }

    fun PlayCardModel.asCardTrackModel(
        isCache: Boolean = false,
        tabName: String,
        tabPosition: Int,
        type: AppLogRecommendationType,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardName = playVideoWidgetUiModel.title,
            tabName = tabName,
            tabPosition = tabPosition,
            isAd = isAds,
            isUseCache = isCache,
            recParams = "", //TODO need to confirm
            requestId = "", //TODO need BE deployment
            shopId = shopId,
            groupId = playVideoWidgetUiModel.id,
            type = type,
        )
    }
}

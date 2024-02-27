package com.tokopedia.home.analytics.byteio

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationCardModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.byteio.recommendation.CardName
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.HomeRecommendationUtil.isFullSpan
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerTopAdsModel

object TrackRecommendationMapper {
    fun RecommendationCardModel.asProductTrackModel(
        isCache: Boolean = false,
        tabName: String,
        tabPosition: Int,
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = recommendationProductItem.id,
            tabName = tabName,
            tabPosition = tabPosition,
            moduleName = pageName,
            isAd = productCardModel.isTopAds,
            isUseCache = isCache,
            recParams = appLog.recParam,
            requestId = appLog.requestId,
            recSessionId = appLog.sessionId,
            shopId = recommendationProductItem.shop.id,
            entranceForm = getEntranceForm(),
            originalPrice = (
                if(recommendationProductItem.slashedPriceInt > 0)
                    recommendationProductItem.slashedPriceInt
                else recommendationProductItem.priceInt
            ).toFloat(),
            salesPrice = recommendationProductItem.priceInt.toFloat(),
            position = position,
            volume = productCardModel.countSoldRating.toIntOrZero(),
        )
    }

    private fun RecommendationCardModel.getEntranceForm(): EntranceForm {
        return if(isFullSpan()) EntranceForm.DETAIL_GOODS_CARD
        else EntranceForm.PURE_GOODS_CARD
    }

    fun BannerTopAdsModel.asCardTrackModel(
        isCache: Boolean = false,
        tabName: String,
        tabPosition: Int,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardName = CardName.AD_FEED_CARD,
            tabName = tabName,
            tabPosition = tabPosition,
            moduleName = pageName,
            isAd = !topAdsImageViewModel?.adViewUrl.isNullOrEmpty() && !topAdsImageViewModel?.adClickUrl.isNullOrEmpty(),
            isUseCache = isCache,
            recParams = "", // TODO need to confirm
            requestId = "", // TODO need BE deployment
            shopId = topAdsImageViewModel?.shopId.orEmpty(),
            entranceForm = EntranceForm.CONTENT_GOODS_CARD,
            position = position,
        )
    }

    fun ContentCardModel.asCardTrackModel(
        isCache: Boolean = false,
        tabName: String,
        tabPosition: Int,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardName = CardName.REC_CONTENT_CARD,
            cardType = layoutItem,
            tabName = tabName,
            tabPosition = tabPosition,
            moduleName = pageName,
            isAd = isAds,
            isUseCache = isCache,
            recParams = "", // TODO need to confirm
            requestId = "", // TODO need BE deployment
            shopId = shopId,
            entranceForm = EntranceForm.CONTENT_GOODS_CARD,
            position = position,
        )
    }

    fun PlayCardModel.asCardTrackModel(
        isCache: Boolean = false,
        tabName: String,
        tabPosition: Int,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardName = CardName.REC_VIDEO_CARD,
            tabName = tabName,
            tabPosition = tabPosition,
            moduleName = pageName,
            isAd = isAds,
            isUseCache = isCache,
            recParams = "", // TODO need to confirm
            requestId = "", // TODO need BE deployment
            shopId = shopId,
            groupId = playVideoWidgetUiModel.id,
            entranceForm = EntranceForm.CONTENT_GOODS_CARD,
            sourcePageType = SourcePageType.VIDEO,
            position = position,
        )
    }
}

package com.tokopedia.recommendation_widget_common.byteio

import com.tokopedia.analytics.byteio.EnterMethod
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationCardModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.byteio.recommendation.CardName
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.recommendation_widget_common.infinite.foryou.content.ContentCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.HomeRecommendationUtil.isFullSpan
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.BannerTopAdsModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

object TrackRecommendationMapper {

    fun RecommendationItem.asAdsLogRealtimeClickModel(refer: String): AdsLogRealtimeClickModel {
        return AdsLogRealtimeClickModel(refer,
            recommendationAdsLog.creativeID.toLongOrZero(),
            recommendationAdsLog.logExtra,
            AdsLogRealtimeClickModel.AdExtraData(
                productId = productId.orZero().toString(),
                productName = name,
            ))
    }

    fun RecommendationItem.asAdsLogShowOverModel(visiblePercentage: Int): AdsLogShowOverModel {
        return AdsLogShowOverModel(
            recommendationAdsLog.creativeID.toLongOrZero(),
            recommendationAdsLog.logExtra,
            AdsLogShowOverModel.AdExtraData(productId = productId.orZero().toString(), productName = name, sizePercent = visiblePercentage.toString()))
    }

    fun RecommendationItem.asAdsLogShowModel(): AdsLogShowModel {
        return AdsLogShowModel(
            recommendationAdsLog.creativeID.toLongOrZero(),
            recommendationAdsLog.logExtra,
            AdsLogShowModel.AdExtraData(
            productId = productId.orZero().toString(),
            productName = name,
        ))
    }

    fun RecommendationCardModel.ProductItem.asAdsLogRealtimeClickModel(refer: String): AdsLogRealtimeClickModel {
        return AdsLogRealtimeClickModel(refer,
            recommendationAdsLog.creativeID.toLongOrZero(),
            recommendationAdsLog.logExtra,
            AdsLogRealtimeClickModel.AdExtraData(
            productId = id,
            productName = name,
        ))
    }

    fun RecommendationCardModel.ProductItem.asAdsLogShowOverModel(visiblePercentage: Int): AdsLogShowOverModel {
        return AdsLogShowOverModel(
            recommendationAdsLog.creativeID.toLongOrZero(),
            recommendationAdsLog.logExtra,
            AdsLogShowOverModel.AdExtraData(
            productId = id,
            sizePercent = visiblePercentage.toString(),
            productName = name,
        ))
    }

    fun RecommendationCardModel.ProductItem.asAdsLogShowModel(): AdsLogShowModel {
        return AdsLogShowModel(
            recommendationAdsLog.creativeID.toLongOrZero(),
            recommendationAdsLog.logExtra,
            AdsLogShowModel.AdExtraData(productId = id, productName = name)
        )
    }

    fun RecommendationItem.asProductTrackModel(
        isCache: Boolean = false,
        entranceForm: EntranceForm,
        tabName: String = "",
        tabPosition: Int = -1,
        additionalParam: AppLogAdditionalParam? = AppLogAdditionalParam.None,
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = productId.toString(),
            parentProductId = parentID.toString(),
            position = position,
            moduleName = pageName,
            isAd = isTopAds,
            isUseCache = isCache,
            recParams = appLog.recParam,
            requestId = appLog.requestId,
            recSessionId = appLog.sessionId,
            shopId = shopId.toString(),
            entranceForm = entranceForm,
            tabName = tabName,
            tabPosition = tabPosition,
            rate = ratingAverage.toFloatOrZero(),
            volume = countSold,
            originalPrice = (if (slashedPriceInt > 0) slashedPriceInt else priceInt).toFloat(),
            salesPrice = priceInt.toFloat(),
            additionalParam = additionalParam ?: AppLogAdditionalParam.None,
        )
    }

    fun RecommendationCardModel.asProductTrackModel(
        isCache: Boolean = false,
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = recommendationProductItem.id,
            parentProductId = recommendationProductItem.parentProductId,
            tabName = tabName,
            tabPosition = tabIndex,
            moduleName = pageName,
            isAd = productCardModel.isTopAds,
            isUseCache = isCache,
            recParams = appLog.recParam,
            requestId = appLog.requestId,
            recSessionId = appLog.sessionId,
            shopId = recommendationProductItem.shop.id,
            entranceForm = getEntranceForm(),
            originalPrice = (if (recommendationProductItem.slashedPriceInt > 0) {
                recommendationProductItem.slashedPriceInt
            } else {
                recommendationProductItem.priceInt
            }).toFloat(),
            salesPrice = recommendationProductItem.priceInt.toFloat(),
            position = position,
            volume = recommendationProductItem.countSold,
            rate = productCardModel.countSoldRating.toFloatOrZero(),
        )
    }

    private fun RecommendationCardModel.getEntranceForm(): EntranceForm {
        return if (isFullSpan()) {
            EntranceForm.DETAIL_GOODS_CARD
        } else {
            EntranceForm.PURE_GOODS_CARD
        }
    }

    fun BannerTopAdsModel.asCardTrackModel(
        isCache: Boolean = false,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardId = cardId,
            cardName = CardName.AD_FEED_CARD,
            tabName = tabName,
            tabPosition = tabIndex,
            moduleName = pageName,
            isAd = !topAdsImageUiModel?.adViewUrl.isNullOrEmpty() && !topAdsImageUiModel?.adClickUrl.isNullOrEmpty(),
            isUseCache = isCache,
            recParams = appLog.recParam,
            requestId = appLog.requestId,
            recSessionId = appLog.sessionId,
            shopId = topAdsImageUiModel?.shopId.orEmpty(),
            entranceForm = EntranceForm.CONTENT_GOODS_CARD,
            position = position,
        )
    }

    fun ContentCardModel.asCardTrackModel(
        isCache: Boolean = false,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardId = id,
            cardName = CardName.REC_CONTENT_CARD.format(layoutItem),
            tabName = tabName,
            tabPosition = tabIndex,
            moduleName = pageName,
            isAd = isAds,
            isUseCache = isCache,
            recParams = appLog.recParam,
            requestId = appLog.requestId,
            recSessionId = appLog.sessionId,
            shopId = shopId,
            entranceForm = EntranceForm.CONTENT_GOODS_CARD,
            position = position,
        )
    }

    fun PlayCardModel.asCardTrackModel(
        isCache: Boolean = false,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardId = cardId,
            cardName = CardName.REC_VIDEO_CARD,
            tabName = tabName,
            tabPosition = tabIndex,
            moduleName = pageName,
            isAd = isAds,
            isUseCache = isCache,
            recParams = appLog.recParam,
            requestId = appLog.requestId,
            recSessionId = appLog.sessionId,
            shopId = shopId,
            groupId = playVideoWidgetUiModel.id,
            entranceForm = EntranceForm.CONTENT_GOODS_CARD,
            sourcePageType = SourcePageType.VIDEO,
            position = position,
        )
    }
}

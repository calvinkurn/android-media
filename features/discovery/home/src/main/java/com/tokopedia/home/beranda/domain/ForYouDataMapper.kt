package com.tokopedia.home.beranda.domain

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsOldDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecomEntityCardUiModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerTopAdsModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.utils.RecomTemporary
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAdsLog

@RecomTemporary
object ForYouDataMapper {

    fun BannerRecommendationDataModel.toModel(): BannerRecommendationModel {
        return BannerRecommendationModel(
            id = id,
            name = name,
            imageUrl = imageUrl,
            url = url,
            applink = applink,
            buAttribution = buAttribution,
            creativeName = creativeName,
            target = target,
            position = position,
            galaxyAttribution = galaxyAttribution,
            affinityLabel = affinityLabel,
            shopId = shopId,
            categoryPersona = categoryPersona,
            tabName = tabName
        )
    }

    fun HomeRecommendationBannerTopAdsOldDataModel.toModel(): BannerOldTopAdsModel {
        return BannerOldTopAdsModel(
            topAdsImageUiModel = topAdsImageUiModel,
            position = position,
            bannerType = bannerType
        )
    }

    fun HomeRecommendationBannerTopAdsUiModel.toModel(): BannerTopAdsModel {
        return BannerTopAdsModel(
            topAdsImageUiModel = topAdsImageUiModel,
            cardId = cardId,
            layoutCard = layoutCard,
            layoutItem = layoutItem,
            categoryId = categoryId,
            position = position,
            pageName = pageName
        )
    }

    fun HomeRecommendationItemDataModel.toModel(): RecommendationCardModel {
        return RecommendationCardModel(
            productCardModel = productCardModel,
            recommendationProductItem = RecommendationCardModel.ProductItem(
                id = recommendationProductItem.id,
                name = recommendationProductItem.name,
                imageUrl = recommendationProductItem.imageUrl,
                recommendationType = recommendationProductItem.recommendationType,
                priceInt = recommendationProductItem.priceInt,
                slashedPriceInt = recommendationProductItem.slashedPriceInt,
                freeOngkirIsActive = recommendationProductItem.freeOngkirIsActive,
                labelGroup = recommendationProductItem.labelGroup.map {
                    RecommendationCardModel.ProductItem.LabelGroup(
                        position = it.position,
                        title = it.title,
                        type = it.type,
                        url = it.url
                    )
                },
                categoryBreadcrumbs = recommendationProductItem.categoryBreadcrumbs,
                clusterID = recommendationProductItem.clusterID,
                isTopAds = recommendationProductItem.isTopAds,
                trackerImageUrl = recommendationProductItem.trackerImageUrl,
                clickUrl = recommendationProductItem.clickUrl,
                isWishlist = recommendationProductItem.isWishlist,
                wishListUrl = recommendationProductItem.wishListUrl,
                shop = recommendationProductItem.shop.let {
                    RecommendationCardModel.ProductItem.Shop(
                        id = it.id,
                        applink = it.applink,
                        city = it.city,
                        domain = it.domain,
                        imageUrl = it.imageUrl,
                        name = it.name,
                        reputation = it.reputation,
                        url = it.url
                    )
                },
                recommendationAdsLog = RecommendationAdsLog(
                    creativeID = recommendationProductItem.recommendationAdsLog.creativeID,
                    logExtra = recommendationProductItem.recommendationAdsLog.logExtra
                )
            ),
            pageName = pageName,
            layoutName = layoutName,
            position = position,
            tabName = tabName,
            appLog = appLog,
        )
    }

    fun HomeRecommendationPlayWidgetUiModel.toModel(): PlayCardModel {
        return PlayCardModel(
            cardId = cardId,
            appLink = appLink,
            playVideoWidgetUiModel = playVideoWidgetUiModel,
            playVideoTrackerUiModel = PlayCardModel.PlayVideoTrackerUiModel(
                videoType = playVideoTrackerUiModel.videoType,
                partnerId = playVideoTrackerUiModel.partnerId,
                recommendationType = playVideoTrackerUiModel.recommendationType,
                playChannelId = playVideoTrackerUiModel.playChannelId,
                layoutCard = playVideoTrackerUiModel.layoutCard,
                layoutItem = playVideoTrackerUiModel.layoutItem,
                categoryId = playVideoTrackerUiModel.categoryId
            ),
            isAds = isAds,
            shopId = shopId,
            pageName = pageName,
            position = position
        )
    }

    fun RecomEntityCardUiModel.toModel(): ContentCardModel {
        return ContentCardModel(
            id = id,
            layoutCard = layoutCard,
            layoutItem = layoutItem,
            categoryId = categoryId,
            title = title,
            subTitle = subTitle,
            appLink = appLink,
            imageUrl = imageUrl,
            backgroundColor = backgroundColor,
            labelState = ContentCardModel.LabelState(
                iconUrl = labelState.iconUrl,
                title = labelState.title,
                textColor = labelState.textColor
            ),
            position = position,
            isAds = isAds,
            shopId = shopId,
            pageName = pageName,
        )
    }
}

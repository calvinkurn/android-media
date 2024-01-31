package com.tokopedia.home.beranda.domain

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsOldDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecomEntityCardUiModel
import com.tokopedia.recommendation_widget_common.widget.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.entity.RecomEntityModel
import com.tokopedia.recommendation_widget_common.widget.foryou.play.PlayWidgetModel
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.BannerTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.utils.RecomTemporary

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
            tabName = tabName,
        )
    }

    fun HomeRecommendationBannerTopAdsOldDataModel.toModel(): BannerOldTopAdsModel {
        return BannerOldTopAdsModel(
            topAdsImageViewModel = topAdsImageViewModel,
            position = position,
            bannerType = bannerType,
        )
    }

    fun HomeRecommendationBannerTopAdsUiModel.toModel(): BannerTopAdsModel {
        return BannerTopAdsModel(
            topAdsImageViewModel = topAdsImageViewModel,
            cardId = cardId,
            layoutCard = layoutCard,
            layoutItem = layoutItem,
            categoryId = categoryId,
            position = position
        )
    }

    fun HomeRecommendationItemDataModel.toModel(): HomeRecommendationModel {
        return HomeRecommendationModel(
            productCardModel = productCardModel,
            recommendationProductItem = HomeRecommendationModel.ProductItem(
                id = recommendationProductItem.id,
                name = recommendationProductItem.name,
                imageUrl = recommendationProductItem.imageUrl,
                recommendationType = recommendationProductItem.recommendationType,
                priceInt = recommendationProductItem.priceInt,
                freeOngkirIsActive = recommendationProductItem.freeOngkirIsActive,
                labelGroup = recommendationProductItem.labelGroup.map {
                    HomeRecommendationModel.ProductItem.LabelGroup(
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
            ),
            pageName = pageName,
            layoutName = layoutName,
            position = position,
            tabName = tabName,
        )
    }

    fun HomeRecommendationPlayWidgetUiModel.toModel(): PlayWidgetModel {
        return PlayWidgetModel(
            cardId = cardId,
            appLink = appLink,
            playVideoWidgetUiModel = playVideoWidgetUiModel,
            playVideoTrackerUiModel = PlayWidgetModel.PlayVideoTrackerUiModel(
                videoType = playVideoTrackerUiModel.videoType,
                partnerId = playVideoTrackerUiModel.partnerId,
                recommendationType = playVideoTrackerUiModel.recommendationType,
                playChannelId = playVideoTrackerUiModel.playChannelId,
                layoutCard = playVideoTrackerUiModel.layoutCard,
                layoutItem = playVideoTrackerUiModel.layoutItem,
                categoryId = playVideoTrackerUiModel.categoryId,
            ),
        )
    }

    fun RecomEntityCardUiModel.toModel(): RecomEntityModel {
        return RecomEntityModel(
            id = id,
            layoutCard = layoutCard,
            layoutItem = layoutItem,
            categoryId = categoryId,
            title = title,
            subTitle = subTitle,
            appLink = appLink,
            imageUrl = imageUrl,
            backgroundColor = backgroundColor,
            labelState = RecomEntityModel.LabelState(
                iconUrl = labelState.iconUrl,
                title = labelState.title,
                textColor = labelState.textColor
            ),
        )
    }
}

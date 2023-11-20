package com.tokopedia.home.beranda.data.mapper

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.gql.recommendationcard.AdsBannerItemResponse
import com.tokopedia.home.beranda.domain.gql.recommendationcard.GetHomeRecommendationCardResponse
import com.tokopedia.home.beranda.domain.gql.recommendationcard.PlayVideoWidgetResponse
import com.tokopedia.home.beranda.domain.gql.recommendationcard.RecommendationCard
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.widget.entitycard.model.RecomEntityCardUiModel
import com.tokopedia.topads.sdk.domain.model.ImageShop
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import dagger.Lazy
import javax.inject.Inject

class HomeRecommendationCardMapper @Inject constructor(
    private val gson: Lazy<Gson>
) {

    fun mapToRecommendationCardDataModel(
        getHomeRecommendationCard: GetHomeRecommendationCardResponse.GetHomeRecommendationCard,
        tabName: String,
        pageNumber: Int
    ): HomeRecommendationDataModel {
        val homeRecommendationTypeFactoryImplList =
            mutableListOf<Visitable<HomeRecommendationTypeFactoryImpl>>()

        getHomeRecommendationCard.recommendationCards.forEachIndexed { index, card ->
            when (card.layout) {
                TYPE_PRODUCT -> {
                    homeRecommendationTypeFactoryImplList.add(
                        mapToHomeProductFeedModel(
                            card,
                            getHomeRecommendationCard.pageName,
                            getHomeRecommendationCard.layoutName,
                            pageNumber,
                            index,
                            getHomeRecommendationCard.recommendationCards.size,
                            tabName
                        )
                    )
                }

                TYPE_RECOM_CARD -> {
                    (mapToEntityCardRecommendationCard(card) as? Visitable<HomeRecommendationTypeFactoryImpl>)?.let {
                        homeRecommendationTypeFactoryImplList.add(
                            it
                        )
                    }
                }

                TYPE_BANNER_ADS -> {
                    if (getHomeRecommendationCard.layoutName != HomeRecommendationUtil.LAYOUT_NAME_LIST) {
                        val adsBannerItemResponse =
                            convertDataJsonToAdsBannerItem(card.dataStringJson)

                        adsBannerItemResponse?.let { bannerItemResponse ->
                            homeRecommendationTypeFactoryImplList.add(
                                HomeRecommendationBannerTopAdsUiModel(
                                    layoutCard = card.layout,
                                    layoutItem = card.layoutTracker,
                                    categoryId = card.categoryID,
                                    position = index,
                                    topAdsImageViewModel = mapToTopAdsImageViewModel(
                                        bannerItemResponse
                                    )
                                )
                            )
                        }
                    }
                }

                TYPE_VIDEO -> {
                    val recommendationPlayWidgetResponse =
                        convertDataJsonToRecommendationPlayWidget(
                            card.dataStringJson
                        )
                    recommendationPlayWidgetResponse?.let {
                        homeRecommendationTypeFactoryImplList.add(
                            mapToHomeRecommendationPlayWidget(
                                layoutCard = card.layout,
                                layoutTracker = card.layoutTracker,
                                playVideoWidgetResponse = it,
                                recommendationType = card.recommendationType
                            )
                        )
                    }
                }
            }
        }

        return HomeRecommendationDataModel(
            homeRecommendationTypeFactoryImplList.toList(),
            getHomeRecommendationCard.hasNextPage
        )
    }

    private fun mapToHomeRecommendationPlayWidget(
        layoutCard: String,
        layoutTracker: String,
        recommendationType: String,
        playVideoWidgetResponse: PlayVideoWidgetResponse
    ): HomeRecommendationPlayWidgetUiModel {
        return HomeRecommendationPlayWidgetUiModel(
            appLink = playVideoWidgetResponse.link.applink,
            playVideoWidgetUiModel = PlayVideoWidgetUiModel(
                id = playVideoWidgetResponse.id,
                totalView = playVideoWidgetResponse.stats.viewFmt,
                title = playVideoWidgetResponse.basic.title,
                avatarUrl = playVideoWidgetResponse.author.thumbnailURL,
                partnerName = playVideoWidgetResponse.author.name,
                coverUrl = playVideoWidgetResponse.basic.coverURL,
                videoUrl = playVideoWidgetResponse.medias.firstOrNull()?.mediaURL.orEmpty(),
                badgeUrl = playVideoWidgetResponse.author.badge,
                isLive = playVideoWidgetResponse.basic.isLive,
                isAutoPlay = playVideoWidgetResponse.basic.autoPlay
            ),
            playVideoTrackerUiModel = HomeRecommendationPlayWidgetUiModel.HomeRecommendationPlayVideoTrackerUiModel(
                videoType = playVideoWidgetResponse.basic.type.text,
                partnerId = playVideoWidgetResponse.author.id,
                playChannelId = playVideoWidgetResponse.contentOriginID,
                recommendationType = recommendationType,
                layoutCard = layoutCard,
                layoutItem = layoutTracker,
                categoryId = (playVideoWidgetResponse.category.dominantL3.firstOrNull() ?: "").toString()
            )
        )
    }

    private fun mapToEntityCardRecommendationCard(
        recommendationCard: RecommendationCard
    ): RecomEntityCardUiModel {
        return RecomEntityCardUiModel(
            id = recommendationCard.id,
            layoutCard = recommendationCard.layout,
            layoutItem = recommendationCard.layoutTracker,
            categoryId = recommendationCard.categoryID,
            title = recommendationCard.name,
            appLink = recommendationCard.applink,
            subTitle = recommendationCard.subtitle,
            imageUrl = recommendationCard.imageUrl,
            backgroundColor = recommendationCard.gradientColor,
            labelState = RecomEntityCardUiModel.LabelState(
                iconUrl = recommendationCard.label.imageUrl,
                title = recommendationCard.label.title,
                textColor = recommendationCard.label.textColor
            )
        )
    }

    private fun convertDataJsonToAdsBannerItem(dataStringJson: String): AdsBannerItemResponse? {
        return convertDataJsonToModel<AdsBannerItemResponse>(dataStringJson)
    }

    private fun convertDataJsonToRecommendationPlayWidget(dataStringJson: String): PlayVideoWidgetResponse? {
        return convertDataJsonToModel<PlayVideoWidgetResponse>(dataStringJson)
    }

    private inline fun <reified T> convertDataJsonToModel(dataStringJson: String): T? {
        return try {
            gson.get().fromJson(dataStringJson, T::class.java)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    private fun mapToTopAdsImageViewModel(adsBannerItemResponse: AdsBannerItemResponse): TopAdsImageViewModel {
        val adsBanner = adsBannerItemResponse.item.adsBanner
        val imageShop = adsBanner.banner.shop.image
        val imageBanner = adsBanner.banner.bannerImages.firstOrNull()

        return TopAdsImageViewModel(
            bannerId = adsBanner.id,
            bannerName = adsBanner.banner.name,
            position = adsBanner.banner.position.toIntSafely(),
            ImpressHolder = ImageShop(
                imageShop.cover,
                imageShop.sURL,
                imageShop.xsURL,
                imageShop.coverEcs,
                imageShop.sEcs,
                imageShop.xsEcs
            ),
            layoutType = adsBanner.banner.layoutType,
            adClickUrl = adsBanner.adClickURL,
            adViewUrl = adsBanner.adViewURL,
            applink = adsBanner.appLink,
            imageUrl = imageBanner?.url.orEmpty(),
            imageWidth = imageBanner?.dimension?.width.orZero(),
            imageHeight = imageBanner?.dimension?.height.orZero(),
            shopId = adsBanner.banner.shop.id
        )
    }

    private fun mapToHomeProductFeedModel(
        recommendationCard: RecommendationCard,
        pageName: String,
        layoutName: String,
        pageNumber: Int,
        index: Int,
        cardTotal: Int,
        tabName: String
    ): HomeRecommendationItemDataModel {
        val productCard = mapToProductCardModel(recommendationCard)

        return HomeRecommendationItemDataModel(
            productCard,
            recommendationCard.mapToHomeRecommendationProductItem(),
            pageName,
            layoutName,
            (((pageNumber - Int.ONE) * cardTotal) + index + Int.ONE),
            tabName
        )
    }

    private fun mapToLabelGroupList(recommendationCard: RecommendationCard): List<ProductCardModel.LabelGroup> {
        return recommendationCard.labelGroup.map {
            ProductCardModel.LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.url
            )
        }
    }

    private fun mapToProductCardModel(recommendationCard: RecommendationCard): ProductCardModel {
        val labelGroups = mapToLabelGroupList(recommendationCard)

        return ProductCardModel(
            slashedPrice = recommendationCard.slashedPrice,
            productName = recommendationCard.name,
            formattedPrice = recommendationCard.price,
            productImageUrl = recommendationCard.imageUrl,
            isTopAds = recommendationCard.isTopads,
            discountPercentage = if (recommendationCard.discountPercentage > 0) "${recommendationCard.discountPercentage}%" else "",
            ratingCount = recommendationCard.rating,
            reviewCount = recommendationCard.countReview,
            countSoldRating = recommendationCard.ratingAverage,
            shopLocation = recommendationCard.shop.city,
            isWishlistVisible = true,
            isWishlisted = recommendationCard.isWishlist,
            shopBadgeList = recommendationCard.badges.map {
                ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                isActive = recommendationCard.freeOngkir.isActive,
                imageUrl = recommendationCard.freeOngkir.imageUrl
            ),
            labelGroupList = labelGroups,
            hasThreeDots = true,
            cardInteraction = true
        )
    }

    companion object {
        private const val TYPE_PRODUCT = "product"
        const val TYPE_BANNER = "banner"
        private const val TYPE_BANNER_ADS = "banner_ads"
        private const val TYPE_RECOM_CARD = "recom_card"
        private const val TYPE_VIDEO = "video"

        const val TYPE_VERTICAL_BANNER_ADS = "banner_ads_vertical"
    }
}

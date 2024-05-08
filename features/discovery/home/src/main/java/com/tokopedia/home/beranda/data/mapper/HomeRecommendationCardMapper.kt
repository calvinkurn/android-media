package com.tokopedia.home.beranda.data.mapper

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.home.beranda.domain.gql.recommendationcard.AdsBanner
import com.tokopedia.home.beranda.domain.gql.recommendationcard.GetHomeRecommendationCardResponse
import com.tokopedia.home.beranda.domain.gql.recommendationcard.PlayVideoWidgetResponse
import com.tokopedia.home.beranda.domain.gql.recommendationcard.RecommendationCard
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BaseHomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil.LAYOUT_NAME_LIST
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecomEntityCardUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAdsLog
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAppLog
import com.tokopedia.topads.sdk.domain.model.ImageShop
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
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
            mutableListOf<BaseHomeRecommendationVisitable>()

        getHomeRecommendationCard.recommendationCards.forEachIndexed { index, card ->
            when (card.layout) {
                TYPE_PRODUCT -> {
                    homeRecommendationTypeFactoryImplList.add(
                        mapToHomeProductFeedModel(
                            getHomeRecommendationCard.appLog,
                            card,
                            getHomeRecommendationCard.pageName,
                            getHomeRecommendationCard.layoutName,
                            pageNumber,
                            index,
                            getHomeRecommendationCard.recommendationCards.size,
                            tabName,
                        )
                    )
                }

                TYPE_RECOM_CARD -> {
                    if (getHomeRecommendationCard.layoutName != LAYOUT_NAME_LIST) {
                        homeRecommendationTypeFactoryImplList.add(
                            mapToEntityCardRecommendationCard(card, index, getHomeRecommendationCard.pageName)
                        )
                    }
                }

                TYPE_BANNER_ADS -> {
                    if (getHomeRecommendationCard.layoutName != LAYOUT_NAME_LIST) {
                        val adsBannerItemResponse =
                            convertDataJsonToAdsBannerItem(card.dataStringJson)

                        adsBannerItemResponse?.let { bannerItemResponse ->
                            homeRecommendationTypeFactoryImplList.add(
                                HomeRecommendationBannerTopAdsUiModel(
                                    layoutCard = card.layout,
                                    layoutItem = card.layoutTracker,
                                    categoryId = card.categoryID,
                                    position = index,
                                    cardId = card.id,
                                    topAdsImageUiModel = mapToTopAdsImageViewModel(
                                        bannerItemResponse
                                    ),
                                    pageName = getHomeRecommendationCard.pageName
                                )
                            )
                        }
                    }
                }

                TYPE_VIDEO -> {
                    if (getHomeRecommendationCard.layoutName != LAYOUT_NAME_LIST) {
                        val recommendationPlayWidgetResponse =
                            convertDataJsonToRecommendationPlayWidget(
                                card.dataStringJson
                            )
                        recommendationPlayWidgetResponse?.let {
                            homeRecommendationTypeFactoryImplList.add(
                                mapToHomeRecommendationPlayWidget(
                                    card = card,
                                    playVideoWidgetResponse = it,
                                    pageName = getHomeRecommendationCard.pageName,
                                    position = index
                                )
                            )
                        }
                    }
                }
            }
        }

        return HomeRecommendationDataModel(
            homeRecommendationTypeFactoryImplList.toList(),
            getHomeRecommendationCard.hasNextPage,
        )
    }

    private fun mapToHomeRecommendationPlayWidget(
        card: RecommendationCard,
        playVideoWidgetResponse: PlayVideoWidgetResponse,
        pageName: String,
        position: Int,
    ): HomeRecommendationPlayWidgetUiModel {
        return HomeRecommendationPlayWidgetUiModel(
            cardId = card.id,
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
                isAutoPlay = playVideoWidgetResponse.basic.autoPlay,
                shopAppLink = playVideoWidgetResponse.author.appLink
            ),
            playVideoTrackerUiModel = HomeRecommendationPlayWidgetUiModel.HomeRecommendationPlayVideoTrackerUiModel(
                videoType = playVideoWidgetResponse.basic.type.text,
                partnerId = playVideoWidgetResponse.author.id,
                playChannelId = playVideoWidgetResponse.contentOriginID,
                recommendationType = playVideoWidgetResponse.recommendationType,
                layoutCard = card.layout,
                layoutItem = card.layoutTracker,
                categoryId = (
                    playVideoWidgetResponse.category.dominantL3.firstOrNull()
                        ?: ""
                    ).toString()
            ),
            isAds = card.isTopads,
            shopId = card.shop.id,
            pageName = pageName,
            position = position,
        )
    }

    private fun mapToEntityCardRecommendationCard(
        recommendationCard: RecommendationCard,
        index: Int,
        pageName: String
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
            ),
            position = index,
            isAds = recommendationCard.isTopads,
            shopId = recommendationCard.shop.id,
            pageName = pageName
        )
    }

    private fun convertDataJsonToAdsBannerItem(dataStringJson: String): AdsBanner? {
        return convertDataJsonToModel<AdsBanner>(dataStringJson)
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

    private fun mapToTopAdsImageViewModel(adsBannerItemResponse: AdsBanner): TopAdsImageUiModel {
        val adsBanner = adsBannerItemResponse.banner
        val imageShop = adsBannerItemResponse.banner.shop.image
        val imageBanner = adsBannerItemResponse.banner.bannerImages.firstOrNull()

        return TopAdsImageUiModel(
            bannerId = adsBannerItemResponse.id,
            bannerName = adsBanner.name,
            position = adsBanner.position.toIntSafely(),
            ImpressHolder = ImageShop(
                imageShop.cover,
                imageShop.sURL,
                imageShop.xsURL,
                imageShop.coverEcs,
                imageShop.sEcs,
                imageShop.xsEcs
            ),
            layoutType = adsBannerItemResponse.banner.layoutType,
            adClickUrl = adsBannerItemResponse.adClickURL,
            adViewUrl = adsBannerItemResponse.adViewURL,
            applink = adsBannerItemResponse.appLink,
            imageUrl = imageBanner?.url.orEmpty(),
            imageWidth = imageBanner?.dimension?.width.orZero(),
            imageHeight = imageBanner?.dimension?.height.orZero(),
            shopId = adsBannerItemResponse.banner.shop.id
        )
    }

    private fun mapToHomeProductFeedModel(
        appLog: GetHomeRecommendationCardResponse.GetHomeRecommendationCard.AppLog,
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
            tabName,
            appLog.toAppLogModel(recommendationCard.recParam),
        )
    }

    private fun GetHomeRecommendationCardResponse.GetHomeRecommendationCard.AppLog.toAppLogModel(
        recParam: String = ""
    ): RecommendationAppLog {
        return RecommendationAppLog(
            sessionId = sessionId,
            requestId = requestId,
            logId = logId,
            recParam = recParam
        )
    }

    private fun mapToLabelGroupList(recommendationCard: RecommendationCard): List<ProductCardModel.LabelGroup> {
        return recommendationCard.labelGroup.map {
            ProductCardModel.LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.url,
                styleList = it.styles.map {
                    ProductCardModel.LabelGroup.Style(it.key, it.value)
                }
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
                ProductCardModel.ShopBadge(title = it.title, imageUrl = it.imageUrl)
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
    }
}

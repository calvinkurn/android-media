package com.tokopedia.home.beranda.data.mapper

import com.google.gson.Gson
import com.tokopedia.home.beranda.data.model.AdsBannerItemResponse
import com.tokopedia.home.beranda.domain.gql.recommendationcard.GetHomeRecommendationCardResponse
import com.tokopedia.home.beranda.domain.gql.recommendationcard.RecommendationCard
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.domain.model.ImageShop
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import dagger.Lazy
import javax.inject.Inject

class HomeRecommendationCardMapper @Inject constructor(
    private val gson: Lazy<Gson>
) {

    fun mapToRecommendationCardDataModel(
        getHomeRecommendationCard: GetHomeRecommendationCardResponse.GetHomeRecommendationCard,
        pageNumber: Int
    ): HomeRecommendationDataModel {
        val homeRecommendationVisitableList = mutableListOf<HomeRecommendationVisitable>()

        getHomeRecommendationCard.recommendationCards.forEachIndexed { index, card ->
            when (card.layout) {
                TYPE_PRODUCT -> {
                    homeRecommendationVisitableList.add(
                        mapToHomeProductFeedModel(
                            card,
                            getHomeRecommendationCard.pageName,
                            getHomeRecommendationCard.layoutName,
                            pageNumber,
                            index,
                            getHomeRecommendationCard.recommendationCards.size
                        )
                    )
                }

                TYPE_BANNER_ADS -> {
                    // todo mapping topadsImageViewModel
                    val adsBannerItemResponse = convertDataJsonToAdsBannerItem(card.dataStringJson)

                    adsBannerItemResponse?.let { bannerItemResponse ->
                        homeRecommendationVisitableList.add(
                            HomeRecommendationBannerTopAdsDataModel(
                                position = index,
                                topAdsImageViewModel = mapToTopAdsImageViewModel(
                                    bannerItemResponse
                                ),
                                bannerType = TYPE_BANNER_ADS // todo need to follow again
                            )
                        )
                    }
                }
            }
        }

        return HomeRecommendationDataModel(
            homeRecommendationVisitableList,
            getHomeRecommendationCard.hasNextPage
        )
    }

    private fun convertDataJsonToAdsBannerItem(dataStringJson: String): AdsBannerItemResponse? {
        return try {
            gson.get().fromJson(dataStringJson, AdsBannerItemResponse::class.java)
        } catch (e: Exception) {
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
            imageUrl = imageBanner?.uRL.orEmpty(),
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
        cardTotal: Int
    ): HomeRecommendationItemDataModel {
        val productCard = mapToProductCardModel(recommendationCard)

        return HomeRecommendationItemDataModel(
            null,
            recommendationCard,
            productCard,
            pageName,
            layoutName,
            (((pageNumber - Int.ONE) * cardTotal) + index + Int.ONE)
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
        private const val TYPE_BANNER = "banner"
        private const val TYPE_BANNER_ADS = "banner_ads"
    }
}

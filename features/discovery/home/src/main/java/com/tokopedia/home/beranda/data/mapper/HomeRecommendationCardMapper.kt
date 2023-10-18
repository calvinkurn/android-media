package com.tokopedia.home.beranda.data.mapper

import com.tokopedia.home.beranda.domain.gql.recommendationcard.Card
import com.tokopedia.home.beranda.domain.gql.recommendationcard.GetHomeRecommendationCardResponse
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.productcard.ProductCardModel
import javax.inject.Inject

class HomeRecommendationCardMapper @Inject constructor() {

    fun mapToRecommendationCardDataModel(
        getHomeRecommendationCard: GetHomeRecommendationCardResponse.GetHomeRecommendationCard,
        pageNumber: Int
    ): HomeRecommendationDataModel {
        val homeRecommendationVisitableList = mutableListOf<HomeRecommendationVisitable>()

        getHomeRecommendationCard.cards.forEachIndexed { index, card ->
            when (card.layout) {
                TYPE_PRODUCT -> {
                    homeRecommendationVisitableList.add(
                        mapToHomeProductFeedModel(
                            card,
                            getHomeRecommendationCard.pageName,
                            getHomeRecommendationCard.layoutName,
                            pageNumber,
                            index,
                            getHomeRecommendationCard.cards.size
                        )
                    )
                }

                TYPE_BANNER_ADS -> {
                    homeRecommendationVisitableList.add(HomeRecommendationBannerTopAdsDataModel(position = index))
                }
            }
        }

        return HomeRecommendationDataModel()
    }

    private fun mapToHomeProductFeedModel(
        card: Card,
        pageName: String,
        layoutName: String,
        pageNumber: Int,
        index: Int,
        cardTotal: Int
    ): HomeRecommendationItemDataModel {
        val productCard = mapToProductCardModel(card)

        return HomeRecommendationItemDataModel(
            product = null,
            card,
            productCard,
            pageName,
            layoutName,
            (((pageNumber - Int.ONE) * cardTotal) + index + Int.ONE)
        )
    }

    private fun mapToLabelGroupList(card: Card): List<ProductCardModel.LabelGroup> {
        return card.labelGroup.map {
            ProductCardModel.LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.url
            )
        }
    }

    private fun mapToProductCardModel(card: Card): ProductCardModel {
        val labelGroups = mapToLabelGroupList(card)

        return ProductCardModel(
            slashedPrice = card.slashedPrice,
            productName = card.name,
            formattedPrice = card.price,
            productImageUrl = card.imageUrl,
            isTopAds = card.isTopads,
            discountPercentage = if (card.discountPercentage > 0) "${card.discountPercentage}%" else "",
            ratingCount = card.rating,
            reviewCount = card.countReview,
            countSoldRating = card.ratingAverage,
            shopLocation = card.shop.city,
            isWishlistVisible = true,
            isWishlisted = card.isWishlist,
            shopBadgeList = card.badges.map {
                ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                isActive = card.freeOngkir.isActive,
                imageUrl = card.freeOngkir.imageUrl
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

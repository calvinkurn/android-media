package com.tokopedia.gamification.pdp.domain

import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import java.util.*

class Mapper {

    suspend fun recommWidgetToListOfVisitables(recommendationWidget: RecommendationWidget): List<Recommendation> {
        val recomendationList = ArrayList<Recommendation>()
        for (item in recommendationWidget.recommendationItemList) {
            recomendationList.add(Recommendation(item, getProductModel(item)))
        }
        return recomendationList
    }

    suspend fun getProductModel(element: RecommendationItem): ProductCardModel {
        return ProductCardModel(
                slashedPrice = element.slashedPrice,
                productName = element.name,
                formattedPrice = element.price,
                productImageUrl = element.imageUrl,
                isTopAds = element.isTopAds,
                discountPercentage = element.discountPercentage.toString(),
                reviewCount = element.countReview,
                ratingCount = element.rating,
                shopLocation = element.location,
                shopBadgeList = element.badgesUrl.map {
                    ProductCardModel.ShopBadge(imageUrl = it
                            ?: "")
                },
                freeOngkir = ProductCardModel.FreeOngkir(
                        isActive = element.isFreeOngkirActive,
                        imageUrl = element.freeOngkirImageUrl
                ),
                labelGroupList = element.labelGroupList.map { recommendationLabel ->
                    ProductCardModel.LabelGroup(
                            position = recommendationLabel.position,
                            title = recommendationLabel.title,
                            type = recommendationLabel.type
                    )
                }
        )
    }
}
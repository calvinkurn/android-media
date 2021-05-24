package com.tokopedia.tokopoints.view.recommwidget

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopoints.view.tokopointhome.RecommendationWrapper
import java.util.*

class DataMapper {

    suspend fun recommWidgetToListOfVisitables(recommendationWidget: RecommendationWidget): List<RecommendationWrapper> {
        val recommendationList = ArrayList<RecommendationWrapper>()
        for (i in 0..9) {
            recommendationList.add(
                RecommendationWrapper(
                    recommendationWidget.recommendationItemList[i],
                    getProductModel(recommendationWidget.recommendationItemList[i])
                )
            )
        }
        return recommendationList
    }

    fun getProductModel(element: RecommendationItem): ProductCardModel {
        return ProductCardModel(
            productName = element.name,
            formattedPrice = element.price,
            productImageUrl = element.imageUrl,
            isTopAds = element.isTopAds,
            ratingCount = element.rating,
            hasThreeDots = false,
            shopRating = element.ratingAverage,
            isShopRatingYellow = true,
            shopLocation = element.location,
            shopBadgeList = element.badgesUrl.map {
                ProductCardModel.ShopBadge(
                    imageUrl = it
                        ?: ""
                )
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                isActive = element.isFreeOngkirActive,
                imageUrl = element.freeOngkirImageUrl
            )
        )
    }
}
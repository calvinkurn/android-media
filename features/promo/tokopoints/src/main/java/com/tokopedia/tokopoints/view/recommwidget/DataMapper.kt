package com.tokopedia.tokopoints.view.recommwidget

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopoints.view.tokopointhome.RecommendationWrapper
import java.util.*

class DataMapper {
    val RECOM_PRODUCT_SIZE = 10
    val recommendationList = ArrayList<RecommendationWrapper>()

    suspend fun recommWidgetToListOfVisitables(recommendationWidget: RecommendationWidget): List<RecommendationWrapper> {
        if (recommendationList.size >= RECOM_PRODUCT_SIZE) {
            composeRecommendationList(RECOM_PRODUCT_SIZE, recommendationWidget)
        } else {
            composeRecommendationList(recommendationList.size, recommendationWidget)
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

    fun composeRecommendationList(size: Int , recommendationWidget:RecommendationWidget){
        for (i in 0 until size) {
            recommendationList.add(
                RecommendationWrapper(
                    recommendationWidget.recommendationItemList[i],
                    getProductModel(recommendationWidget.recommendationItemList[i])
                )
            )
        }
    }
}
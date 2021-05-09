package com.tokopedia.tokopoints.view.recommwidget

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import java.util.*

class DataMapper {

    suspend fun recommWidgetToListOfVisitables(recommendationWidget: RecommendationWidget): List<ProductCardModel> {
        val recommendationList = ArrayList<ProductCardModel>()
        for (i in 0..5) {
            recommendationList.add(getProductModel(recommendationWidget.recommendationItemList[i]))
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
            hasThreeDots = true,
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
            )/*
            labelGroupList = element.labelGroupList.map { recommendationLabel ->
                ProductCardModel.LabelGroup(
                    position = recommendationLabel.position,
                    title = recommendationLabel.title,
                    type = recommendationLabel.type
                )*/
        )
    }
}
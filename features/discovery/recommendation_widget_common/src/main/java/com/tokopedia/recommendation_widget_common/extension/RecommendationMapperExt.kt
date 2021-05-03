package com.tokopedia.recommendation_widget_common.extension

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationSpecificationLabels
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsMapper
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Lukas on 2/15/21.
 */

fun List<RecommendationEntity.RecommendationData>.mappingToRecommendationModel(): List<RecommendationWidget> {
    return map { recommendationData ->
        recommendationData.toRecommendationWidget()
    }
}

fun RecommendationEntity.RecommendationData.toRecommendationWidget(): RecommendationWidget{
    return RecommendationWidget(
            recommendation.mapIndexed { index, recommendation ->
                RecommendationItem(
                        productId = recommendation.id.toInt(),
                        name = recommendation.name,
                        categoryBreadcrumbs = recommendation.categoryBreadcrumbs,
                        url = recommendation.url,
                        appUrl = recommendation.appUrl,
                        clickUrl = recommendation.clickUrl,
                        wishlistUrl = recommendation.wishlistUrl,
                        trackerImageUrl = recommendation.trackerImageUrl,
                        imageUrl = recommendation.imageUrl,
                        price = recommendation.price,
                        priceInt = recommendation.priceInt,
                        departmentId = recommendation.departmentId,
                        rating = recommendation.rating,
                        ratingAverage = recommendation.ratingAverage,
                        countReview = recommendation.countReview,
                        stock = recommendation.stock,
                        recommendationType = recommendation.recommendationType,
                        isTopAds = recommendation.isIsTopads,
                        isWishlist = recommendation.isWishlist,
                        slashedPrice = recommendation.slashedPrice,
                        slashedPriceInt =  recommendation.slashedPriceInt,
                        discountPercentage = if(recommendation.discountPercentage > 0) "${recommendation.discountPercentage}%" else "",
                        discountPercentageInt = recommendation.discountPercentage,
                        position = index,
                        shopId = recommendation.shop.id,
                        shopName = recommendation.shop.name,
                        quantity = 1,
                        header = title,
                        pageName = pageName,
                        minOrder = recommendation.minOrder,
                        location = recommendation.shop.city,
                        badgesUrl = recommendation.badges.map { it.imageUrl },
                        type = layoutType,
                        isFreeOngkirActive = recommendation.freeOngkirInformation.isActive,
                        freeOngkirImageUrl = recommendation.freeOngkirInformation.imageUrl,
                        labelGroupList = recommendation.labelGroups.map {
                            RecommendationLabel(title = it.title, type = it.type, position = it.position, imageUrl = it.imageUrl)
                        },
                        isGold = recommendation.shop.isGold,
                        isOfficial = recommendation.shop.isOfficial,
                        specs = recommendation.specificationsLabels.map {
                            RecommendationSpecificationLabels(
                                    specTitle = it.key,
                                    specSummary = it.value
                            )
                        }
                )
            },
            title = title,
            foreignTitle = foreignTitle,
            subtitle = subtitle,
            source = source,
            tid = tid,
            widgetUrl = widgetUrl,
            layoutType = layoutType,
            seeMoreAppLink = seeMoreAppLink,
            currentPage = pagination.currentPage,
            nextPage = pagination.nextPage,
            prevPage = pagination.prevPage,
            hasNext = pagination.hasNext,
            pageName = pageName
    )
}

fun List<RecommendationItem>.toProductCardModels(hasThreeDots: Boolean = false): List<ProductCardModel>{
    return map {
        it.toProductCardModel(hasThreeDots = hasThreeDots)
    }
}

fun RecommendationItem.toProductCardModel(
        hasAddToCartButton: Boolean = false,
        addToCartButtonType: Int = UnifyButton.Type.TRANSACTION,
        hasThreeDots: Boolean = false
) : ProductCardModel{
    return ProductCardModel(
            slashedPrice = slashedPrice,
            productName = name,
            formattedPrice = price,
            productImageUrl = imageUrl,
            isTopAds = isTopAds,
            isWishlistVisible = true,
            hasThreeDots = hasThreeDots,
            isWishlisted = isWishlist,
            discountPercentage = discountPercentage,
            reviewCount = countReview,
            ratingCount = rating,
            shopLocation = location,
            countSoldRating = ratingAverage,
            shopBadgeList = badgesUrl.map {
                ProductCardModel.ShopBadge(imageUrl = it)
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                    isActive = isFreeOngkirActive,
                    imageUrl = freeOngkirImageUrl
            ),
            labelGroupList = labelGroupList.map {
                ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type, imageUrl=it.imageUrl)
            },
            hasAddToCartButton = hasAddToCartButton,
            addToCartButtonType = addToCartButtonType
    )
}

var LABEL_FULFILLMENT: String = "fulfillment"

fun List<RecommendationLabel>.hasLabelGroupFulfillment(): Boolean{
    return this.any { it.position == LABEL_FULFILLMENT }
}
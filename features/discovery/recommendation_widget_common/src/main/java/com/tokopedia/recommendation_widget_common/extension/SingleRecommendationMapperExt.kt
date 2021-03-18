package com.tokopedia.recommendation_widget_common.extension

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Devara Fikry on 2/15/21.
 */

fun List<RecommendationItem>.toProductCardModels(): List<ProductCardModel>{
    return map {
        it.toProductCardModel()
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
                ProductCardModel.ShopBadge(imageUrl = it?:"")
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                    isActive = isFreeOngkirActive,
                    imageUrl = freeOngkirImageUrl
            ),
            labelGroupList = labelGroupList.map {
                ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type)
            },
            hasAddToCartButton = hasAddToCartButton,
            addToCartButtonType = addToCartButtonType
    )
}


var LABEL_FULFILLMENT: String = "fulfillment"

fun List<RecommendationLabel>.hasLabelGroupFulfillment(): Boolean{
    return this.any { it.position == LABEL_FULFILLMENT }
}
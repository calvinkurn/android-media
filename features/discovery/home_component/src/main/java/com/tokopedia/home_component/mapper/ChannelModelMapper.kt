package com.tokopedia.home_component.mapper

import com.tokopedia.home_component.model.*
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.CardUnify2

object ChannelModelMapper {
    fun mapToProductCardModel(channelGrid: ChannelGrid, cardInteration: Int): ProductCardModel {
        return ProductCardModel(
                slashedPrice = channelGrid.slashedPrice,
                productName = channelGrid.name,
                formattedPrice = channelGrid.price,
                productImageUrl = channelGrid.imageUrl,
                discountPercentage = channelGrid.discount,
                pdpViewCount = channelGrid.productViewCountFormatted,
                stockBarLabel = channelGrid.label,
                isTopAds = channelGrid.isTopads,
                stockBarPercentage = channelGrid.soldPercentage,
                shopLocation = channelGrid.shop.shopLocation,
                shopBadgeList = channelGrid.badges.map {
                    ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
                },
                labelGroupList = channelGrid.labelGroup.map {
                    ProductCardModel.LabelGroup(
                            position = it.position,
                            title = it.title,
                            type = it.type,
                            imageUrl = it.url
                    )
                },
                freeOngkir = ProductCardModel.FreeOngkir(
                        channelGrid.isFreeOngkirActive,
                        channelGrid.freeOngkirImageUrl
                ),
                isOutOfStock = channelGrid.isOutOfStock,
                ratingCount = channelGrid.rating,
                countSoldRating = channelGrid.ratingFloat,
                reviewCount = channelGrid.countReview,
                animationOnPress = cardInteration,
        )
    }
}
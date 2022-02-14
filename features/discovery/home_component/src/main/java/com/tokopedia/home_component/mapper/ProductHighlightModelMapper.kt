package com.tokopedia.home_component.mapper

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.productcard.ProductCardModel

object ProductHighlightModelMapper {
     fun mapToProductCardModel(channelGrid: ChannelGrid): ProductCardModel {
         with(channelGrid) {
             return ProductCardModel(
                 slashedPrice = slashedPrice,
                 productName = name,
                 formattedPrice = price,
                 productImageUrl = imageUrl,
                 discountPercentage = discount,
                 pdpViewCount = productViewCountFormatted,
                 stockBarLabel = label,
                 stockBarPercentage = soldPercentage,
                 labelGroupList = labelGroup.map {
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
                 isOutOfStock = isOutOfStock,
                 ratingCount = rating,
                 reviewCount = countReview,
                 countSoldRating = ratingFloat
             )
         }
     }
}
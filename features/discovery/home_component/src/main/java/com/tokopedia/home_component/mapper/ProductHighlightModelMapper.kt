package com.tokopedia.home_component.mapper

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.productcard.ProductCardModel

object ProductHighlightModelMapper {
     fun mapToProductCardModel(channelGrid: ChannelGrid): ProductCardModel {
            return ProductCardModel(
                slashedPrice = channelGrid.slashedPrice,
                productName = channelGrid.name,
                formattedPrice = channelGrid.price,
                productImageUrl = channelGrid.imageUrl,
                discountPercentage = channelGrid.discount,
                stockBarLabel = channelGrid.label,
                stockBarPercentage = channelGrid.soldPercentage,
                pdpViewCount = channelGrid.productViewCountFormatted,
                isOutOfStock = channelGrid.isOutOfStock,
                freeOngkir = ProductCardModel.FreeOngkir(
                    channelGrid.isFreeOngkirActive,
                    channelGrid.freeOngkirImageUrl
                ),
            )
        }
}
package com.tokopedia.home_component.mapper

import com.tokopedia.home_component.model.*
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.CardUnify2.Companion.ANIMATE_OVERLAY
import com.tokopedia.unifycomponents.CardUnify2.Companion.ANIMATE_OVERLAY_BOUNCE

object ChannelModelMapper {
    fun mapToProductCardModel(
        channelGrid: ChannelGrid,
        cardInteration: Boolean? = null, //replaced with animateOnPress
        animateOnPress: Int = ANIMATE_OVERLAY,
        isTopStockbar: Boolean = false,
        cardType: Int = CardUnify2.TYPE_SHADOW,
        productCardListType: ProductCardModel.ProductListType = ProductCardModel.ProductListType.CONTROL,
        excludeShop: Boolean = false,
        excludeLabelGroup: List<String> = emptyList(),
    ): ProductCardModel {
        val productCardAnimate = if(cardInteration == true) ANIMATE_OVERLAY_BOUNCE else animateOnPress
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
            shopLocation = channelGrid.shop.shopLocation.takeIf { !excludeShop }.orEmpty(),
            shopBadgeList = channelGrid.badges.map {
                ProductCardModel.ShopBadge(title = it.title, imageUrl = it.imageUrl)
            }.takeIf { !excludeShop }.orEmpty(),
            labelGroupList = channelGrid.labelGroup.filterNot { label ->
                excludeLabelGroup.any {
                    it == label.position
                }
            }.map {
                ProductCardModel.LabelGroup(
                    position = it.position,
                    title = it.title,
                    type = it.type,
                    imageUrl = it.url,
                    styleList = it.styles.map {  style ->
                        ProductCardModel.LabelGroup.Style(
                            style.key,
                            style.value
                        )
                    }
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
            animateOnPress = productCardAnimate,
            isTopStockBar = isTopStockbar,
            cardType = cardType,
            productListType = productCardListType
        )
    }
}

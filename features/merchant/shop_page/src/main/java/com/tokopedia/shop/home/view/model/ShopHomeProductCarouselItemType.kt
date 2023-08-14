package com.tokopedia.shop.home.view.model

sealed interface ShopHomeProductCarouselItemType {
    val id: String
}

data class ProductCard(
    val productId: String,
    val imageUrl: String,
    val name: String,
    val price: String,
    val slashedPrice: String,
    val slashedPricePercent: Int,
    val rating: String,
    val soldCount: Int,
    val appLink: String,
    override val id : String = productId
) : ShopHomeProductCarouselItemType

data class VerticalBanner(
    val imageUrl: String,
    val bannerType: String,
    val appLink: String,
    override val id : String = imageUrl
) : ShopHomeProductCarouselItemType

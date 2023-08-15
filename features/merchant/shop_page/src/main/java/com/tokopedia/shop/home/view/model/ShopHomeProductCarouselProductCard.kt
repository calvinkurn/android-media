package com.tokopedia.shop.home.view.model

data class ShopHomeProductCarouselProductCard(
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
) : ShopHomeProductCarouselVerticalBannerItemType

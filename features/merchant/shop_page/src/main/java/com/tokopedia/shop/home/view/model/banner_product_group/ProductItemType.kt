package com.tokopedia.shop.home.view.model.banner_product_group

data class ProductItemType(
    val productId: String,
    val imageUrl: String,
    val name: String,
    val price: String,
    val slashedPrice: String,
    val slashedPricePercent: Int,
    val rating: String,
    val soldCount: String,
    val appLink: String,
    val showProductInfo: Boolean,
    override val id : String = productId
) : ShopHomeBannerProductGroupItemType

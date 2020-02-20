package com.tokopedia.productcard.v2

data class ProductCardModel (
        val productImageUrl: String = "",
        var isWishlisted: Boolean = false,
        val isWishlistVisible: Boolean = false,
        val labelPromo: Label = Label(),
        val shopImageUrl: String = "",
        val shopName: String = "",
        val productName: String = "",
        val discountPercentage: String = "",
        val slashedPrice: String = "",
        val formattedPrice: String = "",
        val shopBadgeList: List<ShopBadge> = listOf(),
        val shopLocation: String = "",
        val ratingCount: Int = 0,
        val reviewCount: Int = 0,
        val labelCredibility: Label = Label(),
        val labelOffers: Label = Label(),
        val freeOngkir: FreeOngkir = FreeOngkir(),
        val isTopAds: Boolean = false
) {
    data class Label(
            val title: String = "",
            val type: String = ""
    )

    data class FreeOngkir(
            val isActive: Boolean = false,
            val imageUrl: String = ""
    )

    data class ShopBadge(
            val isShown: Boolean = true,
            val imageUrl: String = ""
    )
}
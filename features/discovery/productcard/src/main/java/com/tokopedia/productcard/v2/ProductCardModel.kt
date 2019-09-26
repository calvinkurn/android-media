package com.tokopedia.productcard.v2

data class ProductCardModel (
    val productImageUrl: String = "",
    val isWishlisted: Boolean = false,
    val isWishlistVisible: Boolean = false,
    val labelPromo: String = "",
    val labelPromoType: String = "",
    val freeOngkir: FreeOngkir = FreeOngkir(),
    val shopImageUrl: String = "",
    val shopName: String = "",
    val productName: String = "",
    val discountPercentage: String = "",
    val slashedPrice: String = "",
    val formattedPrice: String = "",
    val shopLocation: String = "",
    val ratingCount: Int = 0,
    val reviewCount: Int = 0,
    val labelCredibility: String = "",
    val labelCredibilityType: String = "",
    val labelOffers: String = "",
    val labelOffersType: String = "",
    val isTopAds: Boolean = false
) {

    data class FreeOngkir(
            val isActive: Boolean = false,
            val imageUrl: String = ""
    )
}
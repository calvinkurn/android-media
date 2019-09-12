package com.tokopedia.productcard.v2

class ProductCardModel (
    val productImageUrl: String = "",
    val isWishlisted: Boolean = false,
    val labelPromo: String = "",
    val labelPromoType: String = "",
    val shopImageUrl: String = "",
    val shopName: String = "",
    val productName: String = "",
    val discountPercentage: Int = 0,
    val slashedPrice: String = "",
    val shopLocation: String = "",
    val ratingCount: Int = 0,
    val reviewCount: Int = 0,
    val labelCredibility: String = "",
    val labelCredibilityType: String = "",
    val labelOffers: String = "",
    val labelOffersType: String = "",
    val isTopAds: Boolean = false
)
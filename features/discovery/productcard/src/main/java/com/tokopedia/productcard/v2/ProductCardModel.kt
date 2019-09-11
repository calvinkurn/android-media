package com.tokopedia.productcard.v2

class ProductCardModel (
    val productID: String = "",
    val productName: String = "",
    val imageUrl: String = "",
    val rating: Int = 0,
    val price: String = "",
    val isWishlisted: Boolean = false,
    val position: Int = 0,
    val originalPrice: String = "",
    val discountPercentage: Int = 0,
    val productWishlistUrl: String = "",
    val isTopAds: Boolean = false

)
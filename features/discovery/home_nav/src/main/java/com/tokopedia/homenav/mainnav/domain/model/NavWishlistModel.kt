package com.tokopedia.homenav.mainnav.domain.model

data class NavWishlistModel(
    val id: String? = "",
    val productName: String? = "",
    val imageUrl: String? = "",
    val priceFmt: String? = "",
    val originalPriceFmt: String? = "",
    val discountPercentageFmt: String? = "",
    val cashback: String? = ""
)

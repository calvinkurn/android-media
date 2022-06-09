package com.tokopedia.homenav.mainnav.domain.model

import com.tokopedia.homenav.mainnav.data.pojo.wishlist.Category

data class NavWishlistModel(
    val productId: String = "",
    val productName: String = "",
    val imageUrl: String = "",
    val price: String = "",
    val priceFmt: String = "",
    val originalPriceFmt: String = "",
    val discountPercentageFmt: String = "",
    val cashback: Boolean = false,
    val category: List<Category> = listOf(),
    val categoryBreadcrumb: String = "",
    val variant: String = "",
    val wishlistId: String = "",
    var fullWidth: Boolean = false
)

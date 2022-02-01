package com.tokopedia.vouchercreation.product.create.domain.entity

data class CouponProduct(
    val id: String,
    val price: Int,
    val rating: Float,
    val imageUrl: String,
    val soldCount: Int
)
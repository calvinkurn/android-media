package com.tokopedia.vouchercreation.product.create.domain.entity

data class CouponProduct(
    val id: Int,
    val price: Int,
    val rating: Float,
    val imageUrl: String,
    val soldCount: Int
)
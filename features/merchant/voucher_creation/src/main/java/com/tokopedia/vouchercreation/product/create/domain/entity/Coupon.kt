package com.tokopedia.vouchercreation.product.create.domain.entity

import java.io.Serializable

data class Coupon(
    val id: Int,
    val imageSquareUrl: String,
    val information: CouponInformation,
    val settings: CouponSettings,
    val products: List<CouponProduct>
) : Serializable
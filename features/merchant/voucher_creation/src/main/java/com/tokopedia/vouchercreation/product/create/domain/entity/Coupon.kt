package com.tokopedia.vouchercreation.product.create.domain.entity

import java.io.Serializable

data class Coupon(
    val id: Long,
    val information: CouponInformation,
    val settings: CouponSettings,
    val products: List<CouponProduct>
) : Serializable
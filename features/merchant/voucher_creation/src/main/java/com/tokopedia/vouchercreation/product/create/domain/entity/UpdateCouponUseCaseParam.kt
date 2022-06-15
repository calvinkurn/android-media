package com.tokopedia.vouchercreation.product.create.domain.entity

data class UpdateCouponUseCaseParam(
    val couponId : Long,
    val couponInformation: CouponInformation,
    val couponSettings: CouponSettings,
    val couponProducts: List<CouponProduct>,
    val token: String,
    val imageUrl : String,
    val imageSquare: String,
    val imagePortrait: String,
    val warehouseId: String
)

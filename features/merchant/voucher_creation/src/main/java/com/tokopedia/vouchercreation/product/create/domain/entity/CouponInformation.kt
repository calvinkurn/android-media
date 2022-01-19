package com.tokopedia.vouchercreation.product.create.domain.entity

data class CouponInformation(
    val target: String,
    val name: String,
    val code: String,
    val period: String
)
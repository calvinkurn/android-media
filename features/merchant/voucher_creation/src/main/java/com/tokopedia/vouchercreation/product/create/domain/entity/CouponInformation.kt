package com.tokopedia.vouchercreation.product.create.domain.entity

import java.util.*

data class CouponInformation(
    val target: String,
    val name: String,
    val code: String,
    val period: Period
) {
    data class Period(val startDate : Date, val endDate : Date)
}
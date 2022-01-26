package com.tokopedia.vouchercreation.product.create.domain.entity

import java.io.Serializable
import java.util.*

data class CouponInformation(
    val target: Target,
    val name: String,
    val code: String,
    val period: Period
) : Serializable {
    enum class Target : Serializable {
        PUBLIC,
        SPECIAL
    }
    data class Period(val startDate : Date, val endDate : Date) : Serializable
}
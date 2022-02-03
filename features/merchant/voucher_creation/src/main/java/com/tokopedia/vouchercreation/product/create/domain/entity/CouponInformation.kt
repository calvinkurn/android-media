package com.tokopedia.vouchercreation.product.create.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
data class CouponInformation(
    val target: Target,
    val name: String,
    val code: String,
    val period: Period
) : Parcelable {
    @Parcelize
    enum class Target : Parcelable {
        PUBLIC,
        SPECIAL
    }
    @Parcelize
    data class Period(val startDate : Date, val endDate : Date) : Parcelable
}
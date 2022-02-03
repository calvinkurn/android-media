package com.tokopedia.vouchercreation.product.create.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coupon(
    val id: Long,
    val information: CouponInformation,
    val settings: CouponSettings,
    val products: List<CouponProduct>
): Parcelable
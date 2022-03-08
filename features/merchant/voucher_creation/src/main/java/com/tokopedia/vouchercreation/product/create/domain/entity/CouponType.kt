package com.tokopedia.vouchercreation.product.create.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class CouponType : Parcelable {
    NONE,
    CASHBACK,
    FREE_SHIPPING
}
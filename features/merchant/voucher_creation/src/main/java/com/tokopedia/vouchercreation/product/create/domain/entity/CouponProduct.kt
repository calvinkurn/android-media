package com.tokopedia.vouchercreation.product.create.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CouponProduct(
    val id: String,
    val imageUrl: String,
    val soldCount: Int
) : Parcelable
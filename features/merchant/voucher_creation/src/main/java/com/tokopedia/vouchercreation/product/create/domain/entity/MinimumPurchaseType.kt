package com.tokopedia.vouchercreation.product.create.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MinimumPurchaseType: Parcelable {
    NONE,
    NOMINAL,
    QUANTITY,
    NOTHING
}
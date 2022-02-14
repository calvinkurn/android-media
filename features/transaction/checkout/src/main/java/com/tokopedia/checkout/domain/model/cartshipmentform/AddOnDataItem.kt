package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnDataItem(
    var addOnPrice: Long = 0L,
    var addOnId: Long = 0L,
    var addOnMetadata: String = "",
    var addOnQty: Long = 0L
): Parcelable

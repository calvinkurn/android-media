package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnDataItem(
    var addOnPrice: Long = 0L,
    var addOnId: String = "",
    var addOnMetadata: AddOnMetadataItem = AddOnMetadataItem(),
    var addOnQty: Long = 0L
): Parcelable

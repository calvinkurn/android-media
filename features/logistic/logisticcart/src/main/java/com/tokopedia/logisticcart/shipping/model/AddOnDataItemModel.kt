package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnDataItemModel(
        var addOnPrice: Long = 0L,
        var addOnId: String = "",
        var addOnMetadata: AddOnMetadataItemModel = AddOnMetadataItemModel(),
        var addOnQty: Long = 0L
): Parcelable

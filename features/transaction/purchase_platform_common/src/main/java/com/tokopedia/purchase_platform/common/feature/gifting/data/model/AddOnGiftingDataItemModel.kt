package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnGiftingDataItemModel(
    var addOnPrice: Double = 0.0,
    var addOnId: String = "",
    var addOnMetadata: AddOnGiftingMetadataItemModel = AddOnGiftingMetadataItemModel(),
    var addOnUniqueId: String = "",
    var addOnQty: Long = 0L
) : Parcelable

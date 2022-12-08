package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnDataItemModel(
        var addOnPrice: Double = 0.0,
        var addOnId: String = "",
        var addOnMetadata: AddOnMetadataItemModel = AddOnMetadataItemModel(),
        var addOnQty: Long = 0L
): Parcelable

package com.tokopedia.purchase_platform.common.feature.addons.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnProductDataItemModel(
    var addOnDataId: Long = 0L,
    var addOnDataPrice: Double = 0.0,
    var addOnDataUniqueId: String = "",
    var addOnDataInfoLink: String = "",
    var addOnDataName: String = "",
    var addOnDataStatus: Int = -1,
    var addOnDataType: Int = -1
) : Parcelable

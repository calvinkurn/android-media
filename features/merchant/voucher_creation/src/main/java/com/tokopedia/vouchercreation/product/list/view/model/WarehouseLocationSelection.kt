package com.tokopedia.vouchercreation.product.list.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WarehouseLocationSelection(
        val warehouseId: Int = 0,
        val warehouseName: String = "",
        var isSelected: Boolean = false
) : Parcelable
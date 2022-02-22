package com.tokopedia.localizationchooseaddress.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokonowModel(
        var shopId: Long = 0,
        var warehouseId: Long = 0,
        var warehouses: List<WarehouseModel> = listOf(),
        var serviceType: String = ""
): Parcelable

@Parcelize
data class WarehouseModel(
        var warehouseId: Long = 0,
        var serviceType: String = ""
): Parcelable
package com.tokopedia.mvc.domain.entity

import com.tokopedia.mvc.domain.entity.enums.WarehouseType

data class Warehouse(
    val warehouseId: Long,
    val warehouseName: String,
    val warehouseType: WarehouseType
)

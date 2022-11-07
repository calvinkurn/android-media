package com.tokopedia.mvc.domain.entity

data class Warehouse(
    val warehouseId: Long,
    val warehouseName: String,
    val warehouseType: WarehouseType
)

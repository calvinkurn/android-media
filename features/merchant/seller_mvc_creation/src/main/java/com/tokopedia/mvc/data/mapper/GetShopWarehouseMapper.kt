package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.GetShopWarehouseResponse
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.WarehouseType
import javax.inject.Inject

class GetShopWarehouseMapper @Inject constructor() {

    companion object {
        private const val WAREHOUSE_ID_DEFAULT_WAREHOUSE_LOCATION = 1
    }

    fun map(response: GetShopWarehouseResponse): List<Warehouse> {
        return response.shopLocGetWarehouseByShopIDs.warehouses.map { warehouse ->
            Warehouse(
                warehouse.warehouseId.toLongOrZero(),
                warehouse.warehouseName,
                warehouse.warehouseType.toWarehouseType()
            )
        }
    }

    private fun Int.toWarehouseType(): WarehouseType {
        return if (this == WAREHOUSE_ID_DEFAULT_WAREHOUSE_LOCATION) {
            WarehouseType.DEFAULT_WAREHOUSE_LOCATION
        } else {
            WarehouseType.WAREHOUSE
        }
    }
}

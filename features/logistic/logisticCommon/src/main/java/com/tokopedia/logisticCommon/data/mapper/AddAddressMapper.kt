package com.tokopedia.logisticCommon.data.mapper

import com.tokopedia.logisticCommon.data.entity.address.WarehouseDataModel
import com.tokopedia.logisticCommon.data.response.WarehousesAddAddress

object AddAddressMapper {
    fun mapWarehouses(warehouses: List<WarehousesAddAddress>): List<WarehouseDataModel> {
        return warehouses.map { WarehouseDataModel(warehouseId = it.warehouseId, serviceType = it.serviceType) }
    }
}
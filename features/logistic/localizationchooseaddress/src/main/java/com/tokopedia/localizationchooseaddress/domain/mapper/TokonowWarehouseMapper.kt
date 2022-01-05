package com.tokopedia.localizationchooseaddress.domain.mapper

import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.domain.model.WarehouseModel
import com.tokopedia.localizationchooseaddress.domain.response.Warehouse
import com.tokopedia.logisticCommon.data.entity.address.WarehouseDataModel

object TokonowWarehouseMapper {
    fun mapWarehousesResponseToLocal(warehouses: List<Warehouse>): List<LocalWarehouseModel> {
        return warehouses.map { LocalWarehouseModel(warehouse_id = it.warehouseId, service_type = it.serviceType) }
    }

    fun mapWarehousesAddAddressModelToLocal(warehouses: List<WarehouseDataModel>) : List<LocalWarehouseModel> {
        return warehouses.map { LocalWarehouseModel(warehouse_id = it.warehouseId, service_type = it.serviceType) }
    }

    fun mapWarehousesModelToLocal(warehouses: List<WarehouseModel>) : List<LocalWarehouseModel> {
        return warehouses.map { LocalWarehouseModel(warehouse_id = it.warehouseId, service_type = it.serviceType) }
    }
}
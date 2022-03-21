package com.tokopedia.logisticCommon.data.mapper

import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.WarehouseDataModel
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.data.response.WarehousesAddAddress

object AddAddressMapper {
    fun mapWarehouses(warehouses: List<WarehousesAddAddress>): List<WarehouseDataModel> {
        return warehouses.map { WarehouseDataModel(warehouseId = it.warehouseId, serviceType = it.serviceType) }
    }

    fun mapAddressDetailToSaveAddressDataModel(data: KeroGetAddressResponse.Data) : SaveAddressDataModel {
        return data.keroGetAddress.data.first().let {
            SaveAddressDataModel(
                id = it.addrId,
                addressName = it.addrName,
                receiverName = it.receiverName,
                address1 = it.address1,
                address2 = it.address2,
                postalCode = it.postalCode,
                phone = it.phone,
                cityId = it.city,
                provinceId = it.province,
                districtId = it.district,
                latitude = it.latitude,
                longitude = it.longitude,
            )
        }
    }
}
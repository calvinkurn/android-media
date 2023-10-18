package com.tokopedia.logisticCommon.data.mapper

import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.WarehouseDataModel
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.data.response.WarehousesAddAddress

object AddAddressMapper {
    fun mapWarehouses(warehouses: List<WarehousesAddAddress>): List<WarehouseDataModel> {
        return warehouses.map {
            WarehouseDataModel(
                warehouseId = it.warehouseId,
                serviceType = it.serviceType
            )
        }
    }

    fun mapAddressDetailToSaveAddressDataModel(data: KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse): SaveAddressDataModel {
        return SaveAddressDataModel(
            id = data.addrId,
            addressName = data.addrName,
            receiverName = data.receiverName,
            address1 = data.addressDetailStreet.ifEmpty { data.address1 },
            address1Notes = data.addressDetailNotes,
            address2 = data.address2,
            postalCode = data.postalCode,
            phone = data.phone,
            cityId = data.city,
            provinceId = data.province,
            districtId = data.district,
            latitude = data.latitude,
            longitude = data.longitude,
            selectedDistrict = "${data.provinceName}, ${data.cityName}, ${data.districtName}",
            formattedAddress = "${data.districtName}, ${data.cityName}, ${data.provinceName}"
        )
    }
}

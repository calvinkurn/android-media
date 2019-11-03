package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.domain.model.dropoff.Data
import com.tokopedia.logisticaddaddress.domain.model.dropoff.DropoffUiModel
import com.tokopedia.logisticaddaddress.domain.model.dropoff.GetStoreResponse
import com.tokopedia.logisticdata.data.entity.address.LocationDataModel

class GetStoreMapper {
    fun map(input: GetStoreResponse): DropoffUiModel {
        return DropoffUiModel(
                input.keroAddressStoreLocation.data.map { it.toUiModel() },
                input.keroAddressStoreLocation.globalRadius
        )
    }

    private fun Data.toUiModel(): LocationDataModel = LocationDataModel(
            addrId, addrName, address1, address2, city, cityName, country, district, districtName,
            latitude, longitude, openingHours, phone, postalCode, province, provinceName,
            receiverName, status, storeCode, storeDistance, type
    )
}
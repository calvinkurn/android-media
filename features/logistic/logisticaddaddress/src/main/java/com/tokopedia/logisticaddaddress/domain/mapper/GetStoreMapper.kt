package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.data.entity.response.Data
import com.tokopedia.logisticaddaddress.features.dropoff_picker.model.DropoffNearbyModel
import com.tokopedia.logisticaddaddress.features.dropoff_picker.model.DropoffUiModel
import com.tokopedia.logisticaddaddress.data.entity.response.GetStoreResponse
import com.tokopedia.logisticdata.data.entity.address.LocationDataModel
import javax.inject.Inject


class GetStoreMapper @Inject constructor() {

    fun map(input: GetStoreResponse): DropoffUiModel {
        return DropoffUiModel(
                input.keroAddressStoreLocation.data.map { it.toUiModel() },
                input.keroAddressStoreLocation.globalRadius
        )
    }

    fun mapToIntentModel(input: DropoffNearbyModel): LocationDataModel = LocationDataModel(
            input.addrId, input.addrName, input.address1, input.address2, input.city,
            input.cityName, input.country, input.district, input.districtName,
            input.latitude, input.longitude, input.openingHours, input.phone, input.postalCode,
            input.province, input.provinceName, input.receiverName, input.status, input.storeCode,
            input.storeDistance
    )

    private fun Data.toUiModel(): DropoffNearbyModel = DropoffNearbyModel(
            addrId, addrName, address1, address2, city, cityName, country, district, districtName,
            latitude, longitude, openingHours, phone, postalCode, province, provinceName,
            receiverName, status, storeCode, storeDistance, type
    )
}
package com.tokopedia.dropoff.domain.mapper

import com.tokopedia.dropoff.data.response.getStore.Data
import com.tokopedia.dropoff.data.response.getStore.GetStoreResponse
import com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffUiModel
import com.tokopedia.logisticdata.data.entity.address.LocationDataModel
import javax.inject.Inject


class GetStoreMapper @Inject constructor() {

    fun map(input: GetStoreResponse): DropoffUiModel {
        return DropoffUiModel(
                input.keroAddressStoreLocation.data.map { it.toUiModel() },
                input.keroAddressStoreLocation.globalRadius
        )
    }

    fun mapToIntentModel(input: com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffNearbyModel): LocationDataModel = LocationDataModel(
            input.addrId, input.addrName, input.address1, input.address2, input.city,
            input.cityName, input.country, input.district, input.districtName,
            input.latitude, input.longitude, input.openingHours, input.phone, input.postalCode,
            input.province, input.provinceName, input.receiverName, input.status, input.storeCode,
            input.storeDistance
    )

    private fun Data.toUiModel(): com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffNearbyModel = com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffNearbyModel(
            addrId, addrName, address1, address2, city, cityName, country, district, districtName,
            latitude, longitude, openingHours, phone, postalCode, province, provinceName,
            receiverName, status, storeCode, storeDistance, type
    )
}
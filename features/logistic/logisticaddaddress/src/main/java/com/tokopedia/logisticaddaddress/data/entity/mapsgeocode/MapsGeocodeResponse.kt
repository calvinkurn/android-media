package com.tokopedia.logisticaddaddress.data.entity.mapsgeocode

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.CoordinateModel
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.Location

data class MapsGeocodeResponse(
    @SerializedName("KeroAddressGeocode")
    var keroAddressGeocode: KeroAddressGeocode? = null
) {
    val firstLocation: Location?
        get() = keroAddressGeocode?.data?.firstOrNull()?.geometry?.location
}

data class KeroAddressGeocode(
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("error_code")
    var errorCode: Int? = null,
    @SerializedName("data")
    var data: List<CoordinateModel>? = null
)

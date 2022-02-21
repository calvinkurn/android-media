package com.tokopedia.tradein.model.request


import com.google.gson.annotations.SerializedName

data class GetTradeInDetailInput(
    @SerializedName("AppDeviceId")
    var appDeviceId: String,
    @SerializedName("ModelInfo")
    var modelInfo: String,
    @SerializedName("OriginalPrice")
    var originalPrice: Int,
    @SerializedName("SessionId")
    var sessionId: String,
    @SerializedName("TraceId")
    var traceId: String,
    @SerializedName("UniqueCode")
    var uniqueCode: String,
    @SerializedName("UserId")
    var userId: String,
    @SerializedName("UserLocation")
    var userLocation: UserLocation
) {
    data class UserLocation(
        @SerializedName("CityId")
        var cityId: String,
        @SerializedName("DistrictId")
        var districtId: String,
        @SerializedName("Latitude")
        var latitude: String,
        @SerializedName("Longitude")
        var longitude: String,
        @SerializedName("PostalCode")
        var postalCode: String
    )
}
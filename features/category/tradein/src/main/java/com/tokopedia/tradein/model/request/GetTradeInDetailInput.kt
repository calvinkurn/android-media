package com.tokopedia.tradein.model.request


import com.google.gson.annotations.SerializedName

data class GetTradeInDetailInput(
    @SerializedName("AppDeviceId")
    var appDeviceId: String,
    @SerializedName("ModelInfo")
    var modelInfo: String,
    @SerializedName("DeviceSignature")
    var deviceSignature: String,
    @SerializedName("OriginalPrice")
    var originalPrice: Double,
    @SerializedName("SessionId")
    var sessionId: String,
    @SerializedName("ShopID")
    var shopID: String,
    @SerializedName("TraceId")
    var traceId: String,
    @SerializedName("UniqueCode")
    var uniqueCode: String,
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
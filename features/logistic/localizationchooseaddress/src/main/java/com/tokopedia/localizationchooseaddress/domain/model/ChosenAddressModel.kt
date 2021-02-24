package com.tokopedia.localizationchooseaddress.domain.model

data class ChosenAddressModel (
        var addressId: Int = 0,
        var receiverName: String = "",
        var addressName: String = "",
        var districtId: Int = 0,
        var cityId: Int = 0,
        var cityName: String = "",
        var districtName: String = "",
        var status: Int = 0,
        var latitude: String = "",
        var longitude: String = "",
        var postalCode: String = "",
)
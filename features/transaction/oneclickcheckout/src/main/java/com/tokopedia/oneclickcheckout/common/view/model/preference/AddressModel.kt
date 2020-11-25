package com.tokopedia.oneclickcheckout.common.view.model.preference

data class AddressModel(
        var latitude: String = "",
        var addressId: Int = 0,
        var addressName: String = "",
        var provinceName: String = "",
        var districtName: String = "",
        var cityName: String = "",
        var provinceId: Int = 0,
        var phone: String = "",
        var addressStreet: String = "",
        var receiverName: String = "",
        var districtId: Int = 0,
        var postalCode: String = "",
        var cityId: Int = 0,
        var longitude: String = "",
        var fullAddress: String = ""
)

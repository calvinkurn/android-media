package com.tokopedia.oneclickcheckout.common.view.model.preference

data class AddressModel(
        var addressId: Long = 0,
        var addressName: String = "",
        var provinceName: String = "",
        var districtName: String = "",
        var cityName: String = "",
        var provinceId: Long = 0,
        var phone: String = "",
        var addressStreet: String = "",
        var receiverName: String = "",
        var districtId: Long = 0,
        var postalCode: String = "",
        var cityId: Long = 0,
        var longitude: String = "",
        var latitude: String = "",
        var fullAddress: String = ""
)

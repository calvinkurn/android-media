package com.tokopedia.localizationchooseaddress.domain.model

data class ChosenAddressListModel (
        var chosenAddressList: List<ChosenAddressList> = listOf()
)

data class ChosenAddressList(
        var addressId: String = "",
        var receiverName: String = "Tokopedia",
        var addressname: String = "Tokopedia tower",
        var address1: String = "Jalan jalan ke pasar tua",
        var address2: String = "",
        var postalCode: String = "",
        var provinceId: String = "",
        var cityId: String = "",
        var districtId: String = "",
        var phone: String = "0821321342123",
        var provinceName: String = "",
        var cityName: String = "",
        var districtName: String = "Gambir, Jakarta Selatan",
        var status: String = "",
        var country: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var isStateChosenAddress: Boolean = false,
)
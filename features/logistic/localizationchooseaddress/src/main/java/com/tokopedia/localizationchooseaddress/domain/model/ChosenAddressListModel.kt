package com.tokopedia.localizationchooseaddress.domain.model

class ChosenAddressListModel (
        var chosenAddressList: List<ChosenAddressModel> = listOf()
)

class ChosenAddressModel(
        var addressId: String = "",
        var receiverName: String = "",
        var addressname: String = "",
        var address1: String = "",
        var address2: String = "",
        var postalCode: String = "",
        var provinceId: String = "",
        var cityId: String = "",
        var districtId: String = "",
        var phone: String = "",
        var provinceName: String = "",
        var cityName: String = "",
        var districtName: String = "",
        var status: String = "",
        var country: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var isStateChosenAddress: Boolean = false,
)
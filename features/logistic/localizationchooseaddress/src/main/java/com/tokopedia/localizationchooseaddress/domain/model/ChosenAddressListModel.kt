package com.tokopedia.localizationchooseaddress.domain.model

sealed class ChosenAddressListVisitable

data class ChosenAddressListModel (
        var chosenAddressList: List<ChosenAddressList> = listOf()
)

data class ChosenAddressList(
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
        var status: Int = -1,
        var country: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var isStateChosenAddress: Boolean = false,
) :  ChosenAddressListVisitable()

data class OtherAddressModel(var text: String? = null) : ChosenAddressListVisitable()
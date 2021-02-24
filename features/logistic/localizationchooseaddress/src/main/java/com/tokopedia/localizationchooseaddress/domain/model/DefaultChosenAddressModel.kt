package com.tokopedia.localizationchooseaddress.domain.model

import com.google.gson.annotations.SerializedName

data class DefaultChosenAddressModel(
        var addressData: DefaultChosenAddress = DefaultChosenAddress(),
        var keroAddrError: KeroDefaultAddressError = KeroDefaultAddressError()

)

data class DefaultChosenAddress(
        var addressId: Int = 0,
        var receiverName: String = "",
        var addressName: String = "",
        var address1: String = "",
        var address2: String = "",
        var postalCode: Int = 0,
        var provinceId: Int = 0,
        var cityId: Int = 0,
        var districtId: Int = 0,
        var phone: String = "",
        var provinceName: String = "",
        var cityName: String = "",
        var districtName: String = "",
        var status: Int = 0,
        var country: String = "",
        var latitude: String = "",
        var longitude: String = ""
)

data class KeroDefaultAddressError(
        var code: Int = 0,
        var detail: String = ""
)
package com.tokopedia.home.beranda.data.model

data class HomeChooseAddressData(
        var isActive: Boolean = true,
        val lat: String = "",
        val long: String = "",
        val addressId: String = "",
        val cityId: String = "",
        val districId: String = "",
        val postCode: String = ""
)
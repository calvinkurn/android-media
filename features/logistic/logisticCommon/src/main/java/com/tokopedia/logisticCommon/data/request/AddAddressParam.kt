package com.tokopedia.logisticCommon.data.request

data class AddAddressParam(
        val addr_name: String,
        val receiver_name: String,
        val address_1: String,
        var address_2: String = "",
        val postal_code: String,
        val phone: String,
        val province: String,
        val city: String,
        val district: String,
        val latitude: String,
        val longitude: String,
        var is_ana_positive: String,
        var checksum: String = "",
        var feature: String? = null
) {
    fun toMap(): Map<String, Any> = mapOf(
            "addr_name" to addr_name,
            "receiver_name" to receiver_name,
            "address_1" to address_1,
            "address_2" to address_2,
            "postal_code" to postal_code,
            "phone" to phone,
            "province" to province,
            "city" to city,
            "district" to district,
            "latitude" to latitude,
            "longitude" to longitude,
            "is_ana_positive" to is_ana_positive
    )
}
package com.tokopedia.logisticaddaddress.data.entity.request

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
        var feature: String? = null,
        var apply_name_as_new_user_fullname: Boolean = false,
        var set_as_primary_address: Boolean = false
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
            "is_ana_positive" to is_ana_positive,
            "apply_name_as_new_user_fullname" to apply_name_as_new_user_fullname,
            "set_as_primary_address" to set_as_primary_address,
            // hardcode to get tokonow shop & warehouse
            "is_tokonow_request" to true
    )
}
package com.tokopedia.logisticCommon.data.request

data class EditAddressParam(
        val addressId: Long,
        val addressName: String,
        val receiverName: String,
        val address1: String,
        val postalCode: String,
        val district: String,
        val city: String,
        val province: String,
        val phone: String,
        val latitude: String,
        val longitude: String,

) {
    fun toMap(): Map<String, Any> = mapOf(
            "addr_id" to addressId,
            "addr_name" to addressName,
            "receiver_name" to receiverName,
            "address_1" to address1,
            "postal_code" to postalCode,
            "phone" to phone,
            "province" to province,
            "city" to city,
            "district" to district,
            "latitude" to latitude,
            "longitude" to longitude,
    )
}
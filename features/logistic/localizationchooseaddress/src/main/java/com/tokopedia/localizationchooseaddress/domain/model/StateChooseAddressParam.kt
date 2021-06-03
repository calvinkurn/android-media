package com.tokopedia.localizationchooseaddress.domain.model

data class StateChooseAddressParam(
        val status: Int,
        val addressId: Int,
        val receiverName: String,
        val addressName: String,
        val latitude: String,
        val longitude: String,
        val districtId: Int,
        val postalCode: String,
        val isTokonow: Boolean
) {

    fun toMap(): Map<Any, Any> = mapOf(
            "status" to status,
            "addr_id" to addressId,
            "addr_name" to addressName,
            "receiver_name" to receiverName,
            "district" to districtId,
            "latitude" to latitude,
            "longitude" to longitude,
            "postal_code" to postalCode,
            "is_tokonow_request" to isTokonow
    )
}
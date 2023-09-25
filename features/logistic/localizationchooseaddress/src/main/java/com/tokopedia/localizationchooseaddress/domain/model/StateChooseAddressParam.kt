package com.tokopedia.localizationchooseaddress.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class StateChooseAddressParam(
    val status: Int,
    val addressId: Long?,
    val receiverName: String,
    val addressName: String,
    val latitude: String,
    val longitude: String,
    val districtId: Long,
    val postalCode: String,
    val isTokonow: Boolean
) : GqlParam {

    fun toMap(): Map<Any?, Any?> = mapOf(
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

data class KeroAddrSetStateChosenAddressInput(
    @SerializedName("input")
    val input: StateChooseAddressParam
)

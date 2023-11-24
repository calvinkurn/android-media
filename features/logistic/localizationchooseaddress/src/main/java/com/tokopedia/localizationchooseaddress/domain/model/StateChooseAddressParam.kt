package com.tokopedia.localizationchooseaddress.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class StateChooseAddressParam(
    @SerializedName("status")
    val status: Int,
    @SerializedName("addr_id")
    val addressId: Long?,
    @SerializedName("receiver_name")
    val receiverName: String,
    @SerializedName("addr_name")
    val addressName: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("district")
    val districtId: Long,
    @SerializedName("postal_code")
    val postalCode: String,
    @SerializedName("is_tokonow_request")
    val isTokonow: Boolean
) : GqlParam

data class KeroAddrSetStateChosenAddressInput(
    @SerializedName("input")
    val input: StateChooseAddressParam
) : GqlParam

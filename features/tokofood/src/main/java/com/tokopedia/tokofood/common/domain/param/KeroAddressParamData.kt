package com.tokopedia.tokofood.common.domain.param

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class KeroAddressParamData(
    @SuppressLint("Invalid Data Type")
    @SerializedName("addr_id")
    val addressId: Long = 0,
    @SerializedName("addr_name")
    val addressName: String = "",
    @SerializedName("address_1")
    val firstAddress: String = "",
    @SerializedName("address_2")
    val secondAddress: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("district")
    val district: String = "",
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("postal_code")
    val postalCode: String = "",
    @SerializedName("province")
    val province: String = "",
    @SerializedName("receiver_name")
    val receiverName: String = "",
)
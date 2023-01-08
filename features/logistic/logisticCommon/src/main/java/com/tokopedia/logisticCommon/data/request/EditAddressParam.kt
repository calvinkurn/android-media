package com.tokopedia.logisticCommon.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class EditAddressParam(
    @SuppressLint("Invalid Data Type")
    @SerializedName("addr_id")
    val addressId: Long,
    @SerializedName("addr_name")
    val addressName: String,
    @SerializedName("receiver_name")
    val receiverName: String,
    @SerializedName("address_1")
    val address1: String,
    @SerializedName("address_1_notes")
    val address1Notes: String,
    @SerializedName("address_2")
    val address2: String,
    @SerializedName("postal_code")
    val postalCode: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("province")
    val province: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("is_tokonow_request")
    val isTokonowRequest: Boolean
)

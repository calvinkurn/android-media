package com.tokopedia.tokofood.purchase.purchasepage.domain.model.param

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KeroEditAddressParam(
    @SuppressLint("Invalid Data Type")
    @SerializedName("addr_id")
    @Expose
    val addressId: Long,
    @SerializedName("addr_name")
    @Expose
    val addressName: String,
    @SerializedName("address_1")
    @Expose
    val firstAddress: String,
    @SerializedName("address_2")
    @Expose
    val secondAddress: String,
    @SerializedName("city")
    @Expose
    val city: String,
    @SerializedName("district")
    @Expose
    val district: String,
    @SerializedName("latitude")
    @Expose
    val latitude: String,
    @SerializedName("longitude")
    @Expose
    val longitude: String,
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("postal_code")
    @Expose
    val postalCode: String,
    @SerializedName("province")
    @Expose
    val province: String,
    @SerializedName("receiver_name")
    @Expose
    val receiverName: String,
)
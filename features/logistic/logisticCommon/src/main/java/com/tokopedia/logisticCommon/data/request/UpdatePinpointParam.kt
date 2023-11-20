package com.tokopedia.logisticCommon.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class EditPinpointParam(
    @SuppressLint("Invalid Data Type")
    @SerializedName("addr_id")
    val addressId: Long,
    @SerializedName("addr_name")
    val addressName: String,
    @SerializedName("receiver_name")
    val receiverName: String,
    @SerializedName("address_1")
    val address1: String,
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
    val phone: String
) : GqlParam

data class UpdatePinpointParam(
    @SerializedName("input")
    val input: EditPinpointParam
) : GqlParam

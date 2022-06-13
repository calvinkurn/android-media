package com.tokopedia.manageaddress.domain.model.shareaddress

import com.google.gson.annotations.SerializedName

data class ShareAddressParam(
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("address")
    val addressId: String,
)
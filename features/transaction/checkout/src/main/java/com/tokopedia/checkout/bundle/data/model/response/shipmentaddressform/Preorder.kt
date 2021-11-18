package com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class Preorder(
        @SerializedName("is_preorder")
        val isPreorder: Boolean = false,
        @SerializedName("duration")
        val duration: String = ""
)
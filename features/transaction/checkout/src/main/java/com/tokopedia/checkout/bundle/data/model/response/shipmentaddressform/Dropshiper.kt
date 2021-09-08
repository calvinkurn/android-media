package com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class Dropshiper(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("telp_no")
        val telpNo: String = ""
)
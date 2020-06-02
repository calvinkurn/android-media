package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header (
    @SerializedName("content-type")
    @Expose
    var contentType: String = "",

    @SerializedName("itemLabel")
    @Expose
    var itemLabel: String = "",

    @SerializedName("poweredBy")
    @Expose
    var poweredBy: String = "",

    @SerializedName("statusLabel")
    @Expose
    var statusLabel: String = "",

    @SerializedName("voucherCodes")
    @Expose
    var voucherCodes: String = ""
)

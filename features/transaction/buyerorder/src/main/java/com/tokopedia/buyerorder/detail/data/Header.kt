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

    @SerializedName("item_label")
    @Expose
    var item_label: String = "",

    @SerializedName("poweredBy")
    @Expose
    var poweredBy: String = "",

    @SerializedName("powered_by")
    @Expose
    var powered_by: String = "",

    @SerializedName("statusLabel")
    @Expose
    var statusLabel: String = "",

    @SerializedName("status_label")
    @Expose
    var status_label: String = "",

    @SerializedName("voucherCodes")
    @Expose
    var voucherCodes: String = ""
)

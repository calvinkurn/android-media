package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header (
    @SerializedName("content-type")
    @Expose
    val contentType: String = "",

    @SerializedName("itemLabel")
    @Expose
    val itemLabel: String = "",

    @SerializedName("item_label")
    @Expose
    val item_label: String = "",

    @SerializedName("poweredBy")
    @Expose
    val poweredBy: String = "",

    @SerializedName("powered_by")
    @Expose
    val powered_by: String = "",

    @SerializedName("statusLabel")
    @Expose
    val statusLabel: String = "",

    @SerializedName("status_label")
    @Expose
    val status_label: String = "",

    @SerializedName("voucherCodes")
    @Expose
    val voucherCodes: String = ""
)

package com.tokopedia.attachvoucher.data.voucherv2


import com.google.gson.annotations.SerializedName

data class Hyperlink(
    @SerializedName("delete")
    val delete: String = "",
    @SerializedName("edit")
    val edit: String = "",
    @SerializedName("edit_quota_ajax")
    val editQuotaAjax: String = "",
    @SerializedName("share")
    val share: String = "",
    @SerializedName("stop")
    val stop: String = ""
)
package com.tokopedia.attachvoucher.data.voucherv2


import com.google.gson.annotations.SerializedName

data class Paging(
    @SerializedName("has_next")
    val hasNext: Boolean = false,
    @SerializedName("has_prev")
    val hasPrev: Boolean = false,
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("per_page")
    val perPage: Int = 0
)
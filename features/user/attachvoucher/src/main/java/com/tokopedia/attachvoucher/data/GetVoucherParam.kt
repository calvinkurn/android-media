package com.tokopedia.attachvoucher.data

import com.google.gson.annotations.SerializedName

data class GetVoucherParam (
    @SerializedName("voucher_status")
    var voucher_status: String,

    @SerializedName("per_page")
    var per_page: Int,

    @SerializedName("page")
    var page: Int,

    @SerializedName("voucher_type")
    var voucher_type: Int? = null
)

data class FilterParam(
    @SerializedName("Filter")
    val Filter: GetVoucherParam
)
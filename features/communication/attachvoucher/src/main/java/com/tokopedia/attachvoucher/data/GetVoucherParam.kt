package com.tokopedia.attachvoucher.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetVoucherParam (
    @SerializedName("voucher_status")
    var voucher_status: String = "",

    @SerializedName("per_page")
    var per_page: Int = 15,

    @SerializedName("page")
    var page: Int = 1,

    @SerializedName("voucher_type")
    var voucher_type: Int? = null,

    @SerializedName("hide_overbook")
    var hideOverbook: String = "1",

    @SerializedName("is_lock_to_product")
    var isLockToProduct: String = "0,1"
)

data class FilterParam(
    @SerializedName("Filter")
    val Filter: GetVoucherParam = GetVoucherParam()
) : GqlParam
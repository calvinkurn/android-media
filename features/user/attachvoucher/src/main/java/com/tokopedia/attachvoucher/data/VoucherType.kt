package com.tokopedia.attachvoucher.data


import com.google.gson.annotations.SerializedName

data class VoucherType(
        @SerializedName("identifier")
        val identifier: String = "",
        @SerializedName("voucher_type")
        val voucherType: Int = 0
) {

}
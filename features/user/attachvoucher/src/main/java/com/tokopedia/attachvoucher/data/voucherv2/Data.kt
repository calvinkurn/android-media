package com.tokopedia.attachvoucher.data.voucherv2


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("paging")
    val paging: Paging = Paging(),
    @SerializedName("vouchers")
    val vouchers: List<Voucher> = listOf()
)
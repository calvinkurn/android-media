package com.tokopedia.attachvoucher.data


import com.google.gson.annotations.SerializedName

data class Amount(
        @SerializedName("amount")
        val amount: Int = 0,
        @SerializedName("amount_type")
        val amountType: Int = 0
)
package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.SerializedName

data class TxStatsDynamicPdp(
        @SerializedName("transactionSuccess")
        val txSuccess: String = "",
        @SerializedName("txReject")
        val txReject: String = "",
        @SerializedName("countSold")
        val countSold: String = ""
)
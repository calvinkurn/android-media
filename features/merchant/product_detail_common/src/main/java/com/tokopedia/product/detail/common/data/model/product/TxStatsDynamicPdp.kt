package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.SerializedName

data class TxStatsDynamicPdp(
        @SerializedName("transactionSuccess")
        val txSuccess: String = "",
        @SerializedName("transactionReject")
        val txReject: String = "",
        @SerializedName("countSold")
        val countSold: String = "",
        @SerializedName("paymentVerified")
        val paymentVerified: String = "",
        @SerializedName("itemSoldPaymentVerified")
        val itemSoldPaymentVerified: String = ""
)
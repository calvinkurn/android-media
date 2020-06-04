package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class TxStats(
        @SerializedName("itemSold")
        val itemSold: Int = 0,
        @SerializedName("txSuccess")
        val txSuccess: Int = 0,
        @SerializedName("txReject")
        val txReject: Int = 0
)
package com.tokopedia.buyerorder.unifiedhistory.list.data.model


import com.google.gson.annotations.SerializedName

data class TrainQueryParams(
    @SerializedName("invoice_id")
    val invoiceId: String = ""
)
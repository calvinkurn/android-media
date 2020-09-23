package com.tokopedia.buyerorder.unifiedhistory.list.data.model


import com.google.gson.annotations.SerializedName

data class FlightQueryParams(
    @SerializedName("invoice_id")
    val invoiceId: String = "",
    @SerializedName("order_status_id")
    val orderStatusId: String = ""
)
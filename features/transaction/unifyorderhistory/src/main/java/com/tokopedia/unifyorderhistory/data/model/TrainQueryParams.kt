package com.tokopedia.unifyorderhistory.data.model

import com.google.gson.annotations.SerializedName

data class TrainQueryParams(
    @SerializedName("invoice_id")
    val invoiceId: String = ""
)

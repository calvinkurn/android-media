package com.tokopedia.attachinvoice.data

import com.google.gson.annotations.SerializedName

data class ParamInvoice(
    @SerializedName("msgId")
    val msgId: Int,

    @SerializedName("page")
    val page: Int
)
package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

/**
 * Created by baghira on 11/05/18.
 */
data class Invoice(
    @SerializedName("invoiceRefNum")
    @Expose
    val invoiceRefNum: String = "",

    @SerializedName("invoiceUrl")
    @Expose
    val invoiceUrl: String = ""
)
package com.tokopedia.entertainment.pdp.data.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventPaymentEntity (
        @SerializedName("transaction_id")
        @Expose
        val transactionId: String = ""
)
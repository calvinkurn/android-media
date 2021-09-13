package com.tokopedia.checkout.old.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class Parameter(
        @SerializedName("transaction_id")
        val transactionId: String = ""
)
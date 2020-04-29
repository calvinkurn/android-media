package com.tokopedia.entertainment.pdp.data.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class TaxPerQuantity(
        @SerializedName("entertainment")
        @Expose
        val entertainment: Int = 0,
        @SerializedName("service")
        @Expose
        val service: Int = 0
)

package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class PriceChanges(
        @SerializedName("amount_difference")
        val amountDifference: Long = 0,
        @SerializedName("changes_state")
        val changesState: Int = 0,
        @SerializedName("description")
        val description: String = "",
        @SerializedName("original_amount")
        val originalAmount: Long = 0
)
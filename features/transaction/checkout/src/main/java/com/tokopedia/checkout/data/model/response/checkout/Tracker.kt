package com.tokopedia.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class Tracker(
        @SerializedName("product_changes_type")
        val productChangesType: String = "",
        @SerializedName("campaign_type")
        val campaignType: String = "",
        @SerializedName("product_ids")
        val productIds: List<Long> = emptyList()
)
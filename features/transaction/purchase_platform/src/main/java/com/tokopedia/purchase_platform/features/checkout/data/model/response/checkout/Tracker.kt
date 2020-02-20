package com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-11-19.
 */

data class Tracker(
        @SerializedName("product_changes_type")
        val productChangesType: String = "",

        @SerializedName("campaign_type")
        val campaignType: String = "",

        @SerializedName("product_ids")
        val productIds: List<Long> = emptyList()
)
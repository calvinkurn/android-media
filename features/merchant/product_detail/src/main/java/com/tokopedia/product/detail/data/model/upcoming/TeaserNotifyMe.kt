package com.tokopedia.product.detail.data.model.upcoming

import com.google.gson.annotations.SerializedName

data class TeaserNotifyMe(
        @SerializedName("checkCampaignNotifyMe")
        val result: Result = Result()
)

data class Result(
        @SerializedName("campaign_id")
        val campaignId: Int = 0,

        @SerializedName("product_id")
        val productId: Int = 0,

        @SerializedName("success")
        val isSuccess: Boolean = false,

        @SerializedName("ErrorMessage")
        val errorMessage: String = ""
)
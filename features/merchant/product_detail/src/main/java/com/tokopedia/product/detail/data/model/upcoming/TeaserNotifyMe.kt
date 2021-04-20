package com.tokopedia.product.detail.data.model.upcoming

import com.google.gson.annotations.SerializedName

data class TeaserNotifyMe(
        @SerializedName("checkCampaignNotifyMe")
        val result: Result = Result()
)

data class Result(
        @SerializedName("success")
        val isSuccess: Boolean = false,

        @SerializedName("ErrorMessage")
        val errorMessage: String = ""
)
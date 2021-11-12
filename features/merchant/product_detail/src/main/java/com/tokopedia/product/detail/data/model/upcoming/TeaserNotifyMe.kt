package com.tokopedia.product.detail.data.model.upcoming

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TeaserNotifyMe(
        @SerializedName("checkCampaignNotifyMe")
        @Expose
        val result: Result = Result()
)

data class Result(
        @SerializedName("success")
        @Expose
        val isSuccess: Boolean = false,

        @SerializedName("error_message")
        @Expose
        val errorMessage: String = "",

        @SerializedName("message")
        @Expose
        val message: String = ""
)
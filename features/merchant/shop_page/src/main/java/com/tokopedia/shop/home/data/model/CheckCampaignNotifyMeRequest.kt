package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckCampaignNotifyMeRequest(
    @SerializedName("campaign_id")
    @Expose
    var campaignId: Long = 0,

    @SerializedName("action")
    @Expose
    var action: String = "",

    @SerializedName("source")
    @Expose
    var source: String = "",

    @SerializedName("requestType")
    @Expose
    var requestType: String = ""
)

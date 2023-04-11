package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetCampaignNotifyMeRequest(
    @SerializedName("campaign_id")
    @Expose
    var campaignId: Long = 1,

    @SerializedName("source")
    @Expose
    var source: String = ""
)

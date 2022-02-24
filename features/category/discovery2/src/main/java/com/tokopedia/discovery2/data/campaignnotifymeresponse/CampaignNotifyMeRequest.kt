package com.tokopedia.discovery2.data.campaignnotifymeresponse

import com.google.gson.annotations.SerializedName

data class CampaignNotifyMeRequest(
        @SerializedName("campaign_id")
        var campaignID: Int? = 0,
        @SerializedName("product_id")
        var productID: Long? = 0,
        @SerializedName("source")
        var source: String? = "DISCOVERY",
        @SerializedName("action")
        var action: String? = "REGISTER"
)
package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetMerchantCampaignTNCRequest(
    @SerializedName("campaign_id")
    @Expose
    var campaignId: Long = 1,
    @SerializedName("action_from")
    @Expose
    var actionFrom: String = ""
)

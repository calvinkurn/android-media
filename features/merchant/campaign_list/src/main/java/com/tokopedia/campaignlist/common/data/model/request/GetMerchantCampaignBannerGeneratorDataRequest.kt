package com.tokopedia.campaignlist.common.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetMerchantCampaignBannerGeneratorDataRequest(
        @SerializedName("CampaignID")
        @Expose var campaignId: String = "",
        @SerializedName("Rows")
        @Expose var rows: Int = 0,
        @SerializedName("source")
        @Expose var source: String = ""
)
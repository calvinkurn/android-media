package com.tokopedia.flashsale.management.data.campaignlabel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CampaignStatus(
        @SerializedName("label_name")
        @Expose
        var name: String = "",
        @SerializedName("status_id")
        @Expose
        var id: List<Int> = listOf()
)
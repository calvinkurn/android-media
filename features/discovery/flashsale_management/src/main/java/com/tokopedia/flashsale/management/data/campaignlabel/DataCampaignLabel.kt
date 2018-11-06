package com.tokopedia.flashsale.management.data.campaignlabel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataCampaignLabel(
        @SerializedName("data")
        @Expose
        val data: List<CampaignStatus> = listOf()
){
    data class Response(@SerializedName("getMojitoCampaignLabel")
                        @Expose val result: DataCampaignLabel)
}
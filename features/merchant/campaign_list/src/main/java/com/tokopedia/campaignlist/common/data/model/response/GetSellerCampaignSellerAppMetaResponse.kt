package com.tokopedia.campaignlist.common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetSellerCampaignSellerAppMetaResponse(
        @SerializedName("getSellerCampaignSellerAppMeta")
        @Expose val getSellerCampaignSellerAppMeta: GetSellerCampaignSellerAppMeta = GetSellerCampaignSellerAppMeta()
)

data class GetSellerCampaignSellerAppMeta(
        @SerializedName("campaign_status")
        @Expose val campaignStatus: List<CampaignStatus> = listOf(),
        @SerializedName("campaign_type_data")
        @Expose val campaignTypeData: List<CampaignTypeData> = listOf(),
)

data class CampaignStatus(
        @SerializedName("status")
        @Expose val status: List<Int> = emptyList(),
        @SerializedName("status_text")
        @Expose val statusText: String = ""
)

data class CampaignTypeData(
        @SerializedName("campaign_type_id")
        @Expose val campaignTypeId: String = "",
        @SerializedName("campaign_type_name")
        @Expose val campaignTypeName: String = "",
        @SerializedName("status")
        @Expose val status: Int = 0,
        @SerializedName("status_text")
        @Expose val statusText: String = ""
)
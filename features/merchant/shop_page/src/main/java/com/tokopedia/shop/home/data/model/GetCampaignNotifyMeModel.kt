package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.SerializedName

data class GetCampaignNotifyMeModel(
    @SerializedName("campaign_id")
    val campaignId: String = "",

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("errorMessage")
    val errorMessage: String = "",

    @SerializedName("is_available")
    val isAvailable: Boolean = false

) {
    data class Response(
        @SerializedName("getCampaignNotifyMe")
        val getCampaignNotifyMeModel: GetCampaignNotifyMeModel = GetCampaignNotifyMeModel()
    )
}

package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.SerializedName

data class CheckCampaignNotifyMeModel(
    @SerializedName("campaign_id")
    val campaignId: String = "",

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("error_message")
    val errorMessage: String = ""

) {
    data class Response(
        @SerializedName("checkCampaignNotifyMe")
        val checkCampaignNotifyMeModel: CheckCampaignNotifyMeModel = CheckCampaignNotifyMeModel()
    )
}

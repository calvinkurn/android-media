package com.tokopedia.discovery2.data.campaignnotifymeresponse

import com.google.gson.annotations.SerializedName

class CampaignNotifyMeResponse(
        @SerializedName("checkCampaignNotifyMe")
        val checkCampaignNotifyMeResponse: CheckCampaignNotifyMeResponse? = null
) {
    data class CheckCampaignNotifyMeResponse(
            @SerializedName("campaign_id")
            val campaignID: Int? = 0,
            @SerializedName("product_id")
            val productID: Long? = 0,
            @SerializedName("success")
            val success: Boolean? = false,
            @SerializedName("message")
            val message: String? = "",
            @SerializedName("error_message")
            val errorMessage: String? = ""

    )
}
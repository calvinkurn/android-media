package com.tokopedia.feedcomponent.data.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author by shruti on 30/08/22
 */

data class CheckUpcomingCampaign(
    @SerializedName("getCampaignNotifyMe")
    val response: UpcomingCampaignResponse = UpcomingCampaignResponse(),
)

data class PostUpcomingCampaign(
    @SerializedName("checkCampaignNotifyMe")
    val response: UpcomingCampaignResponse = UpcomingCampaignResponse(),
)

data class UpcomingCampaignResponse(
    @SerializedName("campaign_id")
    val id: String = "",

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("error_message")
    val errorMessage: String = "",

    @SerializedName("is_available")
    val isAvailable: Boolean = false,
)

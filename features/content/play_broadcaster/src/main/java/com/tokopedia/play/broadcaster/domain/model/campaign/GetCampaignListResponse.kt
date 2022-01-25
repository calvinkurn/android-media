package com.tokopedia.play.broadcaster.domain.model.campaign

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by meyta.taliti on 25/01/22.
 */
data class GetCampaignListResponse(
    @Expose
    @SerializedName("getSellerCampaignList")
    val getSellerCampaignList: GetSellerCampaignList = GetSellerCampaignList()
) {

    data class GetSellerCampaignList(
        @Expose
        @SerializedName("campaign")
        val campaigns: List<Campaign> = listOf()
    )

    data class Campaign(
        @Expose
        @SerializedName("campaign_id")
        val campaignId: String = ""
    )
}
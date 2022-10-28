package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GetSellerCampaignEligibilityResponse(
    @SerializedName("getSellerCampaignEligibility")
    val campaignEligibility: CampaignEligibility = CampaignEligibility()
) {
    data class CampaignEligibility(
        @SerializedName("is_eligible")
        val isEligible: Boolean = false
    )
}
package com.tokopedia.campaignlist.page.presentation.model

data class ActiveCampaign(
        val campaignId: String = "",
        val campaignType: String = "",
        val campaignTypeId : String = "",
        val campaignStatus: String = "",
        val campaignStatusId: String = "",
        val campaignPictureUrl: String = "",
        val campaignName: String = "",
        val productQty: String = "",
        val startDate: String = "",
        val endDate: String = "",
        val startTime: String = "",
        val endTime: String = ""
)
package com.tokopedia.campaignlist.common.data.model

data class CampaignListShareModel(
        val sharingText: String = "",
        val sharingUrl: String = "",
        val userId: String = "",
        val pageId: String = "",
        val thumbNailTitle: String = "",
        val thumbNailImage: String = "",
        val ogImageUrl: String = "",
        val specificPageName: String = "",
        val specificPageDescription: String = "",
)
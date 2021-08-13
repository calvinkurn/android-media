package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.SerializedName

data class ThematicCampaign (
        @SerializedName("campaignName")
        val campaignName: String = "",
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("background")
        val background: String = "",
        @SerializedName("additionalInfo")
        val additionalInfo: String = ""
)
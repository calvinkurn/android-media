package com.tokopedia.product.manage.feature.campaignstock.domain.model.param

import com.google.gson.annotations.SerializedName

data class OtherCampaignStockParam(
        @SerializedName("variant")
        val variant: Boolean = true,
        @SerializedName("edit")
        val edit: Boolean = true,
        @SerializedName("campaign")
        val campaign: Boolean = true
)
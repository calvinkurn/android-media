package com.tokopedia.campaignlist.common.data.model.request

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetCampaignListV2Request(
        @SerializedName("rows")
        @Expose var rows: Int = 0,
        @SerializedName("offset")
        @Expose var offset: Int = 0,
        @SerializedName("is_joined")
        @Expose var is_joined: Boolean = false,
        @SerializedName("campaign_type")
        @Expose var campaignType: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("status_id")
        @Expose var statusId: List<Int> = listOf(),
        @SerializedName("list_type")
        @Expose var listType: Int = 0,
        @SerializedName("campaign_name")
        @Expose var campaignName: String = ""
)

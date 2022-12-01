package com.tokopedia.topads.sdk.domain.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlashSaleCampaignDetail(
    @SerializedName("campaign_id")
    val campaignId: String = "",
    @SerializedName("campaign_type")
    val campaignType: String = "",
    @SerializedName("end_time")
    val endTime: String = "",
    @SerializedName("start_time")
    val startTime: String = ""
) : Parcelable

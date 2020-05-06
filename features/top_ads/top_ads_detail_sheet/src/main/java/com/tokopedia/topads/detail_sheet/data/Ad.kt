package com.tokopedia.topads.detail_sheet.data


import com.google.gson.annotations.SerializedName

data class Ad(
    @SerializedName("ad_budget")
    val adBudget: String?,
    @SerializedName("ad_end_date")
    val adEndDate: String?,
    @SerializedName("ad_end_time")
    val adEndTime: String?,
    @SerializedName("ad_id")
    val adId: String?,
    @SerializedName("ad_schedule")
    val adSchedule: String?,
    @SerializedName("ad_start_date")
    val adStartDate: String?,
    @SerializedName("ad_start_time")
    val adStartTime: String?,
    @SerializedName("group_id")
    val groupId: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("status_desc")
    val statusDesc: String?
)
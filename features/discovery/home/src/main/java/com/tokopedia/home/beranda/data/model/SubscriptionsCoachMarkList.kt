package com.tokopedia.home.beranda.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SubscriptionsCoachMarkList(
    @SerializedName("CoachMark")
    @Expose
    val coachMark: List<SubscriptionsCoachMarkData> = listOf(),
    @SerializedName("Type")
    @Expose
    val type: String = ""
)
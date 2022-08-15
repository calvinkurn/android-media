package com.tokopedia.tkpd.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class CampaignParticipationRequestHeader(
    @SerializedName("source")
    val source: String = "fe",
    @SerializedName("ip")
    val ip: String = "",
    @SerializedName("usecase")
    val usecase: String = ""
)
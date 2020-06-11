package com.tokopedia.oneclickcheckout.order.data

import com.google.gson.annotations.SerializedName

data class OccOnboardingCoachMarkDetail(
        @SerializedName("step")
        val step: Int = 0,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("message")
        val message: String = ""
)
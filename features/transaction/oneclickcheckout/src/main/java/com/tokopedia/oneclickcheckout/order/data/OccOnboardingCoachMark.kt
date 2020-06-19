package com.tokopedia.oneclickcheckout.order.data

import com.google.gson.annotations.SerializedName

data class OccOnboardingCoachMark(
        @SerializedName("skip_button_text")
        val skipButtonText: String = "",
        @SerializedName("detail")
        val details: List<OccOnboardingCoachMarkDetail> = emptyList()
)
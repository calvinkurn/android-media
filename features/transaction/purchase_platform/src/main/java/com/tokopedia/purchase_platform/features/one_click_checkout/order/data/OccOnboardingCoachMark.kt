package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class OccOnboardingCoachMark(
        @SerializedName("skip_button_text")
        val skipButtonText: String = "",
        @SerializedName("detail")
        val details: List<OccOnboardingCoachMarkDetail> = emptyList()
)
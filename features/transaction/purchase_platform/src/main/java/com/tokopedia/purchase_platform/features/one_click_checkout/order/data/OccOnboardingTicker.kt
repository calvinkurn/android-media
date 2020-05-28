package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class OccOnboardingTicker(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("show_coachmark_link_text")
        val showActionButton: Boolean = false,
        @SerializedName("coachmark_link_text")
        val actionText: String = ""
)
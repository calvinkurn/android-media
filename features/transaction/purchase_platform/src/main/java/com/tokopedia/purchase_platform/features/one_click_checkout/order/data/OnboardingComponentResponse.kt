package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class OnboardingComponentResponse(
        @SerializedName("header_title")
        val headerTitle: String = "",
        @SerializedName("body_image")
        val bodyImage: String = "",
        @SerializedName("body_message")
        val bodyMessage: String = "",
        @SerializedName("info_component")
        val infoComponent: InfoComponentResponse = InfoComponentResponse()
)
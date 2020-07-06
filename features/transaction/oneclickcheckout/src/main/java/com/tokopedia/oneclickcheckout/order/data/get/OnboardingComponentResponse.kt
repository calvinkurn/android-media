package com.tokopedia.oneclickcheckout.order.data.get

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

data class InfoComponentResponse(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("link")
        val link: String = ""
)
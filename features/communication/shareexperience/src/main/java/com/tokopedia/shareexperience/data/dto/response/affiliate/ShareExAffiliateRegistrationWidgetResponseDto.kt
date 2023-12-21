package com.tokopedia.shareexperience.data.dto.response.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateRegistrationWidgetResponseDto(
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("link")
    val link: String = ""
)

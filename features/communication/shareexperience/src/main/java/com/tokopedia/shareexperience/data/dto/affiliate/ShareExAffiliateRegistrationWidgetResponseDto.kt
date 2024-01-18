package com.tokopedia.shareexperience.data.dto.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateRegistrationWidgetResponseDto(
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("link")
    val link: String = ""
)

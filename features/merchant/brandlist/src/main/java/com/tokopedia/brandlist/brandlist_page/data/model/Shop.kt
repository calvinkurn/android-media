package com.tokopedia.brandlist.brandlist_page.data.model

import com.google.gson.annotations.SerializedName

data class Shop(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("logoUrl")
        val logoUrl: String = "",
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("additionalInformation")
        val additionalInformation: String = "",
        @SerializedName("exclusive_logo_url")
        val exclusiveLogoUrl: String = ""
)
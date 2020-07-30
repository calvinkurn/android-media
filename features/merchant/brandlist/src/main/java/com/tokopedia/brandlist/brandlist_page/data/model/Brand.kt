package com.tokopedia.brandlist.brandlist_page.data.model

import com.google.gson.annotations.SerializedName

data class Brand(
        @SerializedName("defaultUrl")
        val defaultUrl: String = "",
        @SerializedName("exclusiveLogoURL")
        val exclusiveLogoURL: String = "",
        @SerializedName("appsUrl")
        val appsUrl: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("isNew")
        val isNew: Int = 0,
        @SerializedName("logoUrl")
        val logoUrl: String = "",
        @SerializedName("name")
        val name: String = ""
)
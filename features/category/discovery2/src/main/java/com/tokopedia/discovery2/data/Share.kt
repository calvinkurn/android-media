package com.tokopedia.discovery2.data


import com.google.gson.annotations.SerializedName

data class Share(
        @SerializedName("enabled")
        val enabled: Boolean,
        @SerializedName("title")
        val title: String?,
        @SerializedName("image")
        val image: String?,
        @SerializedName("url")
        val url: String?,
        @SerializedName("description")
        val description: String?
)
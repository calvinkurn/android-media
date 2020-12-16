package com.tokopedia.discovery2.data


import com.google.gson.annotations.SerializedName

data class ComponentAdditionalInfo(
        @SerializedName("enabled")
        val enabled: Boolean,
        @SerializedName("product_count")
        val productCount: String,
)
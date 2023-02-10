package com.tokopedia.discovery2.data


import com.google.gson.annotations.SerializedName

data class ComponentAdditionalInfo(
        @SerializedName("enabled")
        val enabled: Boolean,
        @SerializedName("next_page")
        var nextPage: String?,
        @SerializedName("total_product")
        val totalProductData: TotalProductData?,
        @SerializedName("redirection")
        val redirection: Redirection? = null,
)

data class TotalProductData(
        @SerializedName("product_count")
        val productCount: String? = null,
        @SerializedName("product_count_text")
        val productCountText: String? = null,
        @SerializedName("product_count_wording")
        val productCountWording: String? = null,
)

data class Redirection(
    @SerializedName("image_url")
    val image: String? = "",
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("body_text")
    val bodyText: String? = "",
    @SerializedName("link")
    val link: String? = "",
    @SerializedName("applink")
    val applink: String? = "",
)

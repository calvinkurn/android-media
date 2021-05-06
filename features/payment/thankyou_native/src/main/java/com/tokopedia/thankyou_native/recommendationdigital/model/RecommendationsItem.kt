package com.tokopedia.thankyou_native.recommendationdigital.model

import com.google.gson.annotations.SerializedName

data class RecommendationsItem(

        @SerializedName("productId")
        val productId: Long,

        @SerializedName("webLink")
        val webLink: String? = "",

        @SerializedName("description")
        val description: String? = "",

        @SerializedName("clientNumber")
        val clientNumber: String? = "",

        @SerializedName("title")
        val title: String? = "",

        @SerializedName("type")
        val type: String? = "",

        @SerializedName("categoryName")
        val categoryName: String? = "",

        @SerializedName("operatorName")
        val operatorName: String? = "",

        @SerializedName("productName")
        val productName: String? = "",

        @SerializedName("appLink")
        val appLink: String? = "",

        @SerializedName("tagType")
        val tagType: Int,

        @SerializedName("isATC")
        val isATC: Boolean,

        @SerializedName("iconUrl")
        val iconUrl: String? = "",

        @SerializedName("tag")
        val tag: String? = "",

        @SerializedName("productPrice")
        val productPrice: Double
)
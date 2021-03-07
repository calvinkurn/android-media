package com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo

import com.google.gson.annotations.SerializedName

data class RecommendationsItem(

        @field:SerializedName("productId")
        val productId: Long = -1,

        @field:SerializedName("webLink")
        val webLink: String = "",

        @field:SerializedName("description")
        val description: String = "",

        @field:SerializedName("clientNumber")
        val clientNumber: String = "",

        @field:SerializedName("title")
        val title: String = "",

        @field:SerializedName("type")
        val type: String = "",

        @field:SerializedName("categoryName")
        val categoryName: String = "",

        @field:SerializedName("operatorName")
        val operatorName: String? = "",

        @field:SerializedName("productName")
        val productName: String? = "",

        @field:SerializedName("appLink")
        val appLink: String = "",

        @field:SerializedName("tagType")
        val tagType: Int = -1,

        @field:SerializedName("isATC")
        val isATC: Boolean = false,

        @field:SerializedName("iconUrl")
        val iconUrl: String = "",

        @field:SerializedName("position")
        val position: Int = -1,

        @field:SerializedName("tag")
        val tag: String = "",

        @field:SerializedName("operatorID")
        val operatorID: Int = -1,

        @field:SerializedName("categoryId")
        val categoryId: Int = -1,

        @field:SerializedName("productPrice")
        val productPrice: Int = -1
)